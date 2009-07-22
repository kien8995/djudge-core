package db;

import java.util.Vector;

import org.apache.commons.codec.binary.Base64;

import djudge.acmcontester.structures.SubmissionData;


class DBRowSubmissions extends DBRowAbstract
{
	@Override
	public Class<? extends AbstractTableDataModel> getTableClass()
	{
		return SubmissionsDataModel.class;
	}
}

public class SubmissionsDataModel extends AbstractTableDataModel
{
	public final static String tableName = "submissions";

	public final static DBField[] columns = {
		new DBField("id", "#"),
		new DBField("user_id", "User ID", CellDefault.class, -1),
		new DBField("problem_id", "Problem ID", CellDefault.class, -1),
		new DBField("language_id", "Language ID", CellDefault.class),
		new DBField("contest_time", "Contest Time", CellDefault.class),
		new DBField("real_time", "Real Time", CellDefault.class),
		new DBField("judgement", "judgement", CellDefault.class),
		new DBField("max_time", "judgement", CellDefault.class),
		new DBField("max_memory", "judgement", CellDefault.class),
		new DBField("max_output", "judgement", CellDefault.class),
		new DBField("source_code", "Source", CellDefault.class),
		new DBField("failed_test", "Failed", CellDefault.class),
		new DBField("score", "Failed", CellDefault.class),
		new DBField("judged", "Failed", CellDefault.class),
		new DBField("active", "Failed", CellDefault.class),
		new DBField("djudge_flag", "DJudge", CellDefault.class),
		new DBField("xml", "XML", CellDefault.class),
	};
	
	@Override
	protected DBField[] getTableFields()
	{
		return columns;
	}
	
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<? extends DBRowAbstract> getRowClass()
	{
		return DBRowSubmissions.class;
	}

	@Override
	protected String getTableName()
	{
		return tableName;
	}
	
	public DBRowAbstract toRow(SubmissionData sd)
	{
		DBRowSubmissions row = new DBRowSubmissions();
		row.data[0] = "";
		row.data[0] = sd.id;
		row.data[1] = sd.userID;
		row.data[2] = sd.problemID;
		row.data[3] = sd.languageID;
		row.data[4] = sd.contestTime;
		row.data[5] = sd.realTime;
		row.data[6] = sd.judgement;
		row.data[7] = sd.maxTime;
		row.data[8] = sd.maxMemory;
		row.data[9] = sd.maxOutput;
		row.data[10] = sd.sourceCode;
		row.data[11] = sd.failedTest;
		row.data[12] = sd.score;
		row.data[13] = sd.judged;
		row.data[14] = sd.active;
		row.data[15] = sd.djudgeFlag;
		row.data[16] = new String(Base64.encodeBase64(sd.xml.getBytes()));
		return row;
	}

	public SubmissionData toSubmissionData(DBRowAbstract row)
	{
		SubmissionData sd = new SubmissionData();
		sd.id = row.data[0].toString();
		sd.userID = row.data[1].toString();
		sd.problemID = row.data[2].toString();
		sd.languageID = row.data[3].toString();
		sd.contestTime = Integer.parseInt(row.data[4].toString());
		sd.realTime = row.data[5].toString();
		sd.judgement = row.data[6].toString();
		sd.maxTime = Integer.parseInt(row.data[7].toString());
		sd.maxMemory = Integer.parseInt(row.data[8].toString());
		sd.maxOutput = Integer.parseInt(row.data[9].toString());
		sd.sourceCode = row.data[10].toString();
		sd.failedTest = Integer.parseInt(row.data[11].toString());
		sd.score = Integer.parseInt(row.data[12].toString());
		sd.judged = Integer.parseInt(row.data[13].toString());
		sd.active = Integer.parseInt(row.data[14].toString());
		sd.djudgeFlag = Integer.parseInt(row.data[15].toString());
		sd.xml = new String(Base64.decodeBase64(row.data[16].toString().getBytes()));
		return sd;
	}
		
	public Vector<SubmissionData> getRows()
	{
		Vector<SubmissionData> res = new Vector<SubmissionData>();
		for (int i = 0; i < rows.size(); i++)
		{
			res.add(toSubmissionData(rows.get(i)));
		}
		return res;
	}
}

