/* $Id$ */

package djudge.utils;

import java.util.Properties;

import utils.PrintfFormat;

import djudge.acmcontester.server.http.HttpServer;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserProblemStatusACM;
import djudge.acmcontester.structures.UserProblemStatusIOI;

public class HtmlUtils
{
	private static void printHeader(StringBuilder sb, String title, Properties params)
	{
		sb.append("<html><head>");
		sb.append("<title>" + title + "</title>");
		if (params.containsKey("refresh"))
		{
			sb.append("<meta http-equiv='Refresh' content='" + params.getProperty("refresh") +"'>");
		}
		sb.append("</head><body>");
	}
	
	private static void printFooterPC2(StringBuilder sb)
	{
		sb.append("<p>Created by <A HREF='http://www.ecs.csus.edu/pc2/'>CSUS PC^2 8.7 20051115 04</A><br/>" +
				"<A HREF='http://www.ecs.csus.edu/pc2/'>http://www.ecs.csus.edu/pc2/</A><br/>" +
				"Last updated Wed Sep 30 16:00:22 EEST 2009</p></body></html>\n");
	}
	
	public static String getSumatt(MonitorData data, boolean colored)
	{
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Summary/Attempt - Contest", new Properties());
		sb.append("<table border='1'>\n");
		sb.append("<tr><th><strong><u>Name</u></strong></th>");
		appendPC2ProblemsHeader(sb, data);
		sb.append("<th>Total att/solv</th></tr>\n");
		for (int i = 0; i < data.teams.length; i++)
		{
			int totalSolved = 0, totalAtt = 0;
			sb.append("<tr>");
			sb.append("<td>" + data.teams[i].username + "</td>");
			for (int j = 0; j < data.teams[i].acmData.length; j++)
			{
				String style = "";
				UserProblemStatusACM ups = data.teams[i].acmData[j];
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
		sb.append("<tr><td cellwith=\"" + (data.problems.length + 2) + "\"></td></tr>");
		sb.append("<tr><td>Submitted/1st Yes/Total Yes</td>");
		appendPC2ProblemsSummary(sb, data);
		sb.append("</table>\n");
		printFooterPC2(sb);
		return sb.toString();
	}
	
	private static void appendPC2ProblemsHeader(StringBuilder sb, MonitorData data)
	{
		for (int i = 0; i < data.teams[0].acmData.length; i++)
		{
			sb.append("<th>&#160;&#160;&#160;&#160;<strong><u>");
			sb.append(data.problems[i].problemSid);
			sb.append("</u></strong>&#160;&#160;&#160;&#160;</th>");
		}
	}
	
	private static void appendPC2ProblemsSummary(StringBuilder sb, MonitorData data)
	{
		// Summary
		for (int i = 0; i < data.problems.length; i++)
		{
			int submitted = data.problems[i].totalSubmissionsCount;
			String firstAC = data.problems[i].firstACTime >= 0 ? "" + (data.problems[i].firstACTime) : "--";
			int acCount = data.problems[i].totalACCount;
			sb.append("<td>" + submitted + "/" + firstAC + "/" + acCount + "</td>");
		}
		sb.append("<td>" + data.totalSubmitted + "/" + data.totalAC + "</td><tr>\n");
	}
	
	public static String getSummary(MonitorData data, boolean colored)
	{
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Summary - Contest", new Properties());
		sb.append("<table border=\"0\">\n");
		sb.append("<tr><th><strong><u>Rank</u></strong></th>" +
				"<th><strong><u>Name</u></strong></th>" +
				"<th><strong><u>Solved</u></strong></th>" +
				"<th><strong><u>Time</u></strong></th>");
		appendPC2ProblemsHeader(sb, data);
		sb.append("<th>Total att/solv</th></tr>\n");
		for (int i = 0; i < data.teams.length; i++)
		{
			sb.append("<tr>");
			sb.append("<td>" + data.teams[i].place + "</td>");
			sb.append("<td>" + data.teams[i].username + "</td>");
			sb.append("<td>" + data.teams[i].totalSolved + "</td>");
			sb.append("<td>" + data.teams[i].totalTime + "</td>");
			for (int j = 0; j < data.teams[i].acmData.length; j++)
			{
				String style = "";
				UserProblemStatusACM ups = data.teams[i].acmData[j];
				int tries = ups.wrongTryes + (ups.wasSolved ? 1 : 0);
				String time = ups.wasSolved ? "" + (data.teams[i].acmData[j].lastSubmitTime) : "--";
				if (ups.wasSolved) style = " bgcolor='#aaffaa'";
				else if (ups.wrongTryes > 0) style = " bgcolor='#ffaaaa'";
				sb.append("<td" + (colored ? style : "") + ">" + tries + "/" + time + "</td>");
			}
			sb.append("<td>" + data.teams[i].totalAttempts + "/" + data.teams[i].totalSolved + "</td>");
			sb.append("</tr>\n");
		}
		sb.append("<tr><td cellwith=\"" + (data.problems.length + 5) + "\"></td></tr>");
		sb.append("<tr><td></td><td>Submitted/1st Yes/Total Yes</td><td></td><td></td>");
		appendPC2ProblemsSummary(sb, data);
		sb.append("</table>\n");
		printFooterPC2(sb);
		return sb.toString();
	}
	
	private static String getCellBrColor(UserProblemStatusACM ups, Properties params)
	{
		String cellClass = "null";
		boolean flagBg = params.containsKey("bgcolor");
		boolean flagTx = params.containsKey("txcolor");
		if (ups.wasSolved)
		{
			if (flagTx)
				cellClass = "ac_t";
			else if (flagBg)
				cellClass = "ac_b";
		}
		else if (ups.isPending)
		{
			if (flagTx)
				cellClass = "pn_t";
			else if (flagBg)
				cellClass = "pn_b";
		}		
		else if (ups.wrongTryes > 0)
		{
			if (flagTx)
				cellClass = "wa_t";
			else if (flagBg)
				cellClass = "wa_b";
		}
		else
		{
			cellClass = "no";
		}
		return "class=\"" + cellClass + "\"";
	}
	
	private static String getCellBrColorIOI(UserProblemStatusIOI ups, Properties params)
	{
		String cellClass = "null";
		boolean flagBg = params.containsKey("bgcolor");
		boolean flagTx = params.containsKey("txcolor");
		if (ups.isFullScore)
		{
			if (flagTx)
				cellClass = "ac_tf";
			else if (flagBg)
				cellClass = "ac_bf";
		}
		else if (ups.score > 0)
		{
			if (flagTx)
				cellClass = "ac_t";
			else if (flagBg)
				cellClass = "ac_b";
		}
		else if (ups.isPending)
		{
			if (flagTx)
				cellClass = "pn_t";
			else if (flagBg)
				cellClass = "pn_b";
		}
		else if (ups.submissionsTotal > 0)
		{
			if (flagTx)
				cellClass = "wa_t";
			else if (flagBg)
				cellClass = "wa_b";
		}
		else
		{
			cellClass = "no";
		}
		return "class=\"" + cellClass + "\"";
	}
	
	private static String formatTime(long time)
	{
		return new PrintfFormat("%d:%02d").sprintf(new Object[] {time / 60, time % 60});
	}
	
	private static String getCellContentTriesTime(UserProblemStatusACM ups, Properties params)
	{
		String res = "-";
		if (ups.wasSolved)
		{
			res = "+" + (ups.wrongTryes > 0 ? ups.wrongTryes : "") + "<br>" + "<span class=\"time\">" + formatTime(ups.lastSubmitTime) + "</span>";
		}
		else if (ups.wrongTryes > 0)
		{
			res = "-" + ups.wrongTryes + "<br>" + "<span class=\"time\">" + formatTime(ups.lastSubmitTime) + "</span>";
		}
		return res;
	}
	
	private static String getCellContentTries(UserProblemStatusACM ups, Properties params)
	{
		String res = "-";
		if (ups.wasSolved)
		{
			res = "+" + (ups.wrongTryes > 0 ? ups.wrongTryes : "");
		}
		else if (ups.wrongTryes > 0)
		{
			res = "-" + ups.wrongTryes;
		}
		return res;
	}
	
	private static String getCellContentScoreTime(UserProblemStatusIOI ups, Properties params)
	{
		String res = "-";
		if (ups.submissionsTotal > 0)
		{
//			res = (ups.isFullScore ? "<b>" : "") + ups.score + (ups.isFullScore ? "</b>" : "") + "<sub>"+ ups.submissionsTotal +"</sub>"+ "<br>" + "<span class=\"time\" >" + formatTime(ups.lastSubmitTime) + "</span>";
			res = (ups.isFullScore ? "<b>" : "") + ups.score + (ups.isFullScore ? "</b>" : "") + "<br>" + "<span class=\"time\" >" + formatTime(ups.lastSubmitTime) + " [" + ups.submissionsTotal + "]</span>";
		}
		return res;
	}	
	
	private static String getCellContentPC2TT(UserProblemStatusACM ups, Properties params)
	{
		int tries = ups.wrongTryes + (ups.wasSolved ? 1 : 0);
		String time = ups.wasSolved ? "" + (ups.lastSubmitTime) : "--";
		return "" + tries + "/" + time;
	}
	
	private static String getCellContentScore(UserProblemStatusIOI ups, Properties params)
	{
		if (ups.isFullScore)
			return "<b>" + ups.score;// + "</b><sub>("+ ups.submissionsTotal +")</sub>";
		if (ups.submissionsTotal > 0)
			return "" + ups.score;// + "<sub>("+ ups.submissionsTotal +")</sub>";
		return "-";
	}
	
	private static String getCellContentPC2YN(UserProblemStatusACM ups, Properties params)
	{
		String res = "-";
		if (ups.wasSolved)
		{
			res = "" + (ups.wrongTryes + 1) + "/Y";
		}
		else if (ups.wrongTryes > 0)
		{
			res = "" + ups.wrongTryes + "/N";
		}
		return res;
	}
	
	private static String getCellContent(UserProblemStatusACM ups, UserProblemStatusIOI upsIOI, Properties params)
	{
		String contentStyle = params.getProperty("info");
		if ("t".equalsIgnoreCase(contentStyle))
		{
			return getCellContentTries(ups, params);
		}
		else if ("pc2yn".equalsIgnoreCase(contentStyle))
		{
			return getCellContentPC2YN(ups, params);
		}
		else if ("pc2tt".equalsIgnoreCase(contentStyle))
		{
			return getCellContentPC2TT(ups, params);
		}
		else if ("st".equalsIgnoreCase(contentStyle))
		{
			return getCellContentScoreTime(upsIOI, params);
		}
		else if ("s".equalsIgnoreCase(contentStyle))
		{
			return getCellContentScore(upsIOI, params);
		}
		else
		{
			return getCellContentTriesTime(ups, params);
		}
	}
	
	private static void addUserProblemCall(StringBuilder sb, UserProblemStatusACM ups, UserProblemStatusIOI upsIOI, Properties params)
	{
		String cellStyle = getCellBrColor(ups, params);
		String cellContent = getCellContent(ups, upsIOI, params);
		sb.append("<td align='center' " + cellStyle + ">" + cellContent + "</td>");
	}
	
	private static void appendCSS(StringBuilder sb)
	{
		sb.append("\n<style type='text/css'>"
				+ "span.time {font-size: small; }"
				+ "tr.color       { background-color: #EEE } "
				+ "tr  { font-size: 12pt } "
				+ "tr.odd1  { background-color: #FFB } "
				+ "tr.even1 { background-color: #DEF } "
				+ "tr.odd2  { background-color: #c8e8f8 } "
				+ "tr.even2 { background-color: #d0f0ff } "
				+ "tr.odd3   { background-color: #ffffff } "
				+ "tr.even3  { background-color: #f8f8f8 } "
				+ "td.ac_b  { text-align: center; background-color: #aaffaa } "
				+ "td.ac_t  { text-align: center; color: #006600 } "
				+ "td.ac_bf  { text-align: center; background-color: #aaffaa; } "
				+ "td.ac_tf  { text-align: center; color: #0A0; } "
				+ "td.wa_b  { text-align: center; background-color: #ffaaaa } "
				+ "td.wa_t  { text-align: center; color: #ff0000 } "
				+ "td.pn_b  { text-align: center; background-color: #ffff00 } "
				//cccc00
				+ "td.pn_t  { text-align: center; color: #00cccc } "
				+ "td.no    { text-align: center; color: #000000 } "
				+ "</style>\n");		
	}
	
	private static void appendContestHeader(StringBuilder sb, MonitorData data, Properties params)
	{
		sb.append("<h1>" + data.contestName + "</h1>");
		
		sb.append("<table>");
		long time = data.contestTime / 1000;
		String timestr = new PrintfFormat("%02d:%02d:%02d").sprintf(new Object[]{time / 60 / 60, (time / 60)%60, time % 60});
		sb.append("<tr><td>Updated:</td><td>" + timestr + "</td></tr>");
		sb.append("<tr><td>Generation time:</td><td>"+data.lastUpdateTime+"</td></tr>");
		sb.append("</table>");
	}
	
	private static void appendContestFooter(StringBuilder sb, MonitorData data, Properties params)
	{
		sb.append("<p>Generated by <a href='mailto:altdotua@gmail.com?subject=DJudge'>DJudge</a>\n");
		sb.append("&copy; 2008-2009 <a style='color: #aaa000' target='new' href='http://www.topcoder.com/tc?module=MemberProfile&cr=22713875'>alt.ua</a><br />");
		sb.append("</body></html>\n");
	}
	
	public static String getMonitorACM(MonitorData data, Properties params)
	{
		boolean flagEvenOdd = params.containsKey("rowcolor");
		String colorNum = params.getProperty("rowcolor");		
		
		StringBuilder sb = new StringBuilder();
		printHeader(sb, data.contestName + " - Monitor", params);
		appendCSS(sb);
		appendContestHeader(sb, data, params);
		sb.append("<table border='1'>\n");
		sb.append("<tr class=\"" + (flagEvenOdd ? "color" : "none") + "\">");
		
		sb.append("<th><strong>Place</strong></th>");
		sb.append("<th align='center'><strong>Team</strong></th>");
		for (int i = 0; i < data.teams[0].acmData.length; i++)
		{
			sb.append("<th>&#160;&#160;&#160;&#160;<strong><u>");
			sb.append(data.problems[i].problemSid);
			sb.append("</u></strong>&#160;&#160;&#160;&#160;</th>");
		}
		sb.append("<th>Total</th>\n");
		sb.append("<th>Time</th>\n");
		sb.append("<th>Dirt</th>\n");
		
		sb.append("</tr>\n");
		
		for (int i = 0; i < data.teams.length; i++)
		{
			sb.append("<tr class=\"" + (flagEvenOdd ? (i%2==0 ? "even" : "odd") + colorNum : "null") + "\">");
			sb.append("<td align='center'>" + data.teams[i].place + ". </td>");
			sb.append("<td>" + data.teams[i].username + " ["+ data.teams[i].userID + "] </td>");
			for (int j = 0; j < data.teams[i].acmData.length; j++)
			{
				UserProblemStatusACM upsACM = data.teams[i].acmData[j];
				UserProblemStatusIOI upsIOI = data.teams[i].ioiData[j];
				addUserProblemCall(sb, upsACM, upsIOI, params);
			}
			sb.append("<td>" + data.teams[i].totalSolved + "</td>");
			sb.append("<td>" + data.teams[i].totalTime + "</td>");
			sb.append("<td>" + new PrintfFormat("%.0lf%%").sprintf(100.0 * (data.teams[i].totalScoredAttempts - data.teams[i].totalSolved) / Math.max(data.teams[i].totalScoredAttempts, 1)) + "</td>");
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		appendContestFooter(sb, data, params);
		return sb.toString();
	}
	
	
	/*
	 * IOI-monitor related stuff
	 * 
	 */
	
	private static String getCellContentIOI(UserProblemStatusIOI ups, Properties params)
	{
		String contentStyle = params.getProperty("info");
		if ("st".equalsIgnoreCase(contentStyle))
		{
			return getCellContentScoreTime(ups, params);
		}
		else //s
		{
			return getCellContentScore(ups, params);
		}
	}

	private static void addUserProblemCellIOI(StringBuilder sb, UserProblemStatusIOI ups, Properties params)
	{
		String cellStyle = getCellBrColorIOI(ups, params);
		String cellContent = getCellContentIOI(ups, params);
		sb.append("<td align='center' " + cellStyle + ">" + cellContent + "</td>");
	}	
	
	public static String getMonitorIOI(MonitorData data, Properties params)
	{
		boolean flagEvenOdd = params.containsKey("rowcolor");
		String colorNum = params.getProperty("rowcolor");		
		
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Monitor", params);
		appendCSS(sb);
		sb.append("<table border='1'>\n");
		sb.append("<tr class=\"" + (flagEvenOdd ? "color" : "none") + "\">");
		
		sb.append("<th><strong>Place</strong></th>");
		sb.append("<th align='center'><strong>Team</strong></th>");
		for (int i = 0; i < data.teams[0].acmData.length; i++)
		{
			sb.append("<th>&#160;&#160;&#160;&#160;<strong><u>");
			sb.append(data.problems[i].problemSid);
			sb.append("</u></strong>&#160;&#160;&#160;&#160;</th>");
		}
		sb.append("<th>Score</th>\n");
		sb.append("<th>Attempts</th>\n");
		sb.append("</tr>\n");
		
		for (int i = 0; i < data.teams.length; i++)
		{
			sb.append("<tr class=\"" + (flagEvenOdd ? (i%2==0 ? "even" : "odd") + colorNum : "null") + "\">");
			sb.append("<td align='center'>" + data.teams[i].place + ". </td>");
			sb.append("<td>" + data.teams[i].username + " ["+ data.teams[i].userID + "] </td>");
			for (int j = 0; j < data.teams[i].acmData.length; j++)
			{
				UserProblemStatusIOI upsIOI = data.teams[i].ioiData[j];
				addUserProblemCellIOI(sb, upsIOI, params);
			}
			sb.append("<td>" + data.teams[i].totalScore + "</td>");
			sb.append("<td>" + data.teams[i].totalAttempts + "</td>");
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}
	
	public static String getSubmissions(SubmissionData[] data, Properties params)
	{
		StringBuilder sb = new StringBuilder();
		printHeader(sb, "Submissions - Contest", params);
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
		printFooterPC2(sb);
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		HttpServer.main(args);
	}
}
