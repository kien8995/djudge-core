package djudge.acmcontester.server;


import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import sun.util.logging.resources.logging;
import db.DBRowAbstract;
import db.LanguagesDataModel;
import db.MonitorModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import db.UsersDataModel;
import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.interfaces.AdminUsersNativeInterface;
import djudge.acmcontester.interfaces.AdminUsersXmlRpcInterface;
import djudge.acmcontester.structures.AbstractRemoteTable;
import djudge.acmcontester.structures.ContestStatusEnum;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserData;

public class ContestCore implements AdminUsersNativeInterface
{
	private static final Logger log = Logger.getLogger(ContestCore.class);
	
	private UsersDataModel usersModel;
	
	private ProblemsDataModel problemsModel;
	
	private LanguagesDataModel languagesModel;
	
	private SubmissionsDataModel submissionsModel;
	
	private MonitorModel monitorModel;
	
	private ContestSettings contest = new ContestSettings("contest.xml");
	
	private ContestState state = new ContestState(contest);
	
	private final DServiceConnector djudgeInterface;
	
	{
		usersModel = new UsersDataModel();
		usersModel.updateData();
		
		problemsModel = new ProblemsDataModel();
		problemsModel.updateData();
		
		languagesModel = new LanguagesDataModel();
		languagesModel.updateData();
		
		submissionsModel = new SubmissionsDataModel();
		submissionsModel.updateData();
		
		djudgeInterface = new DServiceConnector();
		djudgeInterface.start();
		
		monitorModel = new MonitorModel();
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

	public boolean deleteLanguage(String username, String password, String id)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		DBRowAbstract rd = languagesModel.getRowByID(id);
		if (rd == null)
			return false;
		rd.delete(languagesModel);
		languagesModel.updateData();
		return true;
	}
	
	public boolean deleteUser(String username, String password, String id)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		log.debug("deleteUser - auth OK");
		DBRowAbstract rd = usersModel.getRowByID(id);
		if (rd == null)
			return false;
		rd.delete(usersModel);
		usersModel.updateData();
		return true;
	}
	
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

	public ProblemData[] getProblems(AuthentificationData userInfo)
	{
		return problemsModel.getRows().toArray(new ProblemData[0]);
	}

	public boolean enterContest(AuthentificationData userInfo)
	{
		String userID = usersModel.getUserID(userInfo.username, userInfo.password);
		return Integer.parseInt(userID) > 0;
	}

	public String registerUser(String username, String password)
	{
		return "OK";
	}

	public SubmissionData[] getAllSubmissions(AuthentificationData userInfo)
	{
		return submissionsModel.getRows().toArray(new SubmissionData[0]);
	}

	public SubmissionData[] getOwnSubmissions(AuthentificationData userInfo)
	{
		//FIXME: != getAllSubmissions
		return submissionsModel.getRows().toArray(new SubmissionData[0]);
	}

	public boolean submitSolution(AuthentificationData userInfo,
			String problemID, String languageID, String sourceCode)
	{
		return submitSolution(userInfo.username, userInfo.password, problemID, languageID, sourceCode);
	}

	public LanguageData[] getLanguages(AuthentificationData userInfo)
	{
		return languagesModel.getRows().toArray(new LanguageData[0]);
	}

	public String getVersion()
	{
		return "v 0.1";
	}

	public ContestStatusEnum getContestStatus(String username, String password)
	{
		return state.getContestStatus();
	}

	public long getContestTimeElapsed(String username, String password)
	{
		return state.getContestTime();
	}

	public long getContestTimeLeft(String username, String password)
	{
		return state.getContestTimeLeft();
	}
	
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
		return true;
	}

	@Override
	public UserData[] getUsers(String username, String password)
	{
		//if (!usersModel.isAdmin(username, password))
			//return new UserData[0];
		return usersModel.getRows().toArray(new UserData[0]);
	}
}
