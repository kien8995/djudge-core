package db;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import djudge.acmcontester.admin.AdminClient;
import djudge.acmcontester.server.ContestServer;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.MonitorRow;
import djudge.acmcontester.structures.ProblemData;
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
				//System.out.println(sqlFirstAc);
				ResultSet rs = st.executeQuery(sqlFirstAc);
				if (rs.next())
				{
					firstAcId = rs.getString("id");
					acTime = rs.getInt("contest_time");
					System.out.println(firstAcId + " " + acTime);
				}
				else
				{
					firstAcId = "1000000";
				}
				rs.close();
				st.close();
				String allBeforeAc = "SELECT * FROM `submissions` WHERE `id` < '" + firstAcId + "' AND `user_id` = '" + userID + "' AND `problem_id` = '" + problemID + "' AND djudge_flag > 0 AND id < '" + firstAcId + "' AND contest_time <= " + contestTime  + " AND judgement != 'CE' ORDER BY id ASC";
				//System.out.println(allBeforeAc);
				st = conn.createStatement();
				rs = st.executeQuery(allBeforeAc);
				int waCnt = 0;
				int lastWaTime = -1;
				while (rs.next())
				{
					waCnt++;
					lastWaTime = rs.getInt("contest_time");
				}
				rs.close();
				st.close();
				if (acTime >= 0)
				{
					res.wasSolved = true;
					res.penaltyTime = acTime + waCnt * 20 * 1000 * 60; 
					res.lastSubmitTime = acTime;
					res.isPending = false;
				}
				else
				{
					res.wasSolved = false;
					res.lastSubmitTime = lastWaTime;
					res.penaltyTime = 0;
					String pending = "SELECT id FROM `submissions` WHERE `user_id` = '" + userID + "' AND `problem_id` = '" + problemID + "' AND djudge_flag <= 0 AND contest_time <= " + contestTime  + " LIMIT 1";
					//System.out.println(pending);
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
		//System.out.println("User: " + userID + ", Problem: " + problemID + ": " + res.toString());
		return res;
	}
	
	private MonitorData getMonitor(long contestTime, UserData[] users)
	{
		MonitorData md = new MonitorData();
		md.rows = new MonitorRow[users.length];
		ProblemData[] pr = ContestServer.getCore().getProblemsModel().getRows().toArray(new ProblemData[0]);
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
					mr.totalSolved++;
					mr.totalTime += stat.penaltyTime;
				}
			}
			md.rows[i] = mr;
		}
		return md; 
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}
}
