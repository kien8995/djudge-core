package djudge.acmcontester;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.w3c.dom.Element;

import utils.XmlWorks;

import djudge.acmcontester.interfaces.TeamXmlRpcInterface;
import djudge.common.HashMapSerializer;
import djudge.judge.RPCClientFactory;

public class TeamXmlRpcConnector extends HashMapSerializer implements TeamXmlRpcInterface
{
	protected final static String serviceName = "AcmContester";
	
	XmlRpcClient client;
	
	static final String serverURL; 
	
	static
	{
		Element elem = XmlWorks.getDocument("team.xml").getDocumentElement();
		String url = elem.getAttribute("server-url");
		serverURL = url != null && url.length() > 0 ? url : "http://127.0.0.1:8202/xmlrpc";
	}
	
	public TeamXmlRpcConnector()
	{
		try
		{
			client = RPCClientFactory.getRPCClient(serverURL);
    		System.out.println("AcmContesterClientXmlRpcConnector is up");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	protected Object callRemoteMethod(String methodName, Object... params)
	{
		Object[] paramsArray = new Object[params.length];
		for (int i = 0; i < paramsArray.length; i++)
			paramsArray[i] = params[i];
		Object result = null;
		try
		{
			System.out.println("XML-RPC. Calling " + methodName + " with params " + Arrays.toString(paramsArray));
			result = client.execute(methodName, paramsArray);
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
		return (Boolean) callRemoteMethod(serviceName + ".submitSolution", params);
	}

	@Override
	public HashMap<String, String>[] getLanguages(String username, String password)
	{
		Object[] params = {username, password};
		Object res = callRemoteMethod(serviceName + ".getLanguages", params);
		return deserializeToHashMapArray(res);
	}

	@Override
	public String getVersion()
	{
		return (String) callRemoteMethod(serviceName + ".getVersion", new Object[] {});
	}
	
	@Override
	public String echo(String what)
	{
		return (String) callRemoteMethod(serviceName + ".echo", new Object[] {what});
	}

	@Override
	public String getContestStatus(String username, String password)
	{
		return (String) callRemoteMethod(serviceName + ".getContestStatus", new Object[] {username, password});
	}

	@Override
	public long getContestTimeElapsed(String username, String password)
	{
		return (Long) callRemoteMethod(serviceName + ".getContestTimeElapsed", new Object[] {username, password});
	}

	@Override
	public long getContestTimeLeft(String username, String password)
	{
		return (Long) callRemoteMethod(serviceName + ".getContestTimeLeft", new Object[] {username, password});
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap getMonitor(String username, String password)
	{
		return (HashMap) callRemoteMethod(serviceName + ".getMonitor", new Object[] {username, password});
	}

}
