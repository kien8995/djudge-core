package djudge.acmcontester.server;


import java.util.HashMap;
import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.interfaces.ServerNativeInterface;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserData;
import djudge.utils.xmlrpc.HashMapSerializer;

public class ServerNativeInterfaceStub implements ServerNativeInterface
{
	@Override
	public boolean enterContestTeam(String username, String password)
	{
		return ContestServer.getCore().enterContest(username, password);
	}

	@Override
	public String registerTeam(String username, String password)
	{
		return ContestServer.getCore().registerTeam(username, password);
	}

	@Override
	public boolean submitSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		return ContestServer.getCore().submitSolution(username, password, problemID, languageID, sourceCode);
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

	@Override
	public MonitorData getTeamMonitor(String username, String password)
	{
		return ContestServer.getCore().getTeamMonitor(username, password);
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

	@Override
	public boolean deleteUser(String username, String password, String id)
	{
		return ContestServer.getCore().deleteUser(username, password, id);
	}

	@Override
	public boolean addUser(String username, String password,
			String newUserName, String newPassword, String name, String role)
	{
		return ContestServer.getCore().addUser(username, password, newUserName, newPassword, name, role);
	}

	@Override
	public UserData[] getUsers(String username, String password)
	{
		return ContestServer.getCore().getUsers(username, password);
	}
	
	@Override
	public boolean editUser(String username, String password, String id,
			String newUserName, String newPassword, String name, String role)
	{
		return ContestServer.getCore().editUser(username, password, id, newUserName, newPassword, name, role);
	}

	@Override
	public boolean addProblem(String username, String password, String sid,
			String name, String djudgeProblem, String djudgeContest)
	{
		return ContestServer.getCore().addProblem(username, password, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteProblem(String username, String password, String id)
	{
		return ContestServer.getCore().deleteProblem(username, password, id);
	}

	@Override
	public boolean editProblem(String username, String password, String id,
			String sid, String name, String djudgeProblem, String djudgeContest)
	{
		return ContestServer.getCore().editProblem(username, password, id, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteSubmission(String username, String password, String id)
	{
		return ContestServer.getCore().deleteSubmission(username, password, id);
	}

	@Override
	public SubmissionData[] getTeamSubmissions(String username, String password)
	{
		return ContestServer.getCore().getTeamSubmissions(username, password);
	}

	@Override
	public boolean editSubmission(String username, String password, String id,
			SubmissionData data)
	{
		return ContestServer.getCore().editSubmission(username, password, id, data);
	}

	@Override
	public boolean rejudgeSubmissions(String username, String password,
			String key, String value)
	{
		return ContestServer.getCore().rejudgeSubmissions(username, password, key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SubmissionData[] getSubmissions(String username, String password)
	{
		return ContestServer.getCore().getSubmissions(username, password);
	}

	@Override
	public ProblemData[] getTeamProblems(String username, String password)
	{
		return ContestServer.getCore().getTeamProblems(username, password);
	}

	@Override
	public LanguageData[] getTeamLanguages(String username, String password)
	{
		return ContestServer.getCore().getTeamLanguages(username, password);
	}
}
