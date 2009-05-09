package interfaces;

import java.net.URL;
import java.util.HashMap;

import judge.Judge;
import judge.JudgeTaskDescription;
import judge.JudgeTaskResult;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import dservice.DServiceTask;

public class DServiceConnector extends Thread
{
	XmlRpcClient client;
	
	int judgeID = 2;
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		try
		{
    		// create configuration
    		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    		// FIXME: hardcoding
    		config.setServerURL(new URL("http://127.0.0.1:8001/xmlrpc"));
    		config.setEnabledForExtensions(true);
    		config.setConnectionTimeout(5 * 1000);
    		config.setReplyTimeout(5 * 1000);
    
    		client = new XmlRpcClient();
    
    		// use Commons HttpClient as transport
    		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
    		// set configuration
    		client.setConfig(config);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		while (true)
		{
			Object[] params = new Object[] { new Integer(judgeID) };
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
			
			if (map != null)
			{
				System.out.println("Got + " + map.toString());
				DServiceTask task = new DServiceTask(map);
				JudgeTaskResult res = Judge.judgeTask(new JudgeTaskDescription(task));
				Object[] params2 = new Object[] { task.getID(), "" + res.res.getJudgement(),  res.res.getXMLString()};
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
