package db;

import java.util.Vector;

import djudge.acmcontester.structures.ProblemData;


class DBRowProblems extends DBRowAbstract
{
	@Override
	public Class<? extends AbstractTableDataModel> getTableClass()
	{
		return ProblemsDataModel.class;
	}
}

public class ProblemsDataModel extends AbstractTableDataModel
{
	public final static String tableName = "problems";

	public final static DBField[] columns = {
		new DBField("id", "#"),
		new DBField("sid", "Скорочена назва", String.class, "-"),
		new DBField("name", "Повна назва", String.class, "-"),
		new DBField("djudge_problem", "Повна назва", String.class, "-"),
		new DBField("djudge_contest", "Повна назва", String.class, "-"),
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
		return DBRowProblems.class;
	}

	@Override
	protected String getTableName()
	{
		return tableName;
	}

	public DBRowAbstract toRow(ProblemData ld)
	{
		DBRowProblems row = new DBRowProblems();
		row.data[0] = ld.id;
		row.data[1] = ld.sid;
		row.data[2] = ld.name;
		row.data[3] = ld.djudgeProblem;
		row.data[4] = ld.djudgeContest;
		return row;
	}

	public ProblemData toProblemData(DBRowAbstract row)
	{
		ProblemData ld = new ProblemData();
		ld.id = row.data[0].toString();
		ld.sid = row.data[1].toString();
		ld.name = row.data[2].toString();
		ld.djudgeProblem = row.data[3].toString();
		ld.djudgeContest = row.data[4].toString();
		return ld;
	}
		
	public Vector<ProblemData> getRows()
	{
		Vector<ProblemData> res = new Vector<ProblemData>();
		for (int i = 0; i < rows.size(); i++)
		{
			res.add(toProblemData(rows.get(i)));
		}
		return res;
	}
}

