package djudge.acmcontester.server;


import djudge.acmcontester.admin.AdminXmlRpcConnector;
import djudge.acmcontester.interfaces.ServerNativeInterface;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserData;
import djudge.utils.xmlrpc.HashMapSerializer;

public class ServerNativeViaXmlRpcInterfaceStub implements ServerNativeInterface
{
	ServerXmlRpcInterface serverConnector = new AdminXmlRpcConnector();
	
	@Override
	public boolean enterContestTeam(String username, String password)
	{
		return serverConnector.enterContestTeam(username, password);
	}

	@Override
	public String registerTeam(String username, String password)
	{
		return serverConnector.registerTeam(username, password);
	}

	@Override
	public boolean submitSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		return serverConnector.submitSolution(username, password, problemID, languageID, sourceCode);
	}

	@Override
	public String getVersion()
	{
		return serverConnector.getVersion();
	}

	@Override
	public String echo(String what)
	{
		return serverConnector.echo(what);
	}

	@Override
	public String getContestStatus(String username, String password)
	{
		return serverConnector.getContestStatus(username, password).toString();
	}

	@Override
	public long getContestTimeElapsed(String username, String password)
	{
		return serverConnector.getContestTimeElapsed(username, password);
	}

	@Override
	public long getContestTimeLeft(String username, String password)
	{
		return serverConnector.getContestTimeLeft(username, password);
	}

	@Override
	public MonitorData getTeamMonitor(String username, String password)
	{
		return new MonitorData(serverConnector.getTeamMonitor(username, password));
	}

	@Override
	public boolean addLanguage(String username, String password, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		return serverConnector.addLanguage(username, password, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean editLanguage(String username, String password, String id, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		return serverConnector.editLanguage(username, password, id, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean deleteLanguage(String username, String password, String id)
	{
		return serverConnector.deleteLanguage(username, password, id);
	}

	@Override
	public boolean deleteUser(String username, String password, String id)
	{
		return serverConnector.deleteUser(username, password, id);
	}

	@Override
	public boolean addUser(String username, String password,
			String newUserName, String newPassword, String name, String role)
	{
		return serverConnector.addUser(username, password, newUserName, newPassword, name, role);
	}

	@Override
	public UserData[] getUsers(String username, String password)
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				serverConnector.getUsers(username, password), UserData.class)
				.toArray(new UserData[0]);
	}
	
	@Override
	public boolean editUser(String username, String password, String id,
			String newUserName, String newPassword, String name, String role)
	{
		return serverConnector.editUser(username, password, id, newUserName, newPassword, name, role);
	}

	@Override
	public boolean addProblem(String username, String password, String sid,
			String name, String djudgeProblem, String djudgeContest)
	{
		return serverConnector.addProblem(username, password, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteProblem(String username, String password, String id)
	{
		return serverConnector.deleteProblem(username, password, id);
	}

	@Override
	public boolean editProblem(String username, String password, String id,
			String sid, String name, String djudgeProblem, String djudgeContest)
	{
		return serverConnector.editProblem(username, password, id, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteSubmission(String username, String password, String id)
	{
		return serverConnector.deleteSubmission(username, password, id);
	}

	@Override
	public SubmissionData[] getTeamSubmissions(String username, String password)
	{
		return HashMapSerializer.deserializeFromHashMapArray(serverConnector.getTeamSubmissions(username, password), SubmissionData.class).toArray(new SubmissionData[0]);
	}

	@Override
	public boolean editSubmission(String username, String password, String id,
			SubmissionData data)
	{
		return serverConnector.editSubmission(username, password, id, data.toHashMap());
	}

	@Override
	public boolean rejudgeSubmissions(String username, String password,
			String key, String value)
	{
		return serverConnector.rejudgeSubmissions(username, password, key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SubmissionData[] getSubmissions(String username, String password)
	{
		return HashMapSerializer.deserializeFromHashMapArray(serverConnector.getSubmissions(username, password), SubmissionData.class).toArray(new SubmissionData[0]);
	}

	@Override
	public ProblemData[] getTeamProblems(String username, String password)
	{
		return HashMapSerializer.deserializeFromHashMapArray(serverConnector.getTeamProblems(username, password), ProblemData.class).toArray(new ProblemData[0]);
	}

	@Override
	public LanguageData[] getTeamLanguages(String username, String password)
	{
		return HashMapSerializer.deserializeFromHashMapArray(serverConnector.getTeamLanguages(username, password), LanguageData.class).toArray(new LanguageData[0]);
	}

	@Override
	public LanguageData[] getLanguages(String username, String password)
	{
		return HashMapSerializer.deserializeFromHashMapArray(serverConnector.getLanguages(username, password), LanguageData.class).toArray(new LanguageData[0]);
	}

	@Override
	public ProblemData[] getProblems(String username, String password)
	{
		return HashMapSerializer.deserializeFromHashMapArray(serverConnector.getProblems(username, password), ProblemData.class).toArray(new ProblemData[0]);
	}

	@Override
	public MonitorData getMonitor(String username, String password)
	{
		return new MonitorData(serverConnector.getMonitor(username, password));
	}
}
