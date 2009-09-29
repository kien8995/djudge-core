package djudge.acmcontester.structures;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import djudge.acmcontester.AuthentificationData;
import djudge.common.HashMapSerializable;

public class SubmissionData extends HashMapSerializable
{
	public String id;
	public String userID;
	public String problemID;
	public String languageID;
	public int contestTime;
	public String realTime;
	public String judgement;
	public int maxTime;
	public int maxMemory;
	public int maxOutput;
	public int failedTest;
	public int score;
	public int judged;
	public int active;
	public String sourceCode;
	public int djudgeFlag;
	public String xml;
	
	public SubmissionData()
	{
		id = "-1";
		active = 1;
		contestTime = -1;
		failedTest = -1;
		judged = 0;
		judgement = "N/A";
		languageID = "-1";
		maxMemory = -1;
		maxTime = -1;
		maxOutput = -1;
		problemID = "-1";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateStr = dateFormat.format(date);		
		realTime = dateStr;
		score = -1;
		sourceCode = "";
		userID = "-1";
		djudgeFlag = 0;
		xml = "";
	}
	
	public static final String[] fieldNames = {
		"id",
		"contesttime",
		"problem",
		"language",
		"judgement",
	};
	
	public Object getField(int fieldIndex)
	{
		switch (fieldIndex)
		{
		case 0: return id;
		case 1: return contestTime;
		case 2: return problemID;
		case 3: return languageID;
		case 4: return judgement;
		}
		return "";
	}
	
	public SubmissionData(HashMap<String, String> map)
	{
		fromHashMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map0)
	{
		HashMap<String, String> map = (HashMap<String, String>) map0;
		id = map.get("id");
		userID = map.get("user-id");
		problemID = map.get("problem-id");
		languageID = map.get("language-id");
		contestTime = Integer.parseInt(map.get("contest-time"));
		realTime = map.get("real-time");
		judgement = map.get("judgement");
		maxTime = Integer.parseInt(map.get("max-time"));
		maxMemory = Integer.parseInt(map.get("max-memory"));
		maxOutput = Integer.parseInt(map.get("max-output"));
		failedTest = Integer.parseInt(map.get("failed-test"));
		score = Integer.parseInt(map.get("score"));
		judged = Integer.parseInt(map.get("judged"));
		active = Integer.parseInt(map.get("active"));
		sourceCode = map.get("source-core");
		djudgeFlag = Integer.parseInt(map.get("djudge-flag"));
		xml = map.get("xml");
	}

	@Override
	public HashMap<String, String> toHashMap()
	{
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("id", id);
		res.put("user-id", userID);
		res.put("problem-id", problemID);
		res.put("language-id", languageID);
		res.put("contest-time", "" + contestTime);
		res.put("real-time", realTime);
		res.put("judgement", judgement);
		res.put("max-time", "" + maxTime);
		res.put("max-memory", "" + maxMemory);
		res.put("max-output", "" + maxOutput);
		res.put("failed-test", "" + failedTest);
		res.put("score", "" + score);
		res.put("judged", "" + judged);
		res.put("active", "" + active);
		res.put("source-core", sourceCode);
		res.put("djudge-flag", "" + djudgeFlag);
		res.put("xml", "" + xml);
		return res;
	}
	
	@Override
	int getColumnCount()
	{
		return 17;
	}

	@Override
	Class<? extends AbstractRemoteTable> getTableClass()
	{
		return RemoteTableSubmissions.class;
	}

	@Override
	Object getValueAt(int column)
	{
		switch (column)
		{
		case 0: return id;
		case 1: return userID;
		case 2: return problemID;
		case 3: return languageID;
		case 4: return contestTime;
		case 5: return realTime;
		case 6: return judgement;
		case 7: return maxTime;
		case 8: return maxMemory;
		case 9: return maxOutput;
		case 10: return failedTest;
		case 11: return score;
		case 12: return judged;
		case 13: return active;
		case 14: return sourceCode;
		case 15: return djudgeFlag;
		case 16: return xml;
		
		default:
			return id;
		}
	}
	
	@Override
	void setValueAt(int column, String value)
	{
		switch (column)
		{
		case 0: id = value; break;
		case 1: userID = value; break;
		case 2: problemID = value; break;
		case 3: languageID = value; break;
		case 4: contestTime = Integer.parseInt(value); break;
		case 5: realTime = value; break;
		case 6: judgement = value; break;
		case 7: maxTime = Integer.parseInt(value);; break;
		case 8: maxMemory = Integer.parseInt(value); break;
		case 9: maxOutput = Integer.parseInt(value); break;
		case 10: failedTest = Integer.parseInt(value); break;
		case 11: score = Integer.parseInt(value); break;
		case 12: judged = Integer.parseInt(value); break;
		case 13: active = Integer.parseInt(value); break;
		case 14: sourceCode = value; break;
		case 15: djudgeFlag = Integer.parseInt(value); break;
		case 16: xml = value; break;
		}
	}
	
	@Override
	boolean save()
	{
		if (!fDataChanged)
			return true;
		AuthentificationData ad = table.getAuthentificationData();
		boolean res = table.getConnector().editSubmission(ad.getUsername(),
				ad.getPassword(), id, toHashMap());
		fDataChanged = false;
		return res;
	}
	
	@Override
	boolean create()
	{
		return false;
	}
	
	@Override
	boolean delete()
	{
		AuthentificationData ad = table.getAuthentificationData();
		return table.getConnector().deleteSubmission(
				ad.getUsername(),
				ad.getPassword(), id);
	}
}
