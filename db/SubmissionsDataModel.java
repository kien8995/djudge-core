package db;

import java.util.Vector;
import org.apache.commons.codec.binary.Base64;

import djudge.acmcontester.structures.SubmissionData;


public class SubmissionsDataModel extends AbstractTableDataModel
{
	public final static String tableName = "view_submissions";

	public final static DBField[] columns = {
		new DBField("id", "#"),
		new DBField("user_id", "User ID", String.class, -1),
		new DBField("problem_id", "Problem ID", String.class, -1),
		new DBField("language_id", "Language ID", String.class),
		new DBField("contest_time", "Contest Time", String.class),
		new DBField("real_time", "Real Time", String.class),
		new DBField("judgement", "Judgement", String.class),
		new DBField("max_time", "Time", Integer.class),
		new DBField("max_memory", "Memory", Integer.class),
		new DBField("max_output", "Output", Integer.class),
		new DBField("source_code", "Source", String.class),
		new DBField("failed_test", "Test #", Integer.class),
		new DBField("score", "Score", Integer.class),
		new DBField("judged", "Judged?", Integer.class),
		new DBField("active", "Active?", Integer.class),
		new DBField("djudge_flag", "DJudgeStatus", Integer.class),
		new DBField("xml", "XML", String.class),
		new DBField("username", "Username", String.class, "", false),
		new DBField("sid:1", "ProblemSID", String.class, "", false),
		new DBField("name:1", "ProblemName", String.class, "", false),
		new DBField("sid", "LanguageID", String.class, "", false),
	};
	
	public final static int getUserFieldIndex()
	{
		return 1;
	}
	
	public final static int getProblemFieldIndex()
	{
		return 2;
	}
	
	public final static int getLanguageFieldIndex()
	{
		return 3;
	}
	
	public final static int getContestTimeFieldIndex()
	{
		return 4;
	}
	
	
	public final static int getActiveFlagIndex()
	{
		return 14;
	}
	
	public final static int getDJudgeFlagIndex()
	{
		return 15;
	}
	
	public final static int getXmlIndex()
	{
		return 16;
	}
	
	public final static int getJudgementFieldIndex()
	{
		return 6;
	}
	
	public final static int getRuntimeFieldIndex()
	{
		return 7;
	}
	
	public final static int getMemoryFieldIndex()
	{
		return 8;
	}
	
	public final static int getOutputFieldIndex()
	{
		return 9;
	}
	
	public final static int getFailedTestFieldIndex()
	{
		return 11;
	}
	
	public final static int getScoreFieldIndex()
	{
		return 12;
	}
	
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
		row.data[17] = sd.username;
		row.data[18] = sd.problemSid;
		row.data[19] = sd.problemName;
		row.data[20] = sd.languageSid;
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
		sd.username = row.data[17].toString();
		sd.problemSid = row.data[18].toString();
		sd.problemName = row.data[19].toString();
		sd.languageSid = row.data[20].toString();
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
	
	public SubmissionData getRowT(int index)
	{
		return toSubmissionData(rows.get(index));
	}
	
	private void doRejudge(DBRowAbstract row)
	{
		row.data[getFailedTestFieldIndex()] = -1;
		row.data[getJudgementFieldIndex()] = "N/A";
		row.data[getMemoryFieldIndex()] = -1;
		row.data[getOutputFieldIndex()] = -1;
		row.data[getScoreFieldIndex()] = -1;
		row.data[getDJudgeFlagIndex()] = 0;
		row.data[getRuntimeFieldIndex()] = -1;
		row.data[getXmlIndex()] = "";
		row.save();
	}
	
	public void rejudgeBy(String fieldName, Object fieldValue)
	{
		int fieldIndex = -1;
		for (int i = 0; i < getColumnCount(); i++)
			if (columns[i].key.equals(fieldName))
				fieldIndex = i;
		if (fieldIndex < 0)
			return;
		for (int i = 0; i < getRowCount(); i++)
		{
			if (fieldValue.toString().equals(getValueAt(i, fieldIndex).toString()))
			{
				doRejudge(rows.get(i));
			}
		}
		updateData();
	}
}

