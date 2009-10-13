package djudge.dservice;

import java.util.HashMap;

public class DServiceTask
{
	int id;
	String contestId;
	String problemId;
	String languageId;
	String source;
	String clientData;
	String params;
	
	public DServiceTask()
	{
		params = clientData = contestId = problemId = languageId = source = "";
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
	
	public String getSource()
	{
		return source;
	}
	
	public String getClientData()
	{
		return clientData;
	}
	
	public DServiceTask(HashMap<String, String> map)
	{
		id = Integer.parseInt(map.get("id"));
		contestId = map.get("contest");
		problemId = map.get("problem");
		languageId = map.get("language");
		source = map.get("source");
		clientData = map.get("clientData");
		params = map.get("param");
	}
	
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("id", "" + id);
		res.put("contest", contestId);
		res.put("problem", problemId);
		res.put("language", languageId);
		res.put("source", source);
		res.put("clientData", clientData);
		res.put("param", params);
		return res;
	}
	
	public String getParams()
	{
		return params;
	}
}
