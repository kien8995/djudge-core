package djudge.acmcontester.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AbstractDBModel
{
	public static String dbMutex = new String();
	
	static Connection con;
	
	static
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:data/simpleacm/contest.db3");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}		
	}
	
	public static Statement getStatement()
	{
		try
		{
			Statement stmt = con.createStatement();
			return stmt;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void executeUpdate(String sql)
	{
		synchronized (AbstractDBModel.dbMutex)
		{
			Statement st = AbstractDBModel.getStatement();
			try
			{
				System.out.println(sql);
				st.executeUpdate(sql);
				st.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//FIXME
	public static String escape(String data)
	{
		return data;
	}
}
