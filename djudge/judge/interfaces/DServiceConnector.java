package djudge.judge.interfaces;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import sun.util.logging.resources.logging;

import djudge.acmcontester.server.XMLSettings;
import djudge.dservice.DServiceTask;
import djudge.judge.Judge;
import djudge.judge.JudgeTaskDescription;
import djudge.judge.JudgeTaskResult;
import djudge.judge.RPCClientFactory;


public class DServiceConnector extends Thread
{
	private static final Logger log = Logger.getLogger(DServiceConnector.class);
	
	private static int judgeSerialID = 0;
	
	private XmlRpcClient client;
	
	private final String serviceUrl;
	
	private final String serviceName;
	
	private final int connectionTimeout;
	
	private final int replyTimeout;
	
	
	public DServiceConnector()
	{
		XMLSettings settings = new XMLSettings("judge.xml");
		serviceUrl = settings.getProperty("dservice-url");
		serviceName = settings.getProperty("dservice-name");
		connectionTimeout = settings.getInt("connnection-timeout", 5000);
		replyTimeout = settings.getInt("reply-timeout", 5000);
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		client = RPCClientFactory.getRPCClient(serviceUrl, connectionTimeout, replyTimeout);
		
		log.info("DServiceConnector started on service " + serviceName + " @ " + serviceUrl + "; timeouts: " + connectionTimeout + ", " + replyTimeout);
		
		while (true)
		{
			Object[] params = new Object[] { new Integer(++judgeSerialID) };
			HashMap<String, String> map = null;
			try
			{
				Object obj = client.execute(serviceName + ".getTask", params);
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
    				Object[] params2 = new Object[] {
    						task.getID(),
    						res.res.getJudgement().toString(),
    						res.res.getXMLString()
    					};
    				try
    				{
    					client.execute(serviceName + ".setTaskResult", params2);
    					log.info("Report delivered" + res.res.getJudgement());
    				}
    				catch (XmlRpcException e)
    				{
    					log.warn("Report delivery failed", e);
    				}
				}
				catch (Exception e)
				{
					log.error("Some error in judge", e);
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
		new DServiceConnector().start();
	}

}
