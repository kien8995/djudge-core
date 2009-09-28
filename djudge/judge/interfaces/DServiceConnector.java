package djudge.judge.interfaces;

import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import djudge.dservice.DServiceTask;
import djudge.judge.Judge;
import djudge.judge.JudgeTaskDescription;
import djudge.judge.JudgeTaskResult;
import djudge.judge.RPCClientFactory;


public class DServiceConnector extends Thread
{
	static int judgeSerialID = 0;
	
	XmlRpcClient client;
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		client = RPCClientFactory.getRPCClient("http://127.0.0.1:8001/xmlrpc", 5000, 5000);
		
		while (true)
		{
			Object[] params = new Object[] { new Integer(++judgeSerialID) };
			HashMap<String, String> map = null;
			try
			{
				Object obj = client.execute("DService.getTask", params);
				if (obj != null)
				{
					map = (HashMap<String, String>)obj;
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			if (null != map)
			{
				DServiceTask task = new DServiceTask(map);
				System.out.println("Got: + " + task.getClientData() + " " + task.getID() + " " + task.getContest() + task.getProblem());
				JudgeTaskResult res = Judge.judgeTask(new JudgeTaskDescription(task));
				Object[] params2 = new Object[] {
						task.getID(),
						res.res.getJudgement().toString(),
						res.res.getXMLString()
					};
				try
				{
					client.execute("DService.setTaskResult", params2);
				}
				catch (XmlRpcException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		new DServiceConnector().start();
	}

}
