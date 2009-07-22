package db;


class DBRowUsers extends DBRowAbstract
{
	@Override
	public Class<? extends DBTableAbstract> getTableClass()
	{
		return UsersDataModel.class;
	}
}

public class UsersDataModel extends AbstractTableDataModel
{
	public final static String tableName = "users";

	public final static DBField[] columns = {
		new DBField("id", "#"),
		new DBField("username", "Скорочена назва", CellDefault.class, "-"),
		new DBField("name", "Повна назва", CellDefault.class, "-"),
		new DBField("surname", "Повна назва", CellDefault.class, "-"),
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
		return DBRowUsers.class;
	}

	@Override
	protected String getTableName()
	{
		return tableName;
	}
}

