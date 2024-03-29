/* $Id$ */

package djudge.judge;

import djudge.common.XMLSerializable;

public abstract class AbstractResult extends XMLSerializable
{
	TestResultEnum result = TestResultEnum.Undefined;
	final String resultAttributeName = "result";

	String resultDetails = "";
	final String resultDetailsAttributeName = "result-details";
	
	long maxTime = -1;
	final String maxTimeAttributeName = "max-time";

	int wrongTest = -1;
	final String wrongTestAttributeName = "wrong-test";
	
	long maxMemory = -1;
	final String maxMemoryAttributeName = "max-memory";
	
	int score = 0;
	final String scoreAttributeName = "score";
	
	public final TestResultEnum getResult()
	{
		return result;
	}
	
	public final int getWrongTest()
	{
		return wrongTest;
	}
	
	public final void setResult(TestResultEnum result)
	{
		this.result = result;
	}

	public final int getScore()
	{
		return score;
	}
	
	public final void setScore(int score)
	{
		this.score = score;
	}
	
	public final long getMaxTime()
	{
		return maxTime;
	}

	public final int getMaxOutput()
	{
		return 0;
	}

	public final long getMaxMemory()
	{
		return maxMemory;
	}

	public TestResultEnum getJudgement()
	{
		return result;
	}
	
	public final String getResultDetails()
	{
		return resultDetails;
	}
	
	public final boolean isRuntimeJudgement()
	{
		return 
			result.equals(TestResultEnum.CE) ||
			result.equals(TestResultEnum.MLE) ||
			result.equals(TestResultEnum.OLE) ||
			result.equals(TestResultEnum.RE) ||
			result.equals(TestResultEnum.SV) ||
			result.equals(TestResultEnum.TLE);
	}
}
