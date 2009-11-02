package djudge.acmcontester.server;


import org.apache.log4j.Logger;

import db.DBRowAbstract;
import db.SubmissionsDataModel;
import djudge.acmcontester.server.interfaces.AdminNativeInterface;
import djudge.acmcontester.server.interfaces.ServerCommonInterface;
import djudge.acmcontester.server.interfaces.TeamNativeInterface;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserData;
import djudge.utils.CachedObject;
import djudge.utils.CachedObjectsSet;

public class ContestCore extends ContestCoreInternals implements AdminNativeInterface, TeamNativeInterface, ServerCommonInterface
{
	private static final Logger log = Logger.getLogger(ContestCore.class);
	
	/* TeamMonitor cache */
	private CachedObject<MonitorData> cachedTeamMonitor = new CachedObject<MonitorData>(60000){
		@Override
		protected MonitorData updateData() throws Exception
		{
			return getMonitorInternal(true);
		}
	};
	
	/* TeamLanguages cache */
	private CachedObject<LanguageData[]> cachedTeamLanguages = new CachedObject<LanguageData[]>(60000){
		@Override
		protected LanguageData[] updateData() throws Exception
		{
			LanguageData[] res = languagesModel.getRows().toArray(new LanguageData[0]);
			for (int i = 0; i < res.length; i++)
				res[i].truncateInternalData();
			return res;
		}
	};

	/* TeamProblems cache */
	private CachedObject<ProblemData[]> cachedTeamProblems = new CachedObject<ProblemData[]>(60000){
		@Override
		protected ProblemData[] updateData() throws Exception
		{
	 		ProblemData[] res = problemsModel.getRows().toArray(new ProblemData[0]);
			for (int i = 0; i < res.length; i++)
				res[i].truncateInternalData();
			return res;
		}
	};
	
	/* TeamSubmissions cache */
	private CachedObjectsSet<String, SubmissionData[]> cachedSubmission = new CachedObjectsSet<String, SubmissionData[]>(10000){

		@Override
		public CachedObject<SubmissionData[]> getObjectForKey(final String key)
		{
			return new CachedObject<SubmissionData[]>(updateInterval){
				
				private String keyStr = key;
				
				{
					log.info("Creating CO for " + key);
				}
				
				@Override
				protected SubmissionData[] updateData() throws Exception
				{
					SubmissionsDataModel sdm = new SubmissionsDataModel();
					sdm.setWhere(" `user_id` = " + keyStr);
					sdm.updateData();
					return sdm.getRows().toArray(new SubmissionData[0]);
				}
				
			};
		}
		
	};
	
	
	public ContestCore()
	{
		initCore(false);
	}
	
	public ContestCore(boolean startServices)
	{
		initCore(startServices);
	}
	
	@Override
	public boolean addLanguage(String username, String password, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		log.info("addLanguage request from " + username);
		if (!usersModel.isAdmin(username, password))
		{
			log.info("addLanguage failed " + username);
			return false;
		}
		
		return addLanguageCore(sid, shortName, fullName, compilationComand, djudgeID);
	}
	
	@Override
	public boolean editLanguage(String username, String password, String id, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		log.info("editLanguage request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return editLanguageCore(id, sid, shortName, fullName, compilationComand, djudgeID);
	}
	
	@Override
	public boolean editUser(String username, String password, String id,
			String newUserName, String newPassword, String name, String role)
	{
		log.debug("editUser request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return editUserCore(id, newUserName, newPassword, name, role);
	}
	
	@Override
	public boolean deleteLanguage(String username, String password, String id)
	{
		log.info("deleteLanguage request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;

		return deleteAbstract(languagesModel, id);
	}
	
	@Override
	public boolean deleteUser(String username, String password, String id)
	{
		log.info("deleteUser request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;

		return deleteAbstract(usersModel, id);
	}
	
	@Override
	public boolean submitSolution(String username, String password, String problemID, String languageID, String sourceCode)
	{
		log.info("submitSolution request from " + username);
		String userID = usersModel.getUserID(username, password);
		if (!state.isRunnning() || Integer.parseInt(userID) <= 0 || !problemsModel.isValidID(problemID) || !languagesModel.isValidID(languageID))
			return false;
		
		return submitSolutionCore(userID, problemID, languageID, sourceCode, false);
	}

	@Override
	public ProblemData[] getProblems(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
			return new ProblemData[0];
		
		problemsModel.updateData();
		return problemsModel.getRows().toArray(new ProblemData[0]);
	}

	public boolean enterContest(String username, String password)
	{
		String userID = usersModel.getUserID(username, password);
		return Integer.parseInt(userID) > 0;
	}

	@Override
	public LanguageData[] getLanguages(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
		{
			log.info("getLanguages failed " + username + password);
			return new LanguageData[0];
		}
		
		return languagesModel.getRows().toArray(new LanguageData[0]);
	}

	@Override
	public String getVersion()
	{
		return versionString;
	}

	@Override
	public String getContestStatus(String username, String password)
	{
		return state.getContestState().toString();
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
		if (!usersModel.isAdmin(username, password))
			return null;
		
		return getMonitorInternal(false);
	}
	
	@Override
	public boolean addUser(String username, String password,
			String newUserName, String newPassword, String name, String role)
	{
		log.info("addUser request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;

		return addUserCore(newUserName, newPassword, name, role);
	}

	@Override
	public UserData[] getUsers(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
		{
			log.info("getUsers failed " + username + password);
			return new UserData[0];
		}
		
		usersModel.updateData();
		return usersModel.getRows().toArray(new UserData[0]);
	}

	@Override
	public boolean addProblem(String username, String password, String sid,
			String name, String djudgeProblem, String djudgeContest)
	{
		log.info("addProblem request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;

		return addProblemCore(sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteProblem(String username, String password, String id)
	{
		log.info("deleteProblem request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;

		return deleteAbstract(problemsModel, id);
	}

	@Override
	public boolean editProblem(String username, String password, String id,
			String sid, String name, String djudgeProblem, String djudgeContest)
	{
		
		if (!usersModel.isAdmin(username, password))
			return false;
		
		DBRowAbstract rd = problemsModel.getRowByID(id);
		if (rd == null)
			return false;
		ProblemData pd = new ProblemData(id, sid, name, djudgeProblem, djudgeContest);
		rd = problemsModel.toRow(pd);
		rd.save();
		problemsModel.updateData();
		return true;
	}

	@Override
	public boolean deleteSubmission(String username, String password, String id)
	{
		log.info("deleteSubmission request from " + username);
		if (!usersModel.isAdmin(username, password))
			return false;

		return deleteAbstract(submissionsModel, id);
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
		String userID = usersModel.getUserID(username, password);
		if (Integer.parseInt(userID) <= 0)
			return new ProblemData[0];
		
		return (ProblemData[]) cachedTeamProblems.getData();
	}

	@Override
	public boolean enterContestTeam(String username, String password)
	{
		String userID = usersModel.getUserID(username, password);
		if (Integer.parseInt(userID) <= 0)
			return false;
		
		return true;
	}

	@Override
	public SubmissionData[] getTeamSubmissions(String username, String password)
	{
		String userID = usersModel.getUserID(username, password);
		if (Integer.parseInt(userID) <= 0)
			return new SubmissionData[0];
		
		return cachedSubmission.getData(userID);
	}

	@Override
	public LanguageData[] getTeamLanguages(String username, String password)
	{
		String userID = usersModel.getUserID(username, password);
		if (Integer.parseInt(userID) <= 0)
			return new LanguageData[0];
		
		return (LanguageData[]) cachedTeamLanguages.getData();
	}

	@Override
	public String echo(String what)
	{
		return what;
	}

	@Override
	public MonitorData getTeamMonitor(String username, String password)
	{
		String userID = usersModel.getUserID(username, password);
		if (Integer.parseInt(userID) <= 0)
			return new MonitorData();
		
		MonitorData data = (MonitorData) cachedTeamMonitor.getData();
		return data;
	}

	@Override
	public String registerTeam(String username, String password)
	{
		if (!settings.allowNewUserRegistration() || null != usersModel.getUserByUsername(username))
			return "-1";
		
		UserData ud = new UserData(username, password, "", "TEAM");
		DBRowAbstract rd = usersModel.toRow(ud);
		rd.appendTo(usersModel);
		usersModel.updateData();
		return usersModel.getUserByUsername(username).getID();
	}

	@Override
	public boolean deleteAllLanguages(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return deleteAllAbstract(languagesModel);
	}

	@Override
	public boolean changePassword(String username, String oldPassword,
			String newPassword)
	{
		log.info("deleteSubmission request from " + username);
		String userID = usersModel.getUserID(username, oldPassword);
		if (Integer.parseInt(userID) <= 0)
			return false;
		
		return changePasswordCore(userID, newPassword);
	}

	@Override
	public boolean deleteAllUsers(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return deleteAllAbstract(usersModel);
	}

	@Override
	public boolean deleteAllProblems(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return deleteAllAbstract(problemsModel);
	}

	@Override
	public boolean deleteAllSubmissions(String username, String password)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return deleteAllAbstract(submissionsModel);
	}

	@Override
	public boolean deleteAllData(String username, String password)
	{
		return deleteAllAbstract(submissionsModel)
				&& deleteAllAbstract(languagesModel)
				&& deleteAllAbstract(problemsModel)
				&& deleteAllAbstract(usersModel);
	}

	@Override
	public boolean setContestFreezeTime(String username, String password,
			long tillTimeLeft)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		// state.setFreezeTime(tillTimeLeft);
		return true;
	}

	@Override
	public boolean setContestRunning(String username, String password,
			boolean isRunning)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return state.setContestRunning(isRunning);
	}

	@Override
	public boolean setContestTimeLeft(String username, String password,
			long timeLeft)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		state.setContestTimeLeft(timeLeft);
		return true;
	}

	@Override
	public boolean setContestTimePast(String username, String password,
			long timePast)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		state.setContestTime(timePast);
		return true;
	}

	@Override
	public boolean changePasswordTeam(String username, String oldPassword,
			String newPassword)
	{
		return changePassword(username, oldPassword, newPassword);
	}

	@Override
	public long getContestFreezeTime(String username, String password)
	{
		return 60 * 1000 * 60;
	}

	@Override
	public boolean activateSubmission(String username, String password,
			String id, int active)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return activateSubmissionInternal(id, active);
	}

	@Override
	public boolean generateLogins(String username, String password, int count,
			String loginType)
	{
		if (!usersModel.isAdmin(username, password))
			return false;
		
		return generateLoginsInternal(count, loginType);
	}

	@Override
	public boolean testSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		log.info("submitSolution request from " + username);
		String userID = usersModel.getUserID(username, password);
		if (!state.isRunnning() || Integer.parseInt(userID) <= 0 || !problemsModel.isValidID(problemID) || !languagesModel.isValidID(languageID))
			return false;
		
		return submitSolutionCore(userID, problemID, languageID, sourceCode, true);
	}
}
