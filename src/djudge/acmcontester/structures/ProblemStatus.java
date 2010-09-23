/* $Id$ */

package djudge.acmcontester.structures;

import java.util.HashMap;

import djudge.utils.xmlrpc.HashMapSerializable;

public class ProblemStatus extends HashMapSerializable
{
	public String problemSid;
	
	public String problemName;
	
	public int totalSubmissionsCount;
	
	public int totalACCount;
	
	public long firstACTime = -1;

	public ProblemStatus()
	{
		// TODO Auto-generated constructor stub
	}
	
	public ProblemStatus(String problemId)
	{
		problemName = problemSid = problemId;
	}
	
	@SuppressWarnings("unchecked")
	public ProblemStatus(HashMap map)
	{
		fromHashMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map0)
	{
		HashMap<String, String> map = (HashMap<String, String>) map0;
		problemSid = map.get("problem-sid");
		problemName = map.get("problem-name");
		totalSubmissionsCount = Integer.parseInt(map.get("total-count"));
		totalACCount = Integer.parseInt(map.get("ac-count"));
		firstACTime = Long.parseLong(map.get("first-ac-time"));
	}
	
	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("problem-sid", problemSid);
		map.put("problem-name", problemName);
		map.put("total-count", "" + totalSubmissionsCount);
		map.put("ac-count", "" + totalACCount);
		map.put("first-ac-time", "" + firstACTime);
		return map;
	}	
	
	public void addUser(UserProblemStatusACM ups)
	{
		if (ups.wasSolved)
		{
			totalACCount++;
			if (firstACTime == -1 || firstACTime > ups.lastSubmitTime)
				firstACTime = ups.lastSubmitTime;
			totalSubmissionsCount += 1 + ups.wrongTryes;
		}
		else
		{
			totalSubmissionsCount += ups.wrongTryes;
		}
	}
	
}
