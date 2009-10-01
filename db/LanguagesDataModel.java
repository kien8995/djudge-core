package db;

import java.util.Vector;

import djudge.acmcontester.structures.LanguageData;

public class LanguagesDataModel extends AbstractTableDataModel
{
	public final static String tableName = "languages";

	public final static DBField[] columns = {
		new DBField("id", "#"),
		new DBField("sid", "ID", String.class, "-"),
		new DBField("short_name", "Short name", String.class, "-"),
		new DBField("full_name", "Full name", String.class, "-"),
		new DBField("compilation_command", "Compilation command", String.class, "-"),
		new DBField("djudge_id", "DJudgeID", String.class, "-"),
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
		return DBRowLanguages.class;
	}

	@Override
	protected String getTableName()
	{
		return tableName;
	}
	
	public DBRowAbstract toRow(LanguageData ld)
	{
		DBRowLanguages row = new DBRowLanguages();
		row.data[0] = ld.id;
		row.data[1] = ld.sid;
		row.data[2] = ld.shortName;
		row.data[3] = ld.fullName;
		row.data[4] = ld.compilationCommand;
		row.data[5] = ld.djudgeID;
		return row;
	}

	public LanguageData toLanguageData(DBRowAbstract row)
	{
		LanguageData ld = new LanguageData();
		ld.id = row.data[0].toString();
		ld.sid = row.data[1].toString();
		ld.shortName = row.data[2].toString();
		ld.fullName = row.data[3].toString();
		ld.compilationCommand = row.data[4].toString();
		ld.djudgeID = row.data[5].toString();
		return ld;
	}
		
	public Vector<LanguageData> getRows()
	{
		Vector<LanguageData> res = new Vector<LanguageData>();
		for (int i = 0; i < rows.size(); i++)
		{
			res.add(toLanguageData(rows.get(i)));
		}
		return res;
	}
	
}

