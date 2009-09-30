package djudge.acmcontester.server;

import org.apache.log4j.Logger;

import db.AbstractTableDataModel;
import db.DBRowAbstract;
import db.LanguagesDataModel;
import db.MonitorModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import db.UsersDataModel;
import djudge.acmcontester.structures.LanguageData;


public class ContestCoreInternals
{
	private static final Logger log = Logger.getLogger(ContestCoreInternals.class);
	
	protected static final String versionString = "1.0";
	
	protected UsersDataModel usersModel;
	
	protected ProblemsDataModel problemsModel;
	
	protected LanguagesDataModel languagesModel;
	
	protected SubmissionsDataModel submissionsModel;
	
	protected MonitorModel monitorModel;
	
	protected ContestSettings contest = new ContestSettings("contest.xml");
	
	protected ContestState state = new ContestState(contest);
	
	protected DServiceConnector djudgeInterface;
	
	protected void initCore(boolean flagStandalone)
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
	
	public boolean deleteAbstract(AbstractTableDataModel model, String id)
	{
		DBRowAbstract rd = model.getRowByID(id);
		if (rd == null)
			return false;
		
		boolean res = rd.delete(model);
		if (res)
		{
			model.updateData();
			log.info("Deleted OK");
		}
		return res;
	}
	
	protected boolean addLanguageCore(String sid, String shortName,
			String fullName, String compilationComand, String djudgeID)
	{
		LanguageData ld = new LanguageData(sid, shortName, fullName, compilationComand, djudgeID);
		DBRowAbstract rd = languagesModel.toRow(ld);
		boolean res = rd.appendTo(languagesModel);
		if (res)
			log.info("Language " + sid + " " + shortName + " " + fullName + " " + djudgeID + " added");
		return res;
	}
	
	protected boolean editLanguageCore(String id, String sid, String shortName,
			String fullName, String compilationComand, String djudgeID)
	{
		DBRowAbstract rd = languagesModel.getRowByID(id);
		if (rd == null)
			return false;
		LanguageData ld = new LanguageData(id, sid, shortName, fullName, compilationComand, djudgeID);
		rd = languagesModel.toRow(ld);
		boolean res = rd.save();
		if (res)
		{
			languagesModel.updateData();
			log.info("Language " + id + " " + sid + " " + shortName + " " + fullName + " " + djudgeID + " changed");
		}
		return res;
	}	
}
