package djudge.acmcontester.server;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import db.AbstractTableDataModel;
import db.DBRowAbstract;
import db.LanguagesDataModel;
import db.MonitorModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import db.UsersDataModel;
import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.interfaces.AdminNativeInterface;
import djudge.acmcontester.interfaces.ServerCommonInterface;
import djudge.acmcontester.interfaces.TeamNativeInterface;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserData;

public class ContestCore implements AdminNativeInterface, TeamNativeInterface, ServerCommonInterface
{
	private static final Logger log = Logger.getLogger(ContestCore.class);
	
	private UsersDataModel usersModel;
	
	private ProblemsDataModel problemsModel;
	
	private LanguagesDataModel languagesModel;
	
	private SubmissionsDataModel submissionsModel;
	
	private MonitorModel monitorModel;
	
	private ContestSettings contest = new ContestSettings("contest.xml");
	
	private ContestState state = new ContestState(contest);
	
	private DServiceConnector djudgeInterface;
	
	public ContestCore()
	{
		initCore(false);
	}
	
	public ContestCore(boolean startServices)
	{
		initCore(startServices);
	}
	
	private void initCore(boolean flagStandalone)
	{
		usersModel = new UsersDataModel();
		usersModel.updateData();
		
		problemsModel = new ProblemsDataModel();
		problemsModel.updateData();
		
		languagesModel = new LanguagesDataModel();
		languagesModel.updateData();
		
		submissionsModel = new SubmissionsDataModel();
		submissionsModel.updateData();
		
		if (flagStandalone)
		{
			djudgeInterface = new DServiceConnector();
			djudgeInterface.start();
		}
		
		monitorModel = new MonitorModel();
	}
	
	@SuppressWarnings("deprecation")
	public void stopCore()
	{
		if (djudgeInterface != null)
		{
			djudgeInterface.stop();
		}
	}

	public UsersDataModel getUsersModel()
	{
		return usersModel;
	}
	
	public LanguagesDataModel getLanguagesModel()
	{
		return languagesModel;
	}
	
	public ProblemsDataModel getProblemsModel()
	{
		return problemsModel;
	}
	
	public SubmissionsDataModel getSubmissionsDataModel()
	{
		return submissionsModel;
	}
	
	@Override
	public boolean addLanguage(String username, String password, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		LanguageData ld = new LanguageData(sid, shortName, fullName, compilationComand, djudgeID);
		DBRowAbstract rd = languagesModel.toRow(ld);
		rd.appendTo(languagesModel);
		return true;
	}
	
	@Override
	public boolean editLanguage(String username, String password, String id, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		DBRowAbstract rd = languagesModel.getRowByID(id);
		if (rd == null)
			return false;
		LanguageData ld = new LanguageData(id, sid, shortName, fullName, compilationComand, djudgeID);
		rd = languagesModel.toRow(ld);
		rd.save();
		languagesModel.updateData();
		return true;
	}
	
	@Override
	public boolean editUser(String username, String password, String id,
			String newUserName, String newPassword, String name, String role)
	{
		log.debug("User editing request");
		if (!usersModel.isAdmin(username, password))
			return false;
		
		DBRowAbstract rd = usersModel.getRowByID(id);
		if (rd == null)
			return false;
		UserData ud = new UserData(id, newUserName, newPassword, name, role);
		rd = usersModel.toRow(ud);
		rd.save();
		usersModel.updateData();
		log.debug("User editing finished");
		return true;
	}
	
	@Override
	public boolean deleteLanguage(String username, String password, String id)
	{
		return deleteAbstract(languagesModel, username, password, id);
	}
	
	@Override
	public boolean deleteUser(String username, String password, String id)
	{
		return deleteAbstract(usersModel, username, password, id);
	}
	
	@Override
	public boolean submitSolution(String username, String password, String problemID, String languageID, String courceCode)
	{
		String userID = usersModel.getUserID(username, password);
		if (!state.isRunnning() || Integer.parseInt(userID) <= 0 || !problemsModel.isValidID(problemID) || !languagesModel.isValidID(languageID))
		{
			return false;
		}
		SubmissionData sd = new SubmissionData();
		sd.contestTime = (int) state.getContestTime();
		sd.languageID = languageID;
		sd.problemID = problemID;
		sd.sourceCode = new String(Base64.encodeBase64(courceCode.getBytes()));
		sd.userID = userID;
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = new Date();
//		String dateStr = dateFormat.format(date);
		
		DBRowAbstract row = submissionsModel.toRow(sd);
		row.appendTo(submissionsModel);
		return true;
	}

	@Override
	public ProblemData[] getProblems(String username, String password)
	{
		problemsModel.updateData();
		return problemsModel.getRows().toArray(new ProblemData[0]);
	}

	public boolean enterContest(String username, String password)
	{
		String userID = usersModel.getUserID(username, password);
		return Integer.parseInt(userID) > 0;
	}

	@Override
	public LanguageData[] getLanguages(String username, String passord)
	{
		return languagesModel.getRows().toArray(new LanguageData[0]);
	}

	@Override
	public String getVersion()
	{
		return "v 0.1";
	}

	@Override
	public String getContestStatus(String username, String password)
	{
		return state.getContestStatus().toString();
	}

	@Override
	public long getContestTimeElapsed(String username, String password)
	{
		return state.getContestTime();
	}

	@Override
	public long getContestTimeLeft(String username, String password)
	{
		return state.getContestTimeLeft();
	}
	
	@Override
	public MonitorData getMonitor(String username, String password)
	{
		return monitorModel.getMonitor(getContestTimeElapsed(username, password));
	}
	
	public void stopContest()
	{
		state.stopContest();
	}
	
	public void startContest(long timeLeft)
	{
		state.startContest(timeLeft);
	}

	@Override
	public boolean addUser(String username, String password,
			String newUserName, String newPassword, String name, String role)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		UserData ud = new UserData(newUserName, newPassword, name, role);
		DBRowAbstract rd = usersModel.toRow(ud);
		rd.appendTo(usersModel);
		usersModel.updateData();
		return true;
	}

	@Override
	public UserData[] getUsers(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
			return new UserData[0];
		usersModel.updateData();
		return usersModel.getRows().toArray(new UserData[0]);
	}

	@Override
	public boolean addProblem(String username, String password, String sid,
			String name, String djudgeProblem, String djudgeContest)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		ProblemData pd = new ProblemData(sid, name, djudgeProblem, djudgeContest);
		DBRowAbstract rd = problemsModel.toRow(pd);
		rd.appendTo(problemsModel);
		problemsModel.updateData();
		return true;
	}

	public boolean deleteAbstract(AbstractTableDataModel model, String username, String password, String id)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		log.debug("delete from table " + model.tableName + " - auth OK");
		DBRowAbstract rd = model.getRowByID(id);
		if (rd == null)
			return false;
		rd.delete(model);
		model.updateData();
		log.debug("delete from table " + model.tableName + " - finished");
		return true;
	}
	
	@Override
	public boolean deleteProblem(String username, String password, String id)
	{
		return deleteAbstract(problemsModel, username, password, id);
	}

	@Override
	public boolean editProblem(String username, String password, String id,
			String sid, String name, String djudgeProblem, String djudgeContest)
	{
		
		log.debug("Problem editing request");
		if (!usersModel.isAdmin(username, password))
			return false;
		
		DBRowAbstract rd = problemsModel.getRowByID(id);
		if (rd == null)
			return false;
		ProblemData pd = new ProblemData(id, sid, name, djudgeProblem, djudgeContest);
		rd = problemsModel.toRow(pd);
		rd.save();
		problemsModel.updateData();
		log.debug("Problem editing finished");
		return true;
	}

	@Override
	public boolean deleteSubmission(String username, String password, String id)
	{
		return deleteAbstract(submissionsModel, username, password, id);
	}

	@Override
	public SubmissionData[] getSubmissions(String username, String password)
	{
		submissionsModel.updateData();
		return submissionsModel.getRows().toArray(new SubmissionData[0]);
	}

	@Override
	public boolean editSubmission(String username, String password, String id,
			SubmissionData data)
	{
		log.debug("editSubmission request");
		if (!usersModel.isAdmin(username, password))
			return false;
		
		DBRowAbstract rd = submissionsModel.getRowByID(id);
		if (rd == null)
			return false;
		
		rd = submissionsModel.toRow(data);
		rd.save();
		submissionsModel.updateData();
		log.debug("editSubmission finished");
		return true;		
	}

	@Override
	public boolean rejudgeSubmissions(String username, String password,
			String key, String value)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		submissionsModel.rejudgeBy(key, value);
		return true;
	}

	@Override
	public ProblemData[] getTeamProblems(String username, String password)
	{
		// TODO: fixme
		return getProblems(username, password);
	}

	@Override
	public boolean enterContestTeam(String username, String password)
	{
		// TODO: fixme
		return true;
	}

	@Override
	public SubmissionData[] getTeamSubmissions(String username, String password)
	{
		// TODO: fixme
		return getSubmissions(username, password);
	}

	@Override
	public LanguageData[] getTeamLanguages(String username, String password)
	{
		// TODO: fixme
		return getLanguages(username, password);
	}

	@Override
	public String echo(String what)
	{
		return what;
	}

	@Override
	public MonitorData getTeamMonitor(String username, String password)
	{
		//TODO: fixme
		return getMonitor(username, password);
	}

	@Override
	public String registerTeam(String username, String password)
	{
		// TODO: fixme
		return "OK";
	}
}
