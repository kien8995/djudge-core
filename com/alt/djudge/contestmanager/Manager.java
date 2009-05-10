package com.alt.djudge.contestmanager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.alt.djudge.dservice.DServiceTaskResult;
import com.alt.utils.FileWorks;

public class Manager
{
	protected static HashMap<String, Contest> map = new HashMap<String, Contest>();
	
	protected static DServiceClientConnector judgeConnector = new DServiceClientConnector();

	public static void setUpInterfaces()
	{
		judgeConnector.start();
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
	
	public static boolean submitSolutionInternal(String clientData, String judgeContest, 
			String judgeProblem, String judgeLanguage, String source)
	{
		LocalTaskDescription desc = new LocalTaskDescription();
		desc.clientData = clientData;
		desc.language = judgeLanguage;
		desc.problem = judgeProblem;
		desc.contest = judgeContest;
		desc.source = source;
		return judgeConnector.addTask(desc);
	}
	
	public static void updateSubmission(DServiceTaskResult res)
	{
		String data[] = res.getClientData().split(" ");
		String contestId = data[0];
		String submissionId = data[1];
		Contest contest = getContest(contestId);
		if (contest == null) return;
		contest.addResult(submissionId, res);
	}

	public static void main(String[] argv)
	{
		setUpInterfaces();
		loadContests();
		String source = FileWorks.readFile("E:/A-alt.cpp");
		submitSolution("alt", "123", "default", "A", "GCC342", source);
		
	}
	
	
}
