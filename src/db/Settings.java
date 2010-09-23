/* $Id$ */

package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class Settings
{
	private static Connection conn;
	
	static
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			//FIXME: hardcode
			conn = DriverManager.getConnection("jdbc:sqlite:data/simpleacm/contest.db3");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}		
	}
	
	public static Connection getConnection()
	{
		return conn;
	}
	
}
