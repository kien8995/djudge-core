package com.alt.djudge.contestmanager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Manager
{
	protected static HashMap<String, Contest> map = new HashMap<String, Contest>();  

	public static void setUpInterfaces()
	{
		
	}
	
	public static void main(String[] argv)
	{
		setUpInterfaces();
		loadContests();
	}
	
	public static void log(Object o)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateStr = dateFormat.format(date);
		System.out.println(dateStr + "  " + o.toString());
	}
	
	private static void loadContests()
	{
		File[] files = new File("contests/").listFiles();
		for (File file : files)
		{
			if (file.isDirectory())
			{
				String name = file.getName();
				map.put(name, new Contest(name));
			}
		}
	}
	
	public static Contest getContest(String contestSid)
	{
		return map.get(contestSid);
	}
	
	public static boolean startContest(String userSid, String password, String contestSid)
	{
		if (!GlobalUsers.checkUser(userSid, password)) return false;
		Contest contest = getContest(contestSid);
		if (null == contest) return false;
		return contest.startContest(userSid);
	}
	
	public static boolean enterContest(String userSid, String password, String contestSid)
	{
		if (!GlobalUsers.checkUser(userSid, password)) return false;
		Contest contest = getContest(contestSid);
		if (null == contest) return false;
		return contest.enterContest(userSid);
	}
	
	public static boolean submitSolution(String userSid, String password, String contestSid, 
			String problemId, String languageId, String source)
	{
		if (!GlobalUsers.checkUser(userSid, password)) return false;
		Contest contest = getContest(contestSid);
		if (null == contest) return false;
		if (!contest.startContest(userSid)) return false;
		if (!contest.isRunning()) return false;
		return contest.submitSolution(userSid, problemId, languageId, source);
	}
	
	public static boolean submitSolutionInternal(Contest contest, String judgeContest, 
			String judgeProblem, String judgeLanguage, String source)
	{
		return true;
	}
	
}
