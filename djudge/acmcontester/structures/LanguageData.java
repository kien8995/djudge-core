package djudge.acmcontester.structures;

import java.util.HashMap;

import djudge.common.HashMapSerializable;

public class LanguageData extends HashMapSerializable
{
	public String id;
	public String sid;
	public String shortName;
	public String fullName;
	public String compilationCommand;
	public String djudgeID;
	
	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("id", id);
		res.put("sid", sid);
		res.put("short-name", shortName);
		res.put("full-name", fullName);
		res.put("compilation-command", compilationCommand);
		res.put("djudge-id", djudgeID);
		return res;
	}
	
	public LanguageData()
	{
		// TODO Auto-generated constructor stub
	}

	public LanguageData(HashMap<String, String> map)
	{
		fromHashMap(map);
	}
	
	@Override
	public void fromHashMap(HashMap<String, String> map)
	{
		id = map.get("id");
		sid = map.get("sid");
		fullName = map.get("full-name");
		shortName = map.get("short-name");
		compilationCommand = map.get("compilation-command");
		djudgeID = map.get("djudge-id");
	}
	
	@Override
	public String toString()
	{
		return sid + "(" + fullName +  ")";
	}
}
