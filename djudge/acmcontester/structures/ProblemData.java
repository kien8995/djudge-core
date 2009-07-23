package djudge.acmcontester.structures;

import java.util.HashMap;

import djudge.common.HashMapSerializable;

public class ProblemData extends HashMapSerializable
{
	public String id;
	public String sid;
	public String name;
	
	public ProblemData()
	{
		// TODO Auto-generated constructor stub
	}
	
	public ProblemData(HashMap<String, String> map)
	{
		fromHashMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map)
	{
		id = (String) map.get("id");
		sid = (String) map.get("sid");
		name = (String) map.get("name");
	}

	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("id", id);
		res.put("sid", sid);
		res.put("name", name);
		return res;
	}
	
	@Override
	public String toString()
	{
		return sid + ": " + name;
	}
	
}
