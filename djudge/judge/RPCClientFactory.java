package djudge.judge;

import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class RPCClientFactory
{
	final static int defaultConnectionTimeout = 5000;
	
	final static int defaultReplyTimeout = 5000; 
	
	XmlRpcClient client;
	
	public static XmlRpcClient getRPCClient(String serverURL, int connectionTimeout, int replyTimeout)
	{
		XmlRpcClient client = null;
		try
		{
    		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    		
    		config.setServerURL(new URL(serverURL));
    		config.setEnabledForExtensions(true);
    		config.setConnectionTimeout(connectionTimeout);
    		config.setReplyTimeout(replyTimeout);
    		
    		client = new XmlRpcClient();
    		
    		// use Commons HttpClient as transport
    		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
    		
    		// set configuration
    		client.setConfig(config);
		}
		catch (Exception ex)
		{
			System.out.println("Exception in RPCClientFactory.getRPCClient:");
			ex.printStackTrace();
		}
		return client;
	}

	public static XmlRpcClient getRPCClient(String serverURL)
	{
		return getRPCClient(serverURL, defaultConnectionTimeout, defaultReplyTimeout);
	}
}
