package com.alt.djudge.dservice;

import java.util.HashMap;

public class DServiceTaskResult
{
	int id;
	String contestId;
	String problemId;
	String languageId;
	String xml;
	String dateTime;
	
	public DServiceTaskResult()
	{
		// TODO Auto-generated constructor stub
	}
	
	public DServiceTaskResult(DServiceTask task)
	{
		id = task.id;
		contestId = task.contestId;
		problemId = task.problemId;
		languageId = task.languageId;
	}
	
	public int getID()
	{
		return id;
	}
	
	public String getContest()
	{
		return contestId;
	}
	
	public String getProblem()
	{
		return problemId;
	}
	
	public String getLanguage()
	{
		return languageId;
	}
	
	public String getXml()
	{
		return xml;
	}
	
	public String getDateTime()
	{
		return dateTime;
	}
	
	public DServiceTaskResult(HashMap<String, String> map)
	{
		id = Integer.parseInt(map.get("id"));
		contestId = map.get("contest");
		problemId = map.get("problem");
		languageId = map.get("language");
		xml = map.get("xml");		
		dateTime = map.get("time");
	}
	
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("id", "" + id);
		res.put("contest", contestId);
		res.put("problem", problemId);
		res.put("language", languageId);
		res.put("xml", xml);
		res.put("time", dateTime);
		return res;
	}
}
