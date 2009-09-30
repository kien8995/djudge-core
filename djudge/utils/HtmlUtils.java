package djudge.utils;

import java.util.Arrays;
import java.util.Properties;

import djudge.acmcontester.server.http.HttpServer;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserProblemStatus;

public class HtmlUtils
{
	private static void printHeader(StringBuilder sb, String title)
	{
		sb.append("<html><head><title>" + title + "</title></head><body>");
	}
	
	private static void printFooter(StringBuilder sb)
	{
		sb.append("<p>Created by <A HREF='http://www.ecs.csus.edu/pc2/'>CSUS PC^2 8.7 20051115 04</A><br/>" +
				"<A HREF='http://www.ecs.csus.edu/pc2/'>http://www.ecs.csus.edu/pc2/</A><br/>" +
				"Last updated Wed Sep 30 16:00:22 EEST 2009</p></body></html>\n");
	}
	
	public static String getSumatt(MonitorData data, boolean colored)
	{
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Summary/Attempt - Contest");
		sb.append("<table border='1'>\n");
		sb.append("<tr><th><strong><u>Name</u></strong></th>");
		appendPC2ProblemsHeader(sb, data);
		sb.append("<th>Total att/solv</th></tr>\n");
		for (int i = 0; i < data.rows.length; i++)
		{
			int totalSolved = 0, totalAtt = 0;
			sb.append("<tr>");
			sb.append("<td>" + data.rows[i].username + "</td>");
			for (int j = 0; j < data.rows[i].problemData.length; j++)
			{
				String style = "";
				UserProblemStatus ups = data.rows[i].problemData[j];
				if (ups.wasSolved)
					totalSolved++;
				String flag = ups.wasSolved ? "Y" : "N";
				int tries = ups.wrongTryes + (ups.wasSolved ? 1 : 0);
				totalAtt += tries;
				if (ups.wasSolved) style = " bgcolor='#aaffaa'";
				else if (ups.wrongTryes > 0) style = " bgcolor='#ffaaaa'";
				sb.append("<td" + (colored ? style : "") + ">" + tries + "/" + flag + "</td>");
			}
			sb.append("<td>" + totalAtt + "/" + totalSolved + "</td>");
			sb.append("</tr>\n");
		}
		// Summary
		sb.append("<tr><td cellwith=\"" + (data.problemData.length + 2) + "\"></td></tr>");
		sb.append("<tr><td>Submitted/1st Yes/Total Yes</td>");
		appendPC2ProblemsSummary(sb, data);
		sb.append("</table>\n");
		printFooter(sb);
		return sb.toString();
	}
	
	private static void appendPC2ProblemsHeader(StringBuilder sb, MonitorData data)
	{
		for (int i = 0; i < data.rows[0].problemData.length; i++)
		{
			sb.append("<th>&#160;&#160;&#160;&#160;<strong><u>");
			sb.append(data.problemData[i].problemSid);
			sb.append("</u></strong>&#160;&#160;&#160;&#160;</th>");
		}
	}
	
	private static void appendPC2ProblemsSummary(StringBuilder sb, MonitorData data)
	{
		// Summary
		for (int i = 0; i < data.problemData.length; i++)
		{
			int submitted = data.problemData[i].totalSubmissionsCount;
			String firstAC = data.problemData[i].firstACTime >= 0 ? "" + (data.problemData[i].firstACTime) : "--";
			int acCount = data.problemData[i].totalACCount;
			sb.append("<td>" + submitted + "/" + firstAC + "/" + acCount + "</td>");
		}
		sb.append("<td>" + data.totalSubmitted + "/" + data.totalAC + "</td><tr>\n");
	}
	
	public static String getSummary(MonitorData data, boolean colored)
	{
		Arrays.sort(data.rows);
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Summary - Contest");
		sb.append("<table border=\"0\">\n");
		sb.append("<tr><th><strong><u>Rank</u></strong></th>" +
				"<th><strong><u>Name</u></strong></th>" +
				"<th><strong><u>Solved</u></strong></th>" +
				"<th><strong><u>Time</u></strong></th>");
		appendPC2ProblemsHeader(sb, data);
		sb.append("<th>Total att/solv</th></tr>\n");
		int currentRank = 0;
		for (int i = 0; i < data.rows.length; i++)
		{
			sb.append("<tr>");
			if (i == 0 || data.rows[i].compareTo(data.rows[i-1]) != 0)
				currentRank++;
			sb.append("<td>" + currentRank + "</td>");
			sb.append("<td>" + data.rows[i].username + "</td>");
			sb.append("<td>" + data.rows[i].totalSolved + "</td>");
			sb.append("<td>" + data.rows[i].totalTime + "</td>");
			for (int j = 0; j < data.rows[i].problemData.length; j++)
			{
				String style = "";
				UserProblemStatus ups = data.rows[i].problemData[j];
				int tries = ups.wrongTryes + (ups.wasSolved ? 1 : 0);
				String time = ups.wasSolved ? "" + (data.rows[i].problemData[j].lastSubmitTime) : "--";
				if (ups.wasSolved) style = " bgcolor='#aaffaa'";
				else if (ups.wrongTryes > 0) style = " bgcolor='#ffaaaa'";
				sb.append("<td" + (colored ? style : "") + ">" + tries + "/" + time + "</td>");
			}
			sb.append("<td>" + data.rows[i].totalAttempts + "/" + data.rows[i].totalSolved + "</td>");
			sb.append("</tr>\n");
		}
		sb.append("<tr><td cellwith=\"" + (data.problemData.length + 5) + "\"></td></tr>");
		sb.append("<tr><td></td><td>Submitted/1st Yes/Total Yes</td><td></td><td></td>");
		appendPC2ProblemsSummary(sb, data);
		sb.append("</table>\n");
		printFooter(sb);
		return sb.toString();
	}
	
	public static String getMonitor(MonitorData data, Properties params)
	{
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Monitor");
		sb.append("<table border='1'>\n");
		sb.append("<tr><th><strong><u>Name</u></strong></th>");
		for (int i = 0; i < data.rows[0].problemData.length; i++)
		{
			sb.append("<th>&#160;&#160;&#160;&#160;<strong><u>");
			sb.append(data.problemData[i].problemSid);
			sb.append("</u></strong>&#160;&#160;&#160;&#160;</th>");
		}
		sb.append("<th>Total att/solv</th></tr>\n");
		for (int i = 0; i < data.rows.length; i++)
		{
			int totalSolved = 0, totalAtt = 0;
			sb.append("<tr>");
			sb.append("<td>" + data.rows[i].username + " ["+ data.rows[i].userID + "] </td>");
			for (int j = 0; j < data.rows[i].problemData.length; j++)
			{
				String style = "";
				UserProblemStatus ups = data.rows[i].problemData[j];
				if (ups.wasSolved)
					totalSolved++;
				String flag = ups.wasSolved ? "Y" : "N";
				int tries = ups.wrongTryes + (ups.wasSolved ? 1 : 0);
				totalAtt += tries;
				if (ups.wasSolved) style = " bgcolor='#aaffaa'";
				else if (ups.wrongTryes > 0) style = " bgcolor='#ffaaaa'";
				sb.append("<td" + (params.contains("color") ? style : "") + ">" + tries + "/" + flag + "</td>");
			}
			sb.append("<td>" + totalSolved + "/" + totalAtt + "</td>");
			sb.append("</tr>\n");
		}
		// Summary
		sb.append("<tr><td cellwith=\"5\"></td></tr>");
		sb.append("<tr><td>Submitted/1st Yes/Total Yes</td>");
		for (int i = 0; i < data.problemData.length; i++)
		{
			int submitted = data.problemData[i].totalSubmissionsCount;
			String firstAC = data.problemData[i].firstACTime >= 0 ? "" + (data.problemData[i].firstACTime) : "--";
			int acCount = data.problemData[i].totalACCount;
			sb.append("<td>" + submitted + "/" + firstAC + "/" + acCount + "</td>");
		}
		sb.append("<td>" + data.totalSubmitted + "/" + data.totalAC + "</td><tr>\n");
		sb.append("</table>\n");
		printFooter(sb);
		return sb.toString();
	}
	
	public static String getSubmissions(SubmissionData[] data, Properties params)
	{
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Submissions - Contest");
		sb.append("<table border='1'>\n");
		sb.append("<tr>");
		if (params.containsKey("id"))
		{
			sb.append("<th><strong><u>ID</u></strong></th>");
		}
		if (params.containsKey("contesttime"))
		{
			sb.append("<th><strong><u>ContestTime</u></strong></th>");
		}
		if (params.containsKey("realtime"))
		{
			sb.append("<th><strong><u>RealTime</u></strong></th>");
		}
		if (params.containsKey("problem"))
		{
			sb.append("<th><strong><u>Problem</u></strong></th>");
		}
		if (params.containsKey("user"))
		{
			sb.append("<th><strong><u>User</u></strong></th>");
		}
		if (params.containsKey("language"))
		{
			sb.append("<th><strong><u>Language</u></strong></th>");
		}
		if (params.containsKey("judgement"))
		{
			sb.append("<th><strong><u>Judgement</u></strong></th>");
		}
		if (params.containsKey("time"))
		{
			sb.append("<th><strong><u>Time</u></strong></th>");
		}
		if (params.containsKey("memory"))
		{
			sb.append("<th><strong><u>Memory</u></strong></th>");
		}
		if (params.containsKey("output"))
		{
			sb.append("<th><strong><u>Output</u></strong></th>");
		}
		sb.append("</tr>\n");
		for (int i = 0; i < data.length; i++)
		{
			sb.append("<tr>");
			if (params.containsKey("id"))
			{
				sb.append("<td>" + data[i].id + "</td>");
			}			
			if (params.containsKey("contesttime"))
			{
				sb.append("<td>" + data[i].contestTime + "</td>");
			}
			if (params.containsKey("realtime"))
			{
				sb.append("<td>" + data[i].realTime + "</td>");
			}
			if (params.containsKey("problem"))
			{
				sb.append("<td>" + data[i].problemSid + "</td>");
			}
			if (params.containsKey("user"))
			{
				sb.append("<td>" + data[i].username + "</td>");
			}
			if (params.containsKey("language"))
			{
				sb.append("<td>" + data[i].languageSid + "</td>");
			}
			if (params.containsKey("judgement"))
			{
				sb.append("<td>" + data[i].judgement + "</td>");
			}
			if (params.containsKey("time"))
			{
				sb.append("<td>" + data[i].maxTime + "</td>");
			}
			if (params.containsKey("memory"))
			{
				sb.append("<td>" + data[i].maxMemory + "</td>");
			}
			if (params.containsKey("output"))
			{
				sb.append("<td>" + data[i].maxOutput + "</td>");
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		printFooter(sb);
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		HttpServer.main(args);
	}
}
