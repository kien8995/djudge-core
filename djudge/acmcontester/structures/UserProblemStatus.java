package djudge.acmcontester.structures;

import java.util.HashMap;

import djudge.acmcontester.Admin;
import djudge.common.HashMapSerializable;

public class UserProblemStatus extends HashMapSerializable
{
	public boolean wasSolved;
	
	public int wrongTryes;
	
	public long lastSubmitTime;
	
	public long penaltyTime;
	
	public boolean isPending;
	
	public String toString()
	{
		String res = "";
		if (wasSolved)
		{
			res += "+";
			if (wrongTryes > 0)
				res += "" + wrongTryes;
			res += " [" + (lastSubmitTime / 60000) + "]";  
		}
		else if (wrongTryes > 0)
		{
			res += "-";
			res += "" + wrongTryes + "";
			res += " [" + (lastSubmitTime / 60000) + "]";
		}
		return res;
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
	
	public UserProblemStatus()
	{
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public UserProblemStatus(HashMap map)
	{
		fromHashMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map0)
	{
		HashMap<String, String> map = (HashMap<String, String>) map0;
		wasSolved = Boolean.parseBoolean(map.get("solved"));
		isPending = Boolean.parseBoolean(map.get("pending"));
		wrongTryes = Integer.parseInt(map.get("wrong-count"));
		lastSubmitTime = Long.parseLong(map.get("last-time"));
		penaltyTime = Long.parseLong(map.get("penalty"));
	}

	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("solved", "" + wasSolved);
		map.put("pending", "" + isPending);
		map.put("wrong-count", "" + wrongTryes);
		map.put("last-time", "" + lastSubmitTime);
		map.put("penalty", "" + penaltyTime);
		return map;
	}
}
