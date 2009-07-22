package db;


class DBRowLanguages extends DBRowAbstract
{
	@Override
	public Class<? extends AbstractTableDataModel> getTableClass()
	{
		return LanguagesDataModel.class;
	}
}

public class LanguagesDataModel extends AbstractTableDataModel
{
	public final static String tableName = "languages";

	public final static DBField[] columns = {
		new DBField("id", "#"),
		new DBField("sid", "Скорочена назва", CellDefault.class, "-"),
//		new DBField("short_name", "Повна назва", CellDefault.class, "-"),
		new DBField("full_name", "Повна назва", CellDefault.class, "-"),
//		new DBField("compilation_command", "Повна назва", CellDefault.class, "-"),
		new DBField("djudge_id", "Повна назва", CellDefault.class, "-"),
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
}

