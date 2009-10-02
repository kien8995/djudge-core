package djudge.acmcontester.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.net.URL;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.w3c.dom.Element;

import utils.FileWorks;
import utils.XmlWorks;

import db.AbstractTableDataModel;
import db.LanguagesDataModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.dservice.DServiceTaskResult;

public class DServiceConnector extends Thread
{
	private static final Logger log = Logger.getLogger(DServiceConnector.class);
	
	private final String serviceName;
	
	private final String serverUrl;
		
	private final int defaultSleepTime = 1000;
	
	private final int failSleepTime = 10000;
	
	private boolean flagConnected = false;
	
	private int currentSleepTime = defaultSleepTime;
	
	static final String submitMutex = "mutex";
	
	XmlRpcClient client;
	
	
	
	private void updateSubmissionResult(DServiceTaskResult tr)
	{
		SubmissionsDataModel sdm = new SubmissionsDataModel();
		sdm.setWhere("`id` = " + tr.getClientData());
		sdm.updateData();
		sdm.setValueAt(1, 0, SubmissionsDataModel.getDJudgeFlagIndex());
		SubmissionData sd = sdm.getRows().get(0);
		sd.judgement = tr.getJudgement();
		sd.xml = tr.getXml();
		//FIXME
		FileWorks.saveToFile(sd.xml, "./temp/xml.xml");
		Element elem = XmlWorks.getDocument("./temp/xml.xml").getDocumentElement();
		int maxMemory = Integer.parseInt(elem.getAttribute("max-memory"));
		int maxTime = Integer.parseInt(elem.getAttribute("max-time"));
		int wrongTest = Integer.parseInt(elem.getAttribute("wrong-test"));
		int score = Integer.parseInt(elem.getAttribute("score"));
		sd.maxMemory = maxMemory;
		sd.maxTime = maxTime;
		sd.failedTest = wrongTest;
		sd.score = score;
		sdm.setRowData(0, sdm.toRow(sd).data);
	}
	
	public DServiceConnector()
	{
		XMLSettings settings = new XMLSettings(this.getClass());
		serverUrl = settings.getProperty("dservice-url");
		serviceName = settings.getProperty("dservice-name");
		int connectionTimeout = settings.getInt("connnection-timeout", 5000);
		int replyTimeout = settings.getInt("reply-timeout", 5000);
		try
		{
    		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    		config.setServerURL(new URL(serverUrl));
    		config.setEnabledForExtensions(true);
    		config.setConnectionTimeout(connectionTimeout);
    		config.setReplyTimeout(replyTimeout);
    
    		client = new XmlRpcClient();
    		
    		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
    		
    		client.setConfig(config);
    		
    		log.info("AcmContester.DService Connector started (" + serviceName + " at " + serverUrl + ")");
		}
		catch (Exception ex)
		{
			log.error("DServiceConnector start failed", ex);
		}
	}
	
	public void run()
	{
		while (true)
		{
			/* Sending submissions to judge */
			
			synchronized (submitMutex)
			{
				Set<String> sentIDs = new HashSet<String>();
				SubmissionsDataModel sdm = new SubmissionsDataModel();
				// fetching all not judged submissions
				sdm.setWhere("`djudge_flag` = 0");
				sdm.updateData();
				Vector<SubmissionData> vsd = sdm.getRows();
				
				if (vsd.size() > 0)
				{
					log.debug("Non-judged submissions count = " + vsd.size());
					String ids = "";
					for (int i = 0; i < vsd.size(); i++)
						ids += vsd.get(i).id + " ";
					log.debug("IDs: " + ids);
				}
				// for each such submission
				for (int i = 0; i < vsd.size(); i++)
				{
					SubmissionData sd = vsd.get(i);
					if (sentIDs.contains(sd.id))
						continue;
	    			// fetching problem's djudge flags
	    			ProblemsDataModel pdm = new ProblemsDataModel();
	    			pdm.setWhere(" `id` = " + sd.problemID);
	    			pdm.updateData();
	    			ProblemData pd = pdm.getRows().get(0);
	    			// fetching language's djudge flags
	    			LanguagesDataModel ldm = new LanguagesDataModel();
	    			ldm.setWhere(" `id` = " + sd.languageID);
	    			ldm.updateData();
	    			LanguageData ld = ldm.getRows().get(0);
	    			
	    			// building parameters array
	    			Vector <Object> t = new Vector<Object>();
	    			// DService client's ID 
	    			t.add("simpleacm");
	    			// djudge_contets
	    			t.add(pd.djudgeContest);
	    			// djudge_problem
	    			t.add(pd.djudgeProblem);
	    			// djudge_language
	    			t.add(ld.djudgeID);
	    			// course code (Base64 encoded)
	    			t.add(new String(Base64.decodeBase64(sd.sourceCode.getBytes())));
	    			// user's data
	    			t.add(sd.id);
	    			try
	    			{
	    				int result = (Integer) client.execute(serviceName + ".submitSolution", t.toArray());
	    				if (result > 0)
	    				{
	    					sentIDs.add(sd.id);
	    					log.debug(getName() + " " + getId());
	    					log.debug("OK - Sending solution #" + sd.id + " to DService");
	    					sdm.setValueAt(-1, i, SubmissionsDataModel.getDJudgeFlagIndex());
	    				}
	    				else
	    				{
	    					log.debug("Failed - Sending solution #" + sd.id + " to DService: " + result);
	    				}
	    				flagConnected = true;
	    				currentSleepTime = defaultSleepTime;
	    				sdm.getRow(i).save();
	    				ContestServer.getCore().getSubmissionsDataModel().updateData();
	    			}
	    			catch (XmlRpcException ex)
	    			{
	    				if (flagConnected)
	    				{
	    					log.info("Could not connect " + serviceName + " @ " + " " + serverUrl, ex);
	    					flagConnected = false;
	    					currentSleepTime = failSleepTime;
	    				}
	    			}
	    			catch (Exception ex)
	    			{
	    				log.warn("Error while connecting", ex);
	    			}
				}
			}
			/* Fetching results from judge */
			try
			{
				Object obj = client.execute(serviceName + ".fetchResults", new Object[] {"simpleacm"});
				Object[] array = (Object[]) obj;
				if (array != null)
				{
					for (int i = 0; i < array.length; i++)
					{
						@SuppressWarnings("unchecked")
						HashMap<String, String> map = (HashMap<String, String>) array[i];
						DServiceTaskResult tr = new DServiceTaskResult(map);
						updateSubmissionResult(tr);
					}
					ContestServer.getCore().getSubmissionsDataModel().updateData();
				}
				flagConnected = true;
			}
			catch (XmlRpcException ex)
			{
				if (flagConnected)
				{
					log.info("Could not connect " + serviceName + " @ " + " " + serverUrl, ex);
					flagConnected = false;
					currentSleepTime = failSleepTime;
				}
			}
			catch (Exception ex)
			{
				log.warn("Error while connecting", ex);
			}
			
			// Delay before next connection
			try
			{
				sleep(currentSleepTime);
			} catch (InterruptedException e)
			{
			}
		}
	}
}
