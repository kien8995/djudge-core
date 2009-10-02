package djudge.acmcontester.structures;

import java.util.Date;
import java.util.HashMap;

import djudge.utils.xmlrpc.HashMapSerializable;

public class MonitorData extends HashMapSerializable
{
	/*
	 * User scores
	 */
	public MonitorUserStatus[] rows;
	
	/*
	 * Contest's nbame
	 */
	public String contestName = "";
	
	/*
	 * Monitor last update time (calendar)
	 */
	public Date lastUpdateTime = new Date(0);
	
	/*
	 * Contest time (in ms)
	 */
	public long contestTime = 0;
	
	public int totalAC = 0;
	
	public int totalSubmitted = 0;
	
	/*
	 * Problems IDs
	 */
	public ProblemStatus[] problemData;
	
	public MonitorData()
	{
		rows = new MonitorUserStatus[0];
	}

	@SuppressWarnings("unchecked")
	public MonitorData(HashMap map)
	{
		fromHashMap(map);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void fromHashMap(HashMap map)
	{
		contestName = (String) map.get("contest-name");
		System.out.println(map.get("last-update-time"));
		lastUpdateTime = new Date(Long.parseLong((String) map.get("last-update-time")));
		contestTime = Long.parseLong((String) map.get("contest-time"));
		totalAC = Integer.parseInt((String) map.get("total-ac"));
		totalSubmitted = Integer.parseInt((String) map.get("total-count"));
		Object[] data = (Object[]) map.get("monitor-rows");
		int n = data.length;
		rows = new MonitorUserStatus[n];
		for (int i = 0; i < n; i++)
		{
			rows[i] = new MonitorUserStatus((HashMap) data[i]);
		}
		data = (Object[]) map.get("problems-status");
		n = data.length;
		problemData = new ProblemStatus[n];
		for (int i = 0; i < n; i++)
		{
			problemData[i] = new ProblemStatus((HashMap) data[i]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap toHashMap()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("contest-name", contestName);
		map.put("last-update-time", "" + lastUpdateTime.getTime());
		map.put("contest-time", "" + contestTime);
		map.put("total-ac", "" + totalAC);
		map.put("total-count", "" + totalSubmitted);
		HashMap[] hm = new HashMap[rows.length];
		for (int i = 0; i < rows.length; i++)
		{
			hm[i] = rows[i].toHashMap();
		}
		map.put("monitor-rows", hm);
		hm = new HashMap[problemData.length];
		for (int i = 0; i < problemData.length; i++)
		{
			hm[i] = problemData[i].toHashMap();
		}
		map.put("problems-status", hm);
		return map;
	}
	
	UserProblemStatusACM getUserProblemStatus(String userID, int problemIndex)
	{
		for (int i = 0; i < rows.length; i++)
			if (rows[i].userID.equalsIgnoreCase(userID))
				return rows[i].acmData[problemIndex];
		return null;
	}
}
