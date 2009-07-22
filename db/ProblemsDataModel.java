package db;


class DBRowProblems extends DBRowAbstract
{
	@Override
	public Class<? extends DBTableAbstract> getTableClass()
	{
		return ProblemsDataModel.class;
	}
}

public class ProblemsDataModel extends AbstractTableDataModel
{
	public final static String tableName = "problems";

	public final static DBField[] columns = {
		new DBField("id", "#"),
		new DBField("sid", "Скорочена назва", CellDefault.class, "-"),
		new DBField("name", "Повна назва", CellDefault.class, "-"),
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
}

