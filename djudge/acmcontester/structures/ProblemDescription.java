package djudge.acmcontester.structures;

import java.sql.ResultSet;

import djudge.acmcontester.models.AbstractDBModel;

public class ProblemDescription
{
	public final static String[] names = {
		"id",
		"sid",
		"name",
		"djudge_contest",
		"djudge_problem",
	};

	public String id;
	public String sid;
	public String name;
	public String djudgeContest;
	public String djudgeProblem;
	
	public ProblemDescription()
	{
		
	}
	
	public ProblemDescription(ResultSet rs)
	{
		fill(rs);
	}
	
	private void fill(ResultSet rs)
	{
		try
		{
        	id = rs.getString("id");
        	sid = rs.getString("sid");
        	name = rs.getString("name");
        	djudgeContest = rs.getString("djudge_contest");
        	djudgeProblem = rs.getString("djudge_problem");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getUpdateStatement()
	{
		return "UPDATE `problems` SET " + 
			"`sid` = '" + AbstractDBModel.escape(sid)  + "', " +
			"`name` = '" + AbstractDBModel.escape(name) + "', " +
			"`djudge_contest` = '" + AbstractDBModel.escape(djudgeContest) + "', " +
			"`djudge_problem` = '" + AbstractDBModel.escape(djudgeProblem) + "' " +
			"WHERE `id` = '" + AbstractDBModel.escape(id) + "'";
			
	}
	
	public String getColumnValue(int columnIndex)
	{
		switch (columnIndex)
		{
		case 0: return id;
		case 1: return sid;
		case 2: return name;
		case 3: return djudgeContest;
		case 4: return djudgeProblem;
		}
		return "";
	}
	
	public void setColumnValue(int columnIndex, String newValue)
	{
		switch (columnIndex)
		{
		case 1:
			sid = newValue;
			break;
			
		case 2:
			name = newValue;
			break;
			
		case 3: 
			djudgeContest = newValue;
			break;
			
		case 4:
			djudgeProblem = newValue;
			break;
		}
	}
	
}
