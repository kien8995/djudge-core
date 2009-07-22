package djudge.acmcontester;

import org.apache.commons.codec.binary.Base64;

import db.DBRowAbstract;
import db.LanguagesDataModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import db.UsersDataModel;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;

public class ContestCore
{
	private static UsersDataModel usersModel;
	
	private static ProblemsDataModel problemsModel;
	
	private static LanguagesDataModel languagesModel;
	
	private static SubmissionsDataModel submissionsModel;
	
	private static ContestSettings contest = new ContestSettings();
	
	private static DServiceInterface djudgeInterface;
	
	static
	{
		usersModel = new UsersDataModel();
		usersModel.fill();
		
		problemsModel = new ProblemsDataModel();
		problemsModel.fill();
		
		languagesModel = new LanguagesDataModel();
		languagesModel.fill();
		
		submissionsModel = new SubmissionsDataModel();
		submissionsModel.fill();
		
		djudgeInterface = new DServiceInterface();
		djudgeInterface.start();
		
		new AcmContesterXmlRpcServer().start();
	}
	
	public static UsersDataModel getUsersModel()
	{
		return usersModel;
	}
	
	public static LanguagesDataModel getLanguagesModel()
	{
		return languagesModel;
	}
	
	public static ProblemsDataModel getProblemsModel()
	{
		return problemsModel;
	}
	
	public static SubmissionsDataModel getSubmissionsDataModel()
	{
		return submissionsModel;
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
	
	public static boolean submitSolution(String username, String password, String problemID, String languageID, String courceCode)
	{
		String userID = usersModel.getUserID(username, password);
		if (!contest.isRunnning() || Integer.parseInt(userID) <= 0 || !problemsModel.isValidID(problemID) || !languagesModel.isValidID(problemID))
		{
			return false;
		}
		SubmissionData sd = new SubmissionData();
		sd.contestTime = contest.getContestTime();
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

	public static ProblemData[] getProblems(AuthentificationData userInfo)
	{
		return problemsModel.getRows().toArray(new ProblemData[0]);
	}

	public static boolean enterContest(AuthentificationData userInfo)
	{
		String userID = usersModel.getUserID(userInfo.username, userInfo.password);
		return Integer.parseInt(userID) > 0;
	}

	public static String registerUser(String username, String password)
	{
		return "OK";
	}

	public static SubmissionData[] getAllSubmissions(AuthentificationData userInfo)
	{
		return submissionsModel.getRows().toArray(new SubmissionData[0]);
	}

	public static SubmissionData[] getOwnSubmissions(AuthentificationData userInfo)
	{
		return submissionsModel.getRows().toArray(new SubmissionData[0]);
	}

	public static boolean submitSolution(AuthentificationData userInfo,
			String problemID, String languageID, String sourceCode)
	{
		return submitSolution(userInfo.username, userInfo.password, problemID, languageID, sourceCode);
	}

	public static LanguageData[] getLanguages(AuthentificationData userInfo)
	{
		return languagesModel.getRows().toArray(new LanguageData[0]);
	}

	public static String getVersion()
	{
		return "Version 1E-2";
	}
}
