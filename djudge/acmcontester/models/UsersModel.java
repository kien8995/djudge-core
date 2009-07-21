package djudge.acmcontester.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import djudge.acmcontester.Admin;
import djudge.acmcontester.exceptions.NoDataException;
import djudge.acmcontester.structures.UserDescription;

public class UsersModel extends AbstractDBModel
{
	
	public static Vector<UserDescription> getAllUsers()
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
					v.add(new UserDescription(rs));
				}
				rs.close();
				st.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return v;
	}
	
	public static UserDescription insertRow()
	{
		UserDescription res = new UserDescription();
		synchronized (AbstractDBModel.dbMutex)
		{
			Statement st = AbstractDBModel.getStatement();
			String sql = "INSERT INTO users(`name`) VALUES('new')";
			String sql2 = "SELECT MAX(id) AS maxid FROM users";
			try
			{
				st.executeUpdate(sql);
				ResultSet rs = st.executeQuery(sql2);
				if (rs.next())
				{
					res.id = rs.getString("maxid");
				}
				rs.close();
				st.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return res;
	}

	public static UserDescription getUserByID(String userID) throws NoDataException
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
					result = new UserDescription(rs);
				}
				else
				{
					throw new NoDataException();
				}
				rs.close();
				st.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void updateData(UserDescription ud)
	{
		AbstractDBModel.executeUpdate(ud.getUpdateStatement());
	}

	public static void main(String[] args)
	{
		new Admin();
	}	
}
