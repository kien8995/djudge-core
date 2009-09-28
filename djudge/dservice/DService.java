package djudge.dservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Date;
import java.util.Vector;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import djudge.filesystem.RemoteFS;

public class DService
{
	static Connection con;
	
	static XmlRpcServer xmlRpcServer;
	
	static String serviceName = "dservice";
	
	static final String dbMutex = "mutex";
	
	static
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:data/" + serviceName + "/dservice.db3");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}		
	}

	private static Statement getStatement()
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
	
	private static String generateUID()
	{
		final String chars = "123254";
		
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < 8; i++)
			str.append(chars.charAt((Math.abs(new Random().nextInt()))%chars.length()));
		return str.toString();
	}
	
	private static boolean checkUID(String uid)
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
			e.printStackTrace();
		}
		return true;
	}
	
	private static int getUserID(String hash)
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
    			e.printStackTrace();
    		}
		}
		return 0;		
	}
	
	private static int getLastSubmissionID()
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
				e.printStackTrace();
			}
		}
		return 0;
		
	}

	private static DServiceTask getTaskInternal(int judgeID)
	{
		DServiceTask res = null;
		int submissionID = 0;
		try
		{
			synchronized (dbMutex)
			{
				Statement st = getStatement();
				String sql = "SELECT * FROM submissions WHERE judge_status = 0 ORDER BY id ASC LIMIT 1";
				ResultSet rs = st.executeQuery(sql);
				if (rs.next())
				{
					res = new DServiceTask();
					res.clientData = rs.getString("client_data");
					res.contestId = rs.getString("contest");
					res.problemId = rs.getString("problem");
					res.languageId = rs.getString("language");
					res.id = submissionID = rs.getInt("id");
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
			e.printStackTrace();
		}
		return res;

	}
	
	
	private static boolean checkUsername(String username)
	{
		synchronized (dbMutex)
		{
			try
			{
				Statement st = getStatement();
				String sql = "SELECT * FROM users WHERE username = '" + username + "'";
				ResultSet rs = st.executeQuery(sql);
				boolean res = !rs.next();
				rs.close();
				st.close();
				return res;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public static void executeSql(String sql)
	{
		synchronized (dbMutex)
		{
			try
			{
				Statement st = getStatement();
				System.out.println(sql);
				st.executeUpdate(sql);
				st.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	
	synchronized public static String createUser(String username, String password)
	{
		if (!checkUsername(username))
			return "";
		
		String hash = generateUID();
		while (!checkUID(hash))
			hash = generateUID();
		
		executeSql("INSERT INTO users(username, password, hash) VALUES('" + username + "', '" + password + "', '" + hash + "')");
		return hash;
	}

	synchronized public static int deleteUser(String username, String password)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public static DServiceTaskResult[] getSubmissionResult(String uid, int count)
	{
		int userID = getUserID(uid);
		if (userID <= 0)
			return null;
		return getUserTaskResults(userID, count);
	}
	
	public static DServiceTaskResult[] fetchResults(String uid)
	{
		int userID = getUserID(uid);
		if (userID <= 0)
			return null;
		return getUserTaskResults2(userID);
	}
	
	synchronized public static int submitSolution(String uid, String contestId, String problemId, 
			String languageId, String source, String clientData)
	{
		int userID = getUserID(uid);
		if (userID <= 0)
			return -DServiceResult.AUTHORIZATION_FAILED.ordinal();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateStr = dateFormat.format(date);
		
		String sql = "INSERT INTO submissions(user_id, judgement, judge_id, judge_status, contest, problem, language, date, fetched, client_data)" +
				"VALUES(" +
				"" + userID + "," + 
				"''," +
				"'0'," +
				"'0'," +
				"'" + contestId + "'," + 
				"'" + problemId + "'," + 
				"'" + languageId + "'," + 
				"'" + dateStr + "'," +
				"0," +
				"'" + clientData + "'" +
				")";
				
		executeSql(sql);
		int id = getLastSubmissionID();
		RemoteFS.writeContent(source, "data/" + serviceName + "/sources/" + formatNumber(id) + ".txt");
		
		return id;
	}
	
	private static String formatNumber(int number)
	{
		String res = "" + number;
		while (res.length() < 7)
			res = "0" + res;
		return res;
	}
	
	public static DServiceTask getTask(int judgeID)
	{
		DServiceTask task = getTaskInternal(judgeID);
		if (task == null)
			return null;
		task.source = RemoteFS.readContent("data/" + serviceName + "/sources/" + formatNumber(task.id) + ".txt");
		log("" + formatNumber(task.id) + " was sent");
		return task;
	}
	
	public static void setTaskResult(int taskID, String judgement, String xmlData)
	{
		RemoteFS.writeContent(xmlData, "data/" + serviceName + "/sources/" + formatNumber(taskID) + ".xml");
		String sql = "UPDATE submissions SET judgement = '" + judgement + "', judge_status = 1 WHERE id = " + taskID;
		executeSql(sql);
	}
	
	private static DServiceTaskResult toTaskResult(ResultSet rs)
	{
		DServiceTaskResult res = new DServiceTaskResult();
		try
		{
    		int submissionId = rs.getInt("id");
    		res.id = submissionId;
    		res.clientData = rs.getString("client_data");
    		res.contestId = rs.getString("contest");
    		res.problemId = rs.getString("problem");
    		res.languageId = rs.getString("language");
    		res.dateTime = rs.getString("date");
    		res.judgement = rs.getString("judgement");
    		res.xml = RemoteFS.readContent("data/" + serviceName + "/sources/" + formatNumber(submissionId) + ".xml");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
		return res;
	}
	
	@SuppressWarnings("unused")
	private static DServiceTaskResult getTaskResult(int submissionId)
	{
		DServiceTaskResult res = null;
		
		Statement st = getStatement();
		String sql = "SELECT * FROM submissions WHERE id = " + submissionId;
		try
		{
			ResultSet rs = st.executeQuery(sql);
			if (rs.next())
			{
				res = toTaskResult(rs);
			}
			else
				return null;
			rs.close();
			st.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}
	
	private static DServiceTaskResult[] getUserTaskResults(int userId, int count)
	{
		Vector<DServiceTaskResult> res = new Vector<DServiceTaskResult>();
		Statement st = getStatement();
		String sql = "SELECT * FROM submissions WHERE user_id = " + userId + " ORDER BY id DESC LIMIT " + count;
		try
		{
			ResultSet rs = st.executeQuery(sql);
			int c = 0;
			while (rs.next())
			{
				res.add(toTaskResult(rs));
				c++;
			}
			if (c == 0)
				return null;
			rs.close();
			st.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return res.toArray(new DServiceTaskResult[0]);
	}
	
	private static DServiceTaskResult[] getUserTaskResults2(int userId)
	{
		Vector<DServiceTaskResult> res = new Vector<DServiceTaskResult>();
		Vector <String> resSql = new Vector<String>();
		synchronized (dbMutex)
		{
			try
			{
				Statement st = getStatement();
				String sql = "SELECT * FROM submissions WHERE user_id = " + userId + " AND fetched <= 0 AND judge_status > 0 ORDER BY id DESC";
				ResultSet rs = st.executeQuery(sql);
				int c = 0;
				while (rs.next())
				{
					res.add(toTaskResult(rs));
					c++;
					String sql2 = "UPDATE submissions SET fetched = 1 WHERE id = " + rs.getInt("id");
					resSql.add(sql2);
				}
				if (c == 0)
					return null;
				rs.close();
				st.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		for (int i = 0; i < resSql.size(); i++)
		{
			executeSql(resSql.get(i));
		}
		return res.toArray(new DServiceTaskResult[0]);
	}
	
	
	public static void log(Object o)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateStr = dateFormat.format(date);
		System.out.println(dateStr + "  " + o.toString());
	}
	
	public static void main(String[] args)
	{
		xmlRpcServer = new XmlRpcServer();
		xmlRpcServer.start();
		//String src = FileWorks.readFile("E:\\A-alt.cpp");
		//submitSolution("123", "NEERC-2000", "A", "GCC342", src);
		//log(getUserTaskResults(4, 2)[0].toHashMap().toString());
	}
	
}
