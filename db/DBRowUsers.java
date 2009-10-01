package db;

public class DBRowUsers extends DBRowAbstract
{
	@Override
	public Class<? extends AbstractTableDataModel> getTableClass()
	{
		return UsersDataModel.class;
	}
	
	public String getPassword()
	{
		return data[3].toString();
	}
	
	public void setPassword(String password)
	{
		data[3] = password;
	}
	
	public String getRole()
	{
		return data[4].toString();
	}
	
	public String getID()
	{
		return data[0].toString();
	}
}


