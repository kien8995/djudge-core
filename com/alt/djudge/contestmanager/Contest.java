package com.alt.djudge.contestmanager;

import java.util.Date;

import com.alt.djudge.dservice.DServiceTaskResult;

public class Contest
{
	ContestSettings settings;
	
	LocalUsersProvider users;
	LocalProblemsProvider problems;
	LocalSubmissionsProvider submissions;
	
	public boolean enterContest(String userSid)
	{
		if (users.isUserRegistered(userSid)) return true;
		return users.registerUserOnContest(userSid);
	}
	
	public boolean startContest(String userSid)
	{
		if (!enterContest(userSid)) return false;
		return users.startContest(userSid);
	}
	
	public Contest(String contestId)
	{
		settings = new ContestSettings("contests/" + contestId + "/contest.xml");
		users = new LocalUsersXmlProvider(settings);
		problems = new LocalProblemsXmlProvider(settings);
		submissions = new LocalSubmissionsXmlProvider(settings);
	}
	
	public ContestSettings getSettings()
	{
		return settings;
	}
	
	private Date now()
	{
		return new Date();
	}
	
	public boolean hasStarted()
	{
		return !now().before(settings.startTime);
	}
	
	public boolean hasFinished()
	{
		return getContestAbsoluteTime() > settings.duration;
	}
	
	public boolean wasFrozen()
	{
		return getContestAbsoluteTime() >= settings.freezeTime;
	}
	
	public boolean wasUnfrozen()
	{
		return getContestAbsoluteTime() >= settings.unfreezeTime;
	}

	public long getContestAbsoluteTime()
	{
		//FIXME
		long diff = now().getTime() - settings.startTime.getTime();
		System.out.println("Absolute Time: " + diff);
		return diff;
	}
	
	public boolean isPaused()
	{
		return false;
	}
	
	public boolean isRunning()
	{
		return (!isPaused()) && (getContestTime() == getContestAbsoluteTime()); 
	}
	
	public long getContestTime()
	{
		long diff = getContestAbsoluteTime();
		if (diff <= 0) return 0;
		if (diff >= settings.duration) return settings.duration;
		System.out.println("Time: " + diff);
		return diff;
	}
	
	public boolean addResult(String submissionId, DServiceTaskResult res)
	{
		submissions.setSubmissionResult(submissionId, res.getJudgement(), res.getXml());
		return true;
	}

	public boolean submitSolution(String userSid, String problemSid, 
			String languageId, String source)
	{
		LocalProblemDescription pr = problems.getProblem(problemSid);
		if (null == pr) return false;
		String id = submissions.addSubmission(userSid, problemSid, languageId, source, getContestTime());
		String clientData = settings.id + " " + id;
		return Manager.submitSolutionInternal(clientData, pr.judgeContest, pr.judgeProblem, languageId, source);
	}
}
