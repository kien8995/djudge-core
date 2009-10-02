package db;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.text.StyledEditorKit.BoldAction;

import djudge.acmcontester.admin.AdminClient;
import djudge.acmcontester.server.ContestServer;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.MonitorRow;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.ProblemStatus;
import djudge.acmcontester.structures.UserData;
import djudge.acmcontester.structures.UserProblemStatus;

public class MonitorModel
{	
	public MonitorData getMonitor(long contestTime)
	{
		UsersDataModel udm = new UsersDataModel();
		udm.updateData();
		return getMonitor(contestTime, udm.getRows().toArray(new UserData[0]));
	}
	
	public MonitorData getMonitorIOI(long contestTime)
	{
		UsersDataModel udm = new UsersDataModel();
		udm.updateData();
		return getMonitorIOI(contestTime, udm.getRows().toArray(new UserData[0]));
	}
	
	private UserProblemStatus getUserProblemStatistics(long contestTime, String userID, String problemID)
	{
		UserProblemStatus res = new UserProblemStatus();
		String sqlFirstAc = "SELECT * FROM `submissions` WHERE `contest_time` <= '" + contestTime + "' AND `user_id` = '" + userID + "' AND `problem_id` = '" + problemID + "' AND djudge_flag > 0 AND judgement = 'AC' ORDER BY id asc LIMIT 1";
		Connection conn = Settings.getConnection();
		Statement st;
		synchronized (AbstractTableDataModel.dbMutex)
		{
			try
			{
				String firstAcId = null;
				int acTime = -1;
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sqlFirstAc);
				if (rs.next())
				{
					firstAcId = rs.getString("id");
					acTime = rs.getInt("contest_time") / 60 / 1000;
				}
				else
				{
					firstAcId = "10000000";
				}
				rs.close();
				st.close();
				String allBeforeAc = "SELECT * FROM `submissions` WHERE `id` < '" + firstAcId + "' AND `user_id` = '" + userID + "' AND `problem_id` = '" + problemID + "' AND djudge_flag > 0 AND id < '" + firstAcId + "' AND contest_time <= " + contestTime  + " AND judgement != 'CE' ORDER BY id ASC";
				st = conn.createStatement();
				rs = st.executeQuery(allBeforeAc);
				int waCnt = 0;
				int lastWaTime = -1;
				while (rs.next())
				{
					waCnt++;
					lastWaTime = rs.getInt("contest_time") / 60 / 1000;
				}
				rs.close();
				st.close();
				if (acTime >= 0)
				{
					res.wasSolved = true;
					res.penaltyTime = acTime + waCnt * 20; 
					res.lastSubmitTime = acTime;
					res.isPending = false;
				}
				else
				{
					res.wasSolved = false;
					res.lastSubmitTime = lastWaTime;
					res.penaltyTime = 0;
					String pending = "SELECT id FROM `submissions` WHERE `user_id` = '" + userID + "' AND `problem_id` = '" + problemID + "' AND djudge_flag <= 0 AND contest_time <= " + contestTime  + " LIMIT 1";
					st = conn.createStatement();
					rs = st.executeQuery(pending);
					if (rs.next())
					{
						System.out.println(rs.getString(1));
						res.isPending = true;
					}
					else
					{
						res.isPending = false;
					}
					rs.close();
					st.close();
				}
				res.wrongTryes = waCnt;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		return res;
	}
	
	private boolean getUserProblemStatusPending(long contestTime, String userID, String problemID)
	{
		Connection conn = Settings.getConnection();
		Statement st;
		ResultSet rs;
		boolean res = false;
		synchronized (AbstractTableDataModel.dbMutex)
		{
			try
			{
				String sqtPending = "SELECT id FROM `submissions` WHERE `user_id` = '" + userID + "' AND `problem_id` = '" + problemID + "' AND djudge_flag <= 0 AND contest_time <= " + contestTime  + " LIMIT 1";
				st = conn.createStatement();
				rs = st.executeQuery(sqtPending);
				if (rs.next())
				{
					res = true;
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
	
	private UserProblemStatus getUserProblemStatisticsIOI(long contestTime, String userID, String problemID)
	{
		UserProblemStatus res = new UserProblemStatus();
		Statement st;
		Connection conn = Settings.getConnection();
		ResultSet rs;
		synchronized (AbstractTableDataModel.dbMutex)
		{
			try
			{
				// searching for max score
				String sqlMaxScore = "SELECT * FROM `submissions` WHERE `contest_time` <= '" + contestTime + "' AND `user_id` = '" + userID + "' AND `problem_id` = '" + problemID + "' AND djudge_flag > 0 ORDER BY score DESC, contest_time ASC LIMIT 1";
				st = conn.createStatement();
				rs = st.executeQuery(sqlMaxScore);
				// some score present
				if (rs.next())
				{
					int maxScore = rs.getInt("score");
					long maxScoreTime = rs.getLong("contest_time");
					String judgement = rs.getString("judgement");
					res.lastSubmitTime = maxScoreTime / 60 / 1000;
					res.score = maxScore;
					if ("AC".equalsIgnoreCase(judgement))
					{
						res.fFullScore = true;
						res.wasSolved = true;
					}
					else
					{
						res.wrongTryes = 1;
					}
					rs.close();
					st.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return res;
	}
	
	private MonitorData getMonitor(long contestTime, UserData[] users)
	{
		MonitorData md = new MonitorData();
		md.contestTime = contestTime;
		md.rows = new MonitorRow[users.length];
		ProblemData[] pr = ContestServer.getCore().getProblemsModel().getRows().toArray(new ProblemData[0]);
		md.problemData = new ProblemStatus[pr.length];
		for (int i = 0; i < pr.length; i++)
		{
			md.problemData[i] = new ProblemStatus(pr[i].sid);
		}
		for (int i = 0; i < users.length; i++)
		{
			MonitorRow mr = new MonitorRow();
			mr.userID = users[i].id;
			mr.username = users[i].username;
			mr.problemData = new UserProblemStatus[pr.length];
			for (int ipr = 0; ipr < pr.length; ipr++)
			{
				UserProblemStatus stat = getUserProblemStatistics(contestTime, users[i].id, pr[ipr].id);
				mr.problemData[ipr] = stat;
				if (stat.wasSolved)
				{
					mr.totalAttempts++;
					mr.totalSolved++;
					mr.totalTime += stat.penaltyTime;
					mr.totalScoredAttempts += stat.wrongTryes + 1;
				}
				mr.totalAttempts += stat.wrongTryes;
				md.problemData[ipr].addUser(stat);
			}
			md.rows[i] = mr;
		}
		for (int i = 0; i < pr.length; i++)
		{
			md.totalAC += md.problemData[i].totalACCount;
			md.totalSubmitted += md.problemData[i].totalSubmissionsCount;
		}
		Arrays.sort(md.rows);
		int place = 0;
		for (int i = 0; i < md.rows.length; i++)
		{
			if (i == 0 || md.rows[i].compareTo(md.rows[i-1]) != 0)
				place++;
			md.rows[i].place = place;
		}
		return md;
	}
	
	class IOIComparatorScoreOnly implements Comparator<MonitorRow>
	{
		@Override
		public int compare(MonitorRow a, MonitorRow t)
		{
			return a.totalScore - t.totalScore;
		}		
	}
	
	private MonitorData getMonitorIOI(long contestTime, UserData[] users)
	{
		MonitorData md = new MonitorData();
		md.contestTime = contestTime;
		md.rows = new MonitorRow[users.length];
		ProblemData[] pr = ContestServer.getCore().getProblemsModel().getRows().toArray(new ProblemData[0]);
		md.problemData = new ProblemStatus[pr.length];
		for (int i = 0; i < pr.length; i++)
		{
			md.problemData[i] = new ProblemStatus(pr[i].sid);
		}
		for (int i = 0; i < users.length; i++)
		{
			MonitorRow mr = new MonitorRow();
			mr.userID = users[i].id;
			mr.username = users[i].username;
			mr.problemData = new UserProblemStatus[pr.length];
			for (int ipr = 0; ipr < pr.length; ipr++)
			{
				UserProblemStatus stat = getUserProblemStatisticsIOI(contestTime, users[i].id, pr[ipr].id);
				if (!stat.wasSolved)
					stat.isPending = getUserProblemStatusPending(contestTime, users[i].id, pr[ipr].id);
				mr.problemData[ipr] = stat;
				if (stat.wasSolved)
				{
					mr.totalAttempts++;
					mr.totalSolved++;
					mr.totalTime += stat.penaltyTime;
					mr.totalScoredAttempts += stat.wrongTryes + 1;
				}
				mr.totalScore += stat.score;
				mr.totalAttempts += stat.wrongTryes;
				md.problemData[ipr].addUser(stat);
			}
			md.rows[i] = mr;
		}
		for (int i = 0; i < pr.length; i++)
		{
			md.totalAC += md.problemData[i].totalACCount;
			md.totalSubmitted += md.problemData[i].totalSubmissionsCount;
		}
		Arrays.sort(md.rows, new IOIComparatorScoreOnly());
		int place = 0;
		for (int i = 0; i < md.rows.length; i++)
		{
			if (i == 0 || md.rows[i].compareTo(md.rows[i-1]) != 0)
				place++;
			md.rows[i].place = place;
		}
		return md;
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}
}
