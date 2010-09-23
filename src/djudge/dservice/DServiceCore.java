package djudge.dservice;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import djudge.dservice.interfaces.DServiceNativeInterface;
import djudge.filesystem.RemoteFS;

public class DServiceCore extends DServiceDBLayer implements DServiceNativeInterface
{
	private static final Logger log = Logger.getLogger(DServiceCore.class);
	
	private static final String dataPath = "data/dservice";
	
	/*
	public static String createUser(String username, String password)
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
	}*/

	public DServiceTaskResult[] getSubmissionResult(String uid, int count)
	{
		int userID = getUserID(uid);
		if (userID <= 0)
			return null;
		return getUserTaskResults(userID, count);
	}
	
	public DServiceTaskResult[] fetchResults(String uid)
	{
		int userID = getUserID(uid);
		if (userID <= 0)
			return null;
		return getUserTaskResults2(userID);
	}
	
	@Override
	public int submitSolution(String uid, String contestId, String problemId, 
			String languageId, String source, String clientData, String paramsXML)
	{
		int userID = getUserID(uid);
		if (userID <= 0)
			return -DServiceResult.AUTHORIZATION_FAILED.ordinal();
		
		log.info("Submission " + contestId + "-" + problemId + " " + languageId + " [" + clientData + "]  received from '" + uid + "'");
		log.info(paramsXML);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateStr = dateFormat.format(date);
		
		String sql = "INSERT INTO submissions(user_id, judgement, judge_id, judge_status, contest, problem, language, date, fetched, client_data, check_params)" +
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
				"'" + clientData + "'," +
				"'" + paramsXML +  "'" + 
				")";
				
		executeSql(sql);
		int id = getLastSubmissionID();
		RemoteFS.writeContent(source, dataPath + "/sources/" + formatNumber(id) + ".txt");
		
		return id;
	}
	
	private String formatNumber(int number)
	{
		String res = "" + number;
		while (res.length() < 7)
			res = "0" + res;
		return res;
	}
	
	public DServiceTask getTask(int judgeID)
	{
		DServiceTask task = getTaskInternal(judgeID);
		if (task == null)
			return null;
		task.source = RemoteFS.readContent(dataPath + "/sources/" + formatNumber(task.id) + ".txt");
		log.info("" + formatNumber(task.id) + " was sent");
		return task;
	}
	
	public boolean setTaskResult(int taskID, String judgement, String xmlData)
	{
		RemoteFS.writeContent(xmlData, dataPath + "/sources/" + formatNumber(taskID) + ".xml");
		String sql = "UPDATE submissions SET judgement = '" + judgement + "', judge_status = 1 WHERE id = " + taskID;
		executeSql(sql);
		return true;
	}
	
	private DServiceTaskResult toTaskResult(ResultSet rs)
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
    		res.xml = RemoteFS.readContent(dataPath + "/sources/" + formatNumber(submissionId) + ".xml");
		}
		catch (Exception ex)
		{
			log.error("General error", ex);
			return null;
		}
		return res;
	}
	
	@SuppressWarnings("unused")
	private DServiceTaskResult getTaskResult(int submissionId)
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
	
	private DServiceTaskResult[] getUserTaskResults(int userId, int count)
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
	
	private DServiceTaskResult[] getUserTaskResults2(int userId)
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
	
	public String getHtmlPage(String query)
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
		sb.append("<th>Details</th>");
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
					sb.append("<th>" + rs.getString("check_params") + "</th>");
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
