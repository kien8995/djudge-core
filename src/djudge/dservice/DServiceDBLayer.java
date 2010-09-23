package djudge.dservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.apache.log4j.Logger;

public class DServiceDBLayer
{
	private static final Logger log = Logger.getLogger(DServiceDBLayer.class);
	
	protected final String dbMutex = "mutex";
	
	private final String databasePath = "data/dservice/dservice.db3";
	
	private final static String databaseDriverClassName = "org.sqlite.JDBC"; 

	private Connection con;
	
	{
		try
		{
			Class.forName(databaseDriverClassName);
			con = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
		}
		catch (ClassNotFoundException ex)
		{
			log.fatal("Cannot find database driver class: org.sqlite.JDBC", ex);
			System.exit(1);
		}		
		catch (SQLException ex)
		{
			log.fatal("Database corrupted or does not exists: " + databasePath, ex);
			System.exit(2);
		}		
	}

	protected Statement getStatement()
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
	
	protected String generateUID()
	{
		final String chars = "123254xyz";
		
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < 24; i++)
			str.append(chars.charAt((Math.abs(new Random().nextInt()))%chars.length()));
		return str.toString();
	}
	
	protected boolean checkUID(String uid)
	{
		try
		{
			synchronized (dbMutex)
			{
				Statement st = getStatement();
				String sql = "SELECT * FROM users WHERE hash = '" + uid + "'";
				ResultSet rs = st.executeQuery(sql);
				boolean res = !rs.next();
				rs.close();
				st.close();
				return res;
			}
		}
		catch (Exception e)
		{
			log.error("Database error", e);
		}
		return true;
	}
	
	protected int getUserID(String hash)
	{
		synchronized (dbMutex)
		{
    		Statement st = getStatement();
    		String sql = "SELECT id FROM users WHERE hash = '" + hash + "'";
    		try
    		{
    			ResultSet rs = st.executeQuery(sql);
    			int res;
    			if (!rs.next())
    				res = 0;
    			else
    				res = rs.getInt("id");
    			rs.close();
    			st.close();
    			return res;
    		}
    		catch (Exception e)
    		{
    			log.error("Database error", e);
    		}
		}
		return 0;		
	}
	
	protected int getLastSubmissionID()
	{
		synchronized (dbMutex)
		{
			try
			{
				Statement st = getStatement();
				String sql = "SELECT max(id) AS id FROM submissions";
				ResultSet rs = st.executeQuery(sql);
				int res;
				if (!rs.next())
					res = 0;
				else
					res = rs.getInt("id");
				rs.close();
				st.close();
				return res;
			}
			catch (Exception e)
			{
				log.error("Database error", e);
			}
		}
		return 0;
	}

	protected DServiceTask getTaskInternal(int judgeID)
	{
		DServiceTask res = null;
		int submissionID = 0;
		try
		{
			synchronized (dbMutex)
			{
				Statement st = getStatement();
				String sql = "SELECT * FROM submissions WHERE judge_status = 0 ORDER BY id ASC LIMIT 1";
				log.debug(sql);
				ResultSet rs = st.executeQuery(sql);
				if (rs.next())
				{
					res = new DServiceTask();
					res.clientData = rs.getString("client_data");
					res.contestId = rs.getString("contest");
					res.problemId = rs.getString("problem");
					res.languageId = rs.getString("language");
					res.id = submissionID = rs.getInt("id");
					res.params = rs.getString("check_params");
				}
				rs.close();
				st.close();
			}
			if (submissionID > 0)
			{
				String tsql = "UPDATE submissions SET judge_id = '" + judgeID
						+ "', judge_status = -1 WHERE id = " + submissionID;
				executeSql(tsql);
			}
		} catch (Exception e)
		{
			log.error("Database error", e);
		}
		return res;
	}
	
	
	protected boolean checkUsername(String username)
	{
		synchronized (dbMutex)
		{
			try
			{
				Statement st = getStatement();
				String sql = "SELECT * FROM users WHERE username = '" + username + "'";
				log.debug(sql);
				ResultSet rs = st.executeQuery(sql);
				boolean res = !rs.next();
				rs.close();
				st.close();
				return res;
			}
			catch (Exception e)
			{
				log.error("Database error", e);
			}
		}
		return true;
	}
	
	protected void executeSql(String sql)
	{
		synchronized (dbMutex)
		{
			try
			{
				Statement st = getStatement();
				log.debug("Executing SQL request:" + sql);
				st.executeUpdate(sql);
				st.close();
			}
			catch (Exception ex)
			{
				log.error("Database error", ex);
			}
		}
	}

	

}
