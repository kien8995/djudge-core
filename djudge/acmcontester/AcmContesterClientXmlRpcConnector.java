package djudge.acmcontester;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.w3c.dom.Element;

import utils.XmlWorks;

import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;
import djudge.common.HashMapSerializer;

public class AcmContesterClientXmlRpcConnector extends HashMapSerializer implements AcmContesterXmlRpcClientInterface
{
	public final static String serviceName = "AcmContester";
	
	XmlRpcClient client;
	
	static final String serverURL; 
	
	static
	{
		Element elem = XmlWorks.getDocument("team.xml").getDocumentElement();
		String url = elem.getAttribute("server-url");
		serverURL = url != null && url.length() > 0 ? url : "http://127.0.0.1:8202/xmlrpc";
	}
	
	public AcmContesterClientXmlRpcConnector()
	{
		try
		{
    		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    		config.setServerURL(new URL(serverURL));
    		config.setEnabledForExtensions(true);
    		config.setConnectionTimeout(5 * 1000);
    		config.setReplyTimeout(5 * 1000);
    
    		client = new XmlRpcClient();
    
    		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
    		
    		client.setConfig(config);
    		
    		System.out.println("AcmContesterClientXmlRpcConnector is up");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private Object callRemoteMethod(String methodName, Object[] params)
	{
		Object result = null;
		try
		{
			System.out.println("XML-RPC. Calling " + methodName + " with params " + Arrays.toString(params));
			result = client.execute(methodName, params);
			System.out.println("Result: " + result.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public HashMap<String, String>[] getProblems(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".getProblems", params);
		return deserializeToHashMapArray(remoteResult);
	}

	@Override
	public boolean enterContest(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".enterContest", params);
		return (Boolean) remoteResult;
	}

	@Override
	public String registerUser(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".registerUser", params);
		return (String) remoteResult;
	}

	@Override
	public HashMap<String, String>[] getAllSubmissions(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".getAllSubmissions", params);
		return deserializeToHashMapArray(remoteResult);
	}

	@Override
	public HashMap<String, String>[] getOwnSubmissions(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".getOwnSubmissions", params);
		return deserializeToHashMapArray(remoteResult);
	}

	@Override
	public boolean submitSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		Object[] params = {username, password, problemID, languageID, sourceCode};
		return (Boolean) callRemoteMethod("AcmContester.submitSolution", params);
	}

	@Override
	public HashMap<String, String>[] getLanguages(String username, String password)
	{
		Object[] params = {username, password};
		Object res = callRemoteMethod("AcmContester.getLanguages", params);
		return deserializeToHashMapArray(res);
	}

	@Override
	public String getVersion()
	{
		return (String) callRemoteMethod("AcmContester.getVersion", new Object[] {});
	}
	
	
	public static void main(String[] args)
	{
		new Client();
	}

	@Override
	public String echo(String what)
	{
		return (String) callRemoteMethod("AcmContester.echo", new Object[] {what});
	}

	@Override
	public String getContestStatus(String username, String password)
	{
		return (String) callRemoteMethod("AcmContester.getContestStatus", new Object[] {username, password});
	}

	@Override
	public long getContestTimeElapsed(String username, String password)
	{
		return (Long) callRemoteMethod("AcmContester.getContestTimeElapsed", new Object[] {username, password});
	}

	@Override
	public long getContestTimeLeft(String username, String password)
	{
		return (Long) callRemoteMethod("AcmContester.getContestTimeLeft", new Object[] {username, password});
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap getMonitor(String username, String password)
	{
		return (HashMap) callRemoteMethod("AcmContester.getMonitor", new Object[] {username, password});
	}

}
