package db;

import java.util.Vector;

import djudge.acmcontester.structures.UserData;

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
		new DBField("username", "Username", String.class, "-"),
		new DBField("name", "Info", String.class, "-"),
		new DBField("password", "Password", String.class, "-"),
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
			if (getValueAt(i, 1).equals(username) && getValueAt(i, 3).equals(password))
			{
				return getValueAt(i, 0).toString(); 
			}
		}
		return "-1";
	}
	
	public DBRowAbstract toRow(UserData ld)
	{
		DBRowProblems row = new DBRowProblems();
		row.data[0] = ld.id;
		row.data[1] = ld.username;
		row.data[2] = ld.name;
		row.data[2] = ld.password;
		return row;
	}

	public UserData toUserData(DBRowAbstract row)
	{
		UserData ld = new UserData();
		ld.id = row.data[0].toString();
		ld.username = row.data[1].toString();
		ld.name = row.data[2].toString();
		ld.password = row.data[2].toString();
		return ld;
	}
		
	public Vector<UserData> getRows()
	{
		Vector<UserData> res = new Vector<UserData>();
		for (int i = 0; i < rows.size(); i++)
		{
			res.add(toUserData(rows.get(i)));
		}
		return res;
	}
	
}

