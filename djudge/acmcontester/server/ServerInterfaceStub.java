package djudge.acmcontester.server;

import java.util.HashMap;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.acmcontester.interfaces.TeamXmlRpcInterface;
import djudge.acmcontester.structures.MonitorData;
import djudge.common.HashMapSerializer;

public class ServerInterfaceStub extends HashMapSerializer implements ServerXmlRpcInterface
{
	@Override
	public HashMap<String, String>[] getProblems(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestServer.getCore().getProblems(userInfo));
	}

	@Override
	public boolean enterContest(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return ContestServer.getCore().enterContest(userInfo);
	}

	@Override
	public String registerUser(String username, String password)
	{
		return ContestServer.getCore().registerUser(username, password);
	}

	@Override
	public HashMap<String, String>[] getAllSubmissions(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestServer.getCore().getAllSubmissions(userInfo));
	}

	@Override
	public HashMap<String, String>[] getOwnSubmissions(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestServer.getCore().getOwnSubmissions(userInfo));
	}

	@Override
	public boolean submitSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return ContestServer.getCore().submitSolution(userInfo, problemID, languageID, sourceCode);
	}

	@Override
	public HashMap<String, String>[] getLanguages(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestServer.getCore().getLanguages(userInfo));
	}
	
	@Override
	public String getVersion()
	{
		return ContestServer.getCore().getVersion();
	}

	@Override
	public String echo(String what)
	{
		return what;
	}

	@Override
	public String getContestStatus(String username, String password)
	{
		return ContestServer.getCore().getContestStatus(username, password).toString();
	}

	@Override
	public long getContestTimeElapsed(String username, String password)
	{
		return ContestServer.getCore().getContestTimeElapsed(username, password);
	}

	@Override
	public long getContestTimeLeft(String username, String password)
	{
		return ContestServer.getCore().getContestTimeLeft(username, password);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap getMonitor(String username, String password)
	{
		MonitorData md = ContestServer.getCore().getMonitor(username, password);
		return md.toHashMap();
	}

	@Override
	public boolean addLanguage(String username, String password, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		return ContestServer.getCore().addLanguage(username, password, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean editLanguage(String username, String password, String id, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		return ContestServer.getCore().editLanguage(username, password, id, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean deleteLanguage(String username, String password, String id)
	{
		return ContestServer.getCore().deleteLanguage(username, password, id);
	}
}
