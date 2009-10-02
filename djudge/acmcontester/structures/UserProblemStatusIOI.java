package djudge.acmcontester.structures;


import java.util.HashMap;
import djudge.utils.xmlrpc.HashMapSerializable;

public class UserProblemStatusIOI extends HashMapSerializable
{
	public int score;
	
	public long maxScoreFirstTime;
	
	public long lastSubmitTime;
	
	public int submissionsTotal;
	
	public boolean isFullScore;
	
	public boolean isPending;
	
	public UserProblemStatusIOI()
	{
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public UserProblemStatusIOI(HashMap map)
	{
		fromHashMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map0)
	{
		HashMap<String, String> map = (HashMap<String, String>) map0;
		isPending = Boolean.parseBoolean(map.get("pending"));
		lastSubmitTime = Long.parseLong(map.get("last-time"));
		maxScoreFirstTime = Long.parseLong(map.get("max-score-time"));
		score = Integer.parseInt(map.get("score"));
		submissionsTotal = Integer.parseInt(map.get("submissions-count"));
		isFullScore = Boolean.parseBoolean(map.get("full-score"));
	}

	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pending", "" + isPending);
		map.put("last-time", "" + lastSubmitTime);
		map.put("max-score-time", "" + maxScoreFirstTime);
		map.put("score", "" + score);
		map.put("submissions-count", "" + submissionsTotal);
		map.put("full-score", "" + isFullScore);
		return map;
	}
}
