/* $Id$ */

package djudge.acmcontester.structures;

import java.util.HashMap;

import djudge.acmcontester.admin.AdminClient;
import djudge.utils.xmlrpc.HashMapSerializable;

public class UserProblemStatusACM extends HashMapSerializable
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
			res = (wrongTryes + 1) + "/" + lastSubmitTime;
		}
		else if (wrongTryes > 0)
		{
			res = (wrongTryes) + "/--";
		}
		return res;
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}
	
	public UserProblemStatusACM()
	{
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public UserProblemStatusACM(HashMap map)
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
