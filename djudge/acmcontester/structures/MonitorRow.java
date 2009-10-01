package djudge.acmcontester.structures;


import java.util.HashMap;

import djudge.utils.xmlrpc.HashMapSerializable;

public class MonitorRow extends HashMapSerializable implements Comparable<MonitorRow>
{
	public String userID;
	public String username;
	
	public int totalSolved;
	public long totalTime;
	public int place;
	public int totalAttempts;
	public int totalScoredAttempts;
	
	public UserProblemStatus[] problemData;

	public MonitorRow()
	{
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public MonitorRow(HashMap hashMap)
	{
		fromHashMap(hashMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map)
	{
		userID = (String) map.get("user-id");
		username = (String) map.get("username");
		totalScoredAttempts = Integer.parseInt((String) map.get("total-scored-attempts"));
		totalAttempts = Integer.parseInt((String) map.get("total-attempts"));
		totalSolved = Integer.parseInt((String) map.get("total-solved"));
		totalTime = Integer.parseInt((String) map.get("total-time"));
		place = Integer.parseInt((String) map.get("place"));
		Object[] data = (Object[]) map.get("data");
		int n = data.length;
		problemData = new UserProblemStatus[n];
		for (int i = 0; i < n; i++)
		{
			problemData[i] = new UserProblemStatus((HashMap) data[i]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> toHashMap()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user-id", userID);
		map.put("username", username);
		map.put("total-attempts", "" + totalAttempts);
		map.put("total-solved", "" + totalSolved);
		map.put("total-time", "" + totalTime);
		map.put("place", "" + place);
		map.put("total-scored-attempts", "" + totalScoredAttempts);
		HashMap[] hm = new HashMap[problemData.length];
		for (int i = 0; i < problemData.length; i++)
		{
			hm[i] = problemData[i].toHashMap();
		}
		map.put("data", hm);
		return map;
	}
	
	@Override
	public int compareTo(MonitorRow t)
	{
		if (t.totalSolved != totalSolved)
			return t.totalSolved - totalSolved;
		if (totalTime != t.totalTime)
		{
			long diff = totalTime - t.totalTime;
			return diff > 0 ? 1 : diff < 0 ? -1 : 0; 
		}
		return 0;
	}
}
