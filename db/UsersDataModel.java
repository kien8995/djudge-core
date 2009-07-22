package db;

class DBRowUsers extends DBRowAbstract
{
	@Override
	public Class<? extends AbstractTableDataModel> getTableClass()
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
		new DBField("password", "Повна назва", CellDefault.class, "-"),
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
	
	public String getUserID(String username, String password)
	{
		for (int i = 0; i < getRowCount(); i++)
		{
			if (getValueAt(i, 1).equals(username) && getValueAt(i, 4).equals(password))
			{
				return getValueAt(i, 0).toString(); 
			}
		}
		return "-1";
	}
}

