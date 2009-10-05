package djudge.acmcontester.server;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import utils.FileWorks;
import utils.XmlWorks;

import db.LanguagesDataModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.dservice.DServiceTaskResult;
import djudge.dservice.DServiceXmlRpcConnector;
import djudge.dservice.interfaces.DServiceXmlRpcInterface;

public class ContesterServer2DServiceLink extends Thread
{
	private static final Logger log = Logger.getLogger(ContesterServer2DServiceLink.class);
	
	private final int defaultSleepTime = 1000;
	
	private final int failSleepTime = 10000;
	
	private boolean flagConnected = false;
	
	private int currentSleepTime = defaultSleepTime;
	
	static final String submitMutex = "mutex";
	
	static final DServiceXmlRpcInterface serverConnector = new DServiceXmlRpcConnector();
	
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
	    			
	    			try
	    			{
	    				Integer result = serverConnector.submitSolution("simpleacm", pd.djudgeContest, pd.djudgeProblem, ld.djudgeID, new String(Base64.decodeBase64(sd.sourceCode.getBytes())), sd.id);
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
	    			catch (Exception ex)
	    			{
	    				///Change to vizi
	    				if (flagConnected)
	    				{
	    					//log.info("Could not connect " + serviceName + " @ " + " " + serverUrl, ex);
	    					flagConnected = false;
	    					currentSleepTime = failSleepTime;
	    				}
	    				log.info("Error while connecting", ex);
	    			}
				}
			}
			/* Fetching results from judge */
			try
			{
				Object obj = serverConnector.fetchResults("simpleacm");
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
			catch (Exception ex)
			{
				if (flagConnected)
				{
					// TODO: vizi
					//log.info("Could not connect " + serviceName + " @ " + " " + serverUrl, ex);
					flagConnected = false;
					currentSleepTime = failSleepTime;
				}
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
