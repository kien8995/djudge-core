package djudge.judge.interfaces;

import java.util.HashMap;
import org.apache.log4j.Logger;

import djudge.dservice.DServiceTask;
import djudge.dservice.DServiceXmlRpcConnector;
import djudge.dservice.interfaces.DServiceXmlRpcInterface;
import djudge.judge.Judge;
import djudge.judge.JudgeTaskDescription;
import djudge.judge.JudgeTaskResult;

public class Judge2DServiceLink extends Thread
{
	private static final Logger log = Logger.getLogger(Judge2DServiceLink.class);
	
	private static int judgeSerialID = 0;
	
	private final DServiceXmlRpcInterface serverConnector = new DServiceXmlRpcConnector();
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		while (true)
		{
			HashMap<String, String> map = null;
			try
			{
				Object obj = serverConnector.getTask(++judgeSerialID);
				if (obj != null)
				{
					map = (HashMap<String, String>)obj;
				}
			}
			catch (Exception ex)
			{
				log.debug("Failed connect to DService", ex);
			}
			
			if (null != map)
			{
				try
				{
    				DServiceTask task = new DServiceTask(map);
    				String msg = "Accepted task: " + task.getID() + " " + task.getContest() + "-" + task.getProblem() + " " + task.getLanguage() + " " + task.getClientData(); 
    				log.info(msg);
    				JudgeTaskResult res = Judge.judgeTask(new JudgeTaskDescription(task));
   					if (serverConnector.setTaskResult(task.getID(), res.res.getJudgement().toString(), res.res.getXMLString()))
   					{
   						log.info("Report delivered" + res.res.getJudgement());
   					}
				}
				catch (Exception e)
				{
					log.error("Some error", e);
				}
			}
			else
			{
				try
				{
					sleep(1000);
				} catch (InterruptedException e)
				{
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Judge2DServiceLink().start();
	}
}
