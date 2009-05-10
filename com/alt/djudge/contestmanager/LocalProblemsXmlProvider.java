package com.alt.djudge.contestmanager;

import java.util.HashMap;

public class LocalProblemsXmlProvider implements LocalProblemsProvider
{
	
	HashMap<String, LocalProblemDescription> map = new HashMap<String, LocalProblemDescription>();
	
	ContestSettings contest;
	
	public boolean loadData()
	{
		return true;
	}
	
	public LocalProblemsXmlProvider(ContestSettings contest)
	{
		this.contest = contest;
		loadData();
	}
	
	@Override
	public boolean saveData()
	{
		return false;
	}

	@Override
	public boolean checkProblem(String problemSid)
	{
		return map.get(problemSid) != null;
	}

	@Override
	public LocalProblemDescription[] getProblems()
	{
		return map.values().toArray(new LocalProblemDescription[0]);
	}

	@Override
	public LocalProblemDescription getProblem(String problemSid)
	{
		return map.get(problemSid);
	}
	
}
