/* $Id$ */

package djudge.dservice;

import java.util.HashMap;

import djudge.utils.xmlrpc.HashMapSerializable;

public class DServiceTaskResult extends HashMapSerializable
{
	int id;
	String contestId;
	String problemId;
	String languageId;
	String xml;
	String dateTime;
	String clientData;
	String judgement;
	
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
		clientData = task.clientData;
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
	
	public String getClientData()
	{
		return clientData;
	}
	
	public String getDateTime()
	{
		return dateTime;
	}
	
	public String getJudgement()
	{
		return judgement;
	}
	
	public DServiceTaskResult(HashMap<String, String> map)
	{
		fromHashMap(map);
	}
	
	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("id", "" + id);
		res.put("contest", contestId);
		res.put("problem", problemId);
		res.put("language", languageId);
		res.put("xml", xml);
		res.put("time", dateTime);
		res.put("clientData", clientData);
		res.put("judgement", judgement);
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map)
	{
		id = Integer.parseInt((String) map.get("id"));
		contestId = (String) map.get("contest");
		problemId = (String) map.get("problem");
		languageId = (String) map.get("language");
		xml = (String) map.get("xml");		
		dateTime = (String) map.get("time");
		clientData = (String) map.get("clientData");
		judgement = (String) map.get("judgement");
	}
}
