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
	
	public String toString()
	{
		String res = "";
		if (wasSolved)
		{
			res += "+";
			if (wrongTryes > 0)
				res += "" + wrongTryes;
			res += " " + penaltyTime / 1000 / 60;
		}
		else if (wrongTryes > 0)
		{
			res += "-";
			res += "" + wrongTryes + "";
			res += " " + penaltyTime / 1000 / 60;			
		}
		return res;
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}

	@Override
	public void fromHashMap(HashMap<String, String> map)
	{
		wasSolved = Boolean.parseBoolean(map.get("solved"));
		wrongTryes = Integer.parseInt(map.get("wrong-count"));
		lastSubmitTime = Long.parseLong(map.get("last-time"));
		penaltyTime = Long.parseLong(map.get("penalty"));
	}

	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("solved", "" + wasSolved);
		map.put("wrong-count", "" + wrongTryes);
		map.put("last-time", "" + lastSubmitTime);
		map.put("penalty", "" + penaltyTime);
		return map;
	}
}
