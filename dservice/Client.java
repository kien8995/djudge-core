package dservice;


import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class Client
{
	public static void main(String[] args) throws Exception
	{
		// create configuration
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		// FIXME: hardcoding
		config.setServerURL(new URL("http://127.0.0.1:8001/xmlrpc"));
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		XmlRpcClient client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);

		// make the a regular call
		Object[] params2 = new Object[] { "ferf", "erferf" };
		String res = (String)client.execute("DService.createUser", params2);
		System.out.println(res);
		params2 = new Object[] { "123", "contestId", "problemId", "languageId", "source" };
		res = "" + (Integer)client.execute("DService.submitSolution", params2);
		System.out.println(res+"lklk");
	}
}