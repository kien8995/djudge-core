package x_djudge.contestmanager;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import djudge.dservice.DServiceTaskResult;

class LocalTaskDescription
{
	String problem;
	String contest;
	String language;
	String source;
	String clientData;
}

public class DServiceClientConnector extends Thread
{
	XmlRpcClient client;
	
	Queue<LocalTaskDescription> queue = new LinkedList<LocalTaskDescription>();
	
	private String clientID = "123";
	
	public boolean addTask(LocalTaskDescription desc)
	{
		synchronized (queue)
		{
			System.out.println("Connector: pushed " + desc.clientData);
			return queue.add(desc);
		}
	}
	
	public LocalTaskDescription pollTask()
	{
		synchronized (queue)
		{
			return queue.poll();
		}
	}
	
	public LocalTaskDescription peekTask()
	{
		synchronized (queue)
		{
			return queue.peek();
		}
	}
	
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
			LocalTaskDescription task = null;
			while (null != (task = peekTask()))
			{
				Object[] params = new Object[] {
						clientID,
						task.contest,
						task.problem,
						task.language,
						task.source,
						task.clientData,
				};
				try
				{
					client.execute("DService.submitSolution", params);
					pollTask();
				}
				catch (XmlRpcException e)
				{
					e.printStackTrace();
					break;
				}
			}
			
			Object[] params2 = new Object[] {clientID};
			try
			{
				//HashMap<String, String>
				Object[] res = (Object[])client.execute("DService.fetchResults", params2);
				if (res != null)
				for (Object o : res)
				{
					HashMap<String, String> hashMap = (HashMap<String, String>) o;
					DServiceTaskResult t = new DServiceTaskResult(hashMap);
					Manager.updateSubmission(t);
				}
			}
			catch (XmlRpcException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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
	
	/*public static void main(String[] args)
	{
		new DServiceClientConnector().start();
	}*/

}
