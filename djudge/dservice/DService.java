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

import org.apache.log4j.Logger;

import djudge.acmcontester.server.ContestCore;
import djudge.filesystem.RemoteFS;
import djudge.utils.SimpleHttpServer;
import djudge.utils.SimpleHttpServerDataProvider;

public class DService
{
	private static final Logger log = Logger.getLogger(DService.class);
	
	static Connection con;
	
	static XmlRpcServer xmlRpcServer;
	
	static String serviceName = "dservice";
	
	static final String dbMutex = "mutex";
	
	static final String databasePath = "data/" + serviceName + "/dservice.db3";

	private static SimpleHttpServer simpleHttpServer;
	
	static
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
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
		final String chars = "123254xyz";
		
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < 24; i++)
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
			log.error("Database error", e);
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
    			log.error("Database error", e);
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
				log.error("Database error", e);
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
			log.error("Database error", e);
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
				log.error("Database error", e);
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
				log.info("Executing SQL request:" + sql);
				st.executeUpdate(sql);
				st.close();
			}
			catch (Exception ex)
			{
				log.error("Database error", ex);
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
		log.info("" + formatNumber(task.id) + " was sent");
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
			log.error("General error", ex);
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
			log.error("General error", e);
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
		catch (Exception ex)
		{
			log.error("General error", ex);
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
			catch (Exception ex)
			{
				log.error("General error", ex);
			}
		}
		for (int i = 0; i < resSql.size(); i++)
		{
			executeSql(resSql.get(i));
		}
		return res.toArray(new DServiceTaskResult[0]);
	}
	
	public static void main(String[] args)
	{
		xmlRpcServer = new XmlRpcServer();
		xmlRpcServer.start();		
		
		new SimpleHttpServer(new Temp(), 5555);
	}

	public static String getHtmlPage(String query)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<body><h1 align='center'>DService status</h1>");

		sb.append("<table border='1'>");
		sb.append("<tr>");
		sb.append("<th>ID</th>");
		sb.append("<th>Date</th>");
		sb.append("<th>UserID</th>");
		sb.append("<th>Problem</th>");
		sb.append("<th>Contest</th>");
		sb.append("<th>Language</th>");
		sb.append("<th>Judgement</th>");
		sb.append("<th>JudgeStatus</th>");
		sb.append("<th>Fetched</th>");
		sb.append("<th>ClientData</th>");
		sb.append("<th>JudgeID</th>");
		sb.append("</tr>\n");
		
		synchronized (dbMutex)
		{
			try
			{
				Statement st = getStatement();
				String sql = "SELECT * FROM submissions ORDER BY id DESC";
				ResultSet rs = st.executeQuery(sql);
				while (rs.next())
				{
					sb.append("<tr>");
					sb.append("<th>" + rs.getString("id") + "</th>");
					sb.append("<th>" + rs.getString("date") + "</th>");
					sb.append("<th>" + rs.getString("user_id") + "</th>");
					sb.append("<th>" + rs.getString("problem") + "</th>");
					sb.append("<th>" + rs.getString("contest") + "</th>");
					sb.append("<th>" + rs.getString("language") + "</th>");
					sb.append("<th>" + rs.getString("judgement") + "</th>");
					sb.append("<th>" + rs.getString("judge_status") + "</th>");
					sb.append("<th>" + rs.getString("fetched") + "</th>");
					sb.append("<th>" + rs.getString("client_data") + "</th>");
					sb.append("<th>" + rs.getString("judge_id") + "</th>");
					sb.append("</tr>\n");
				}
				rs.close();
				st.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error("General error", ex);
			}
		}
		
		sb.append("</table>");
		sb.append("</body>");
		
		//String res = "<body></body>";
		return sb.toString();
	}
}

class Temp implements SimpleHttpServerDataProvider
{

	@Override
	public String getHtmlPage(String query)
	{
		return DService.getHtmlPage(query);
	}
}

