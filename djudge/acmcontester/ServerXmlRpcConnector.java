package djudge.acmcontester;


import java.util.HashMap;
import djudge.acmcontester.server.interfaces.ServerCommonInterface;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;
import djudge.acmcontester.server.interfaces.TeamXmlRpcInterface;
import djudge.acmcontester.structures.MonitorData;
import djudge.utils.XMLSettings;
import djudge.utils.xmlrpc.XmlRpcConnector;
import djudge.utils.xmlrpc.XmlRpcStateVisualizer;

@SuppressWarnings("unchecked")
public class ServerXmlRpcConnector extends XmlRpcConnector implements TeamXmlRpcInterface, ServerCommonInterface, ServerXmlRpcInterface
{
	//private static final Logger log = Logger.getLogger(TeamXmlRpcConnector.class);
	
	private static final String defaultServiceName = "AcmContester";
	
	private static final String defaultServiceUrl = "http://127.0.0.1:8202/xmlrpc"; 
	
	private static final String serviceName;
	
	private static final String serverURL;
	
	static
	{
		XMLSettings settings = new XMLSettings(ServerXmlRpcConnector.class);
		serviceName = settings.getString("service-name", defaultServiceName);
		serverURL = settings.getString("service-url", defaultServiceUrl);
	}
	
	public ServerXmlRpcConnector()
	{
		super(serviceName, serverURL);
	}

	public ServerXmlRpcConnector(XmlRpcStateVisualizer vizi)
	{
		super(serviceName, serverURL, vizi);
	}
	
	@Override
	public HashMap<String, String>[] getTeamProblems(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod("getTeamProblems", params);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public boolean enterContestTeam(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod("enterContestTeam", params);
		return (Boolean) remoteResult;
	}

	@Override
	public String registerTeam(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod("registerTeam", params);
		return (String) remoteResult;
	}

	@Override
	public HashMap<String, String>[] getTeamSubmissions(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod("getTeamSubmissions", params);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public boolean submitSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		Object[] params = {username, password, problemID, languageID, sourceCode};
		return (Boolean) callRemoteMethod("submitSolution", params);
	}

	@Override
	public HashMap<String, String>[] getTeamLanguages(String username, String password)
	{
		Object[] params = {username, password};
		Object res = callRemoteMethod("getTeamLanguages", params);
		return objectToHashMapArray(res);
	}

	@Override
	public String getVersion()
	{
		return (String) callRemoteMethod("getVersion", new Object[] {});
	}
	
	@Override
	public String echo(String what)
	{
		return (String) callRemoteMethod("echo", new Object[] {what});
	}

	@Override
	public String getContestStatus(String username, String password)
	{
		return (String) callRemoteMethod("getContestStatus", new Object[] {username, password});
	}

	@Override
	public long getContestTimeElapsed(String username, String password)
	{
		return (Long) callRemoteMethod("getContestTimeElapsed", new Object[] {username, password});
	}

	@Override
	public long getContestTimeLeft(String username, String password)
	{
		return (Long) callRemoteMethod("getContestTimeLeft", new Object[] {username, password});
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap getTeamMonitor(String username, String password)
	{
		return (HashMap) callRemoteMethod("getTeamMonitor", new Object[] {username, password});
	}

	@Override
	public boolean changePasswordTeam(String username, String oldPassword,
			String newPassword)
	{
		return (Boolean) callRemoteMethod("changePasswordTeam", username, oldPassword, newPassword);
	}
	
	@Override
	public boolean addLanguage(String username, String password, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		return (Boolean) callRemoteMethod("addLanguage", username, password, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean editLanguage(String username, String password, String id,
			String sid, String shortName, String fullName,
			String compilationComand, String djudgeID)
	{
		return (Boolean) callRemoteMethod("editLanguage", username, password, id, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean deleteLanguage(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod("deleteLanguage", username, password, id);	
	}

	@Override
	public boolean addUser(String username, String password,
			String newUserName, String newPassword, String name, String role)
	{
		return (Boolean) callRemoteMethod("addUser", username, password, newUserName, newPassword, name, role);
	}

	@Override
	public boolean deleteUser(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod("deleteUser", username, password, id);
	}

	@Override
	public HashMap<String, String>[] getUsers(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod("getUsers", params);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public boolean editUser(String username, String password, String id,
			String newUserName, String newPassword, String name, String role)
	{
		return (Boolean) callRemoteMethod("editUser", username, password, id, newUserName, newPassword, name, role);
	}

	@Override
	public boolean addProblem(String username, String password, String sid,
			String name, String djudgeProblem, String djudgeContest)
	{
		return (Boolean) callRemoteMethod("addProblem", username, password, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteProblem(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod("deleteProblem", username, password, id);
	}

	@Override
	public boolean editProblem(String username, String password, String id,
			String sid, String name, String djudgeProblem, String djudgeContest)
	{
		return (Boolean) callRemoteMethod("editProblem", username, password, id, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteSubmission(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod("deleteSubmission", username, password, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean editSubmission(String username, String password, String id,
			HashMap data)
	{
		return (Boolean) callRemoteMethod("editSubmission", username, password, id, data);
	}

	@Override
	public boolean rejudgeSubmissions(String username, String password,
			String key, String value)
	{
		return (Boolean) callRemoteMethod("rejudgeSubmissions", username, password, key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap[] getSubmissions(String username, String password)
	{
		Object remoteResult = callRemoteMethod("getSubmissions", username, password);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public HashMap[] getLanguages(String username, String password)
	{
		Object remoteResult = callRemoteMethod("getLanguages", username, password);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public HashMap[] getProblems(String username, String password)
	{
		Object remoteResult = callRemoteMethod("getProblems", username, password);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public HashMap getMonitor(String username, String password)
	{
		Object remoteResult = callRemoteMethod("getMonitor", username, password);
		return ((MonitorData) remoteResult).toHashMap();
	}

	@Override
	public boolean deleteAllLanguages(String username, String password)
	{
		return (Boolean) callRemoteMethod("deleteAllLanguages", username, password);
	}

	@Override
	public boolean changePassword(String username, String oldPassword,
			String newPassword)
	{
		return (Boolean) callRemoteMethod("changePassword", username, oldPassword, newPassword);
	}

	@Override
	public boolean deleteAllUsers(String username, String password)
	{
		return (Boolean) callRemoteMethod("deleteAllUsers", username, password);
	}

	@Override
	public boolean deleteAllProblems(String username, String password)
	{
		return (Boolean) callRemoteMethod("deleteAllProblems", username, password);
	}

	@Override
	public boolean deleteAllSubmissions(String username, String password)
	{
		return (Boolean) callRemoteMethod("deleteAllSubmissions", username, password);
	}

	@Override
	public boolean deleteAllData(String username, String password)
	{
		return (Boolean) callRemoteMethod("deleteAllData", username, password);
	}

	@Override
	public boolean setContestFreezeTime(String username, String password,
			long tillTimeLeft)
	{
		return (Boolean) callRemoteMethod("setContestFreezeTime", username, password, tillTimeLeft);
	}

	@Override
	public boolean setContestRunning(String username, String password,
			boolean isRunning)
	{
		return (Boolean) callRemoteMethod("setContestRunning", username, password, isRunning);
	}

	@Override
	public boolean setContestTimeLeft(String username, String password,
			long timeLeft)
	{
		return (Boolean) callRemoteMethod("setContestTimeLeft", username, password, timeLeft);
	}

	@Override
	public boolean setContestTimePast(String username, String password,
			long timePast)
	{
		return (Boolean) callRemoteMethod("setContestTimePast", username, password, timePast);
	}

	@Override
	public long getContestFreezeTime(String username, String password)
	{
		return (Long) callRemoteMethod("getContestFreezeTime", username, password);
	}

	@Override
	public boolean activateSubmission(String username, String password,
			String id, int active)
	{
		return (Boolean) callRemoteMethod("activateSubmission", username, password, id, active);
	}

	@Override
	public boolean generateLogins(String username, String password, int count,
			String loginType)
	{
		return (Boolean) callRemoteMethod("generateLogins", username, password, count, loginType);
	}
}
