package djudge.acmcontester;

import org.apache.commons.codec.binary.Base64;

import db.DBRowAbstract;
import db.LanguagesDataModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import db.UsersDataModel;
import djudge.acmcontester.structures.SubmissionData;

public class ContestCore
{
	private UsersDataModel usersModel;
	
	private ProblemsDataModel problemsModel;
	
	private LanguagesDataModel languagesModel;
	
	private SubmissionsDataModel submissionsModel;
	
	private ContestSettings contest = new ContestSettings();
	
	private DServiceInterface djudgeInterface;
	
	private void setData()
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
	}
	
	public ContestCore()
	{
		setData();
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
	
	public static void main(String[] args)
	{
		new Admin();
	}
	
	public boolean submitSolution(String username, String password, String problemID, String languageID, String courceCode)
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
}
