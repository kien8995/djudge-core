package djudge.acmcontester;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.w3c.dom.Element;

import utils.XmlWorks;

import djudge.acmcontester.server.interfaces.ServerCommonInterface;
import djudge.acmcontester.server.interfaces.TeamXmlRpcInterface;
import djudge.utils.xmlrpc.XmlRpcConnector;
import djudge.utils.xmlrpc.XmlRpcStateVisualizer;

public class TeamXmlRpcConnector extends XmlRpcConnector implements TeamXmlRpcInterface, ServerCommonInterface
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TeamXmlRpcConnector.class);
	
	protected static final String serviceName = "AcmContester";
	
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
		super(serviceName, serverURL);
	}

	public TeamXmlRpcConnector(XmlRpcStateVisualizer vizi)
	{
		super(serviceName, serverURL, vizi);
	}

	@Override
	public HashMap<String, String>[] getTeamProblems(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".getTeamProblems", params);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public boolean enterContestTeam(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".enterContestTeam", params);
		return (Boolean) remoteResult;
	}

	@Override
	public String registerTeam(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".registerTeam", params);
		return (String) remoteResult;
	}

	@Override
	public HashMap<String, String>[] getTeamSubmissions(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".getTeamSubmissions", params);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public boolean submitSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		Object[] params = {username, password, problemID, languageID, sourceCode};
		return (Boolean) callRemoteMethod(serviceName + ".submitSolution", params);
	}

	@Override
	public HashMap<String, String>[] getTeamLanguages(String username, String password)
	{
		Object[] params = {username, password};
		Object res = callRemoteMethod(serviceName + ".getTeamLanguages", params);
		return objectToHashMapArray(res);
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
	public HashMap getTeamMonitor(String username, String password)
	{
		return (HashMap) callRemoteMethod(serviceName + ".getTeamMonitor", new Object[] {username, password});
	}

	@Override
	public boolean changePasswordTeam(String username, String oldPassword,
			String newPassword)
	{
		return (Boolean) callRemoteMethod(serviceName + ".changePasswordTeam", username, oldPassword, newPassword);
	}
}
