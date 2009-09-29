package djudge.acmcontester.server;

import java.util.HashMap;
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
	
	private static final String serviceName = "DService";
	
	private static final String serverUrl = "http://127.0.0.1:8001/xmlrpc";
		
	private static final int defaultSleepTime = 1000;
	
	private static final int failSleepTime = 10000;
	
	private boolean flagConnected = false;
	
	private int currentSleepTime = defaultSleepTime;
	
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
		sd.maxMemory = maxMemory;
		sd.maxTime = maxTime;
		sd.failedTest = wrongTest;
		sdm.setRowData(0, sdm.toRow(sd).data);
	}
	
	public void run()
	{
		try
		{
    		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    		config.setServerURL(new URL(serverUrl));
    		config.setEnabledForExtensions(true);
    		config.setConnectionTimeout(10000);
    		config.setReplyTimeout(10000);
    
    		client = new XmlRpcClient();
    
    		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
    		
    		client.setConfig(config);
    		
    		log.info("AcmContester.DService Connector started (" + serviceName + " at " + serverUrl + ")");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		while (true)
		{
			/* Sending submissions to judge */
			
			SubmissionsDataModel sdm = new SubmissionsDataModel();
			// fetching all not judged submissions
			sdm.setWhere("`djudge_flag` = 0 AND active > 0");
			sdm.updateData();
			Vector<SubmissionData> vsd = sdm.getRows();
			
			// for each such submission
			for (int i = 0; i < vsd.size(); i++)
			{
				SubmissionData sd = vsd.get(i);
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
    				log.debug("Sending solution #" + sd.id + " to DService");
    				int result = (Integer) client.execute(serviceName + ".submitSolution", t.toArray());
    				if (result > 0)
    				{
    					sdm.setValueAt(-1, i, SubmissionsDataModel.getDJudgeFlagIndex());
    				}
    				flagConnected = true;
    				currentSleepTime = defaultSleepTime;
    				sdm.getRow(i).save();
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
			if (vsd.size() > 0)
			{
				ContestServer.getCore().getSubmissionsDataModel().updateData();
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
