package djudge.acmcontester.structures;

import java.util.HashMap;

import djudge.common.HashMapSerializable;

public class MonitorRow  extends HashMapSerializable
{
	public String userID;
	public String username;
	
	public int totalSolved;
	public long totalTime;
	public int place;
	//public int totalTried;
	
	public UserProblemStatus[] problemData;

	@Override
	public void fromHashMap(HashMap<String, String> map)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user-id", userID);
		map.put("username", username);
		map.put("total-solved", "" + totalSolved);
		map.put("total-time", "" + totalSolved);
		map.put("place", "" + place);
		return map;
	}  
}
