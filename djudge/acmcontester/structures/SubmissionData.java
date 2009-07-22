package djudge.acmcontester.structures;

public class SubmissionData
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
		realTime = "2009-10-03 17:45";
		score = -1;
		sourceCode = "";
		userID = "-1";
		djudgeFlag = 0;
		xml = "";
	}
}
