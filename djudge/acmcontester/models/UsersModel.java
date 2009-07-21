package djudge.acmcontester.models;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import djudge.acmcontester.structures.UserDescription;

public class UsersModel
{
	private static UserDescription fillDescription(ResultSet rs)
	{
		UserDescription ud = new UserDescription();
		try
		{
        	ud.id = rs.getString("id");
        	ud.username = rs.getString("username");
        	ud.password = rs.getString("password");
        	ud.name = rs.getString("name");
        	ud.surname = rs.getString("surname");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ud;
	}
	
	public static UserDescription[] getAllUsers()
	{
		Vector <UserDescription> v = new Vector <UserDescription>();  
		synchronized (AbstractDBModel.dbMutex)
		{
			Statement st = AbstractDBModel.getStatement();
			String sql = "SELECT * FROM users";
			try
			{
				ResultSet rs = st.executeQuery(sql);
				while (rs.next())
				{
					v.add(fillDescription(rs));
				}
				rs.close();
				st.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return v.toArray(new UserDescription[0]);
	}

	public static UserDescription getUser(String userID)
	{
		UserDescription result = null;
		synchronized (AbstractDBModel.dbMutex)
		{
			Statement st = AbstractDBModel.getStatement();
			String sql = "SELECT * FROM users where users.id = " + userID;
			try
			{
				ResultSet rs = st.executeQuery(sql);
				if (rs.next())
				{
					result = fillDescription(rs);
				}
				rs.close();
				st.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
