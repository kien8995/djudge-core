package com.alt.djudge.judge;

import com.alt.djudge.common.XMLSerializable;

public abstract class AbstractResult extends XMLSerializable
{
	TestResultEnum result = TestResultEnum.Undefined;
	final String resultAttributeName = "result";
	
	int maxTime = -1;
	final String maxTimeAttributeName = "max-time";

	int wrongTest = -1;
	final String wrongTestAttributeName = "wrong-test";
	
	int maxMemory = -1;
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
	
	public final int getMaxTime()
	{
		return maxTime;
	}

	public final int getMaxOutput()
	{
		return 0;
	}

	public final int getMaxMemory()
	{
		return maxMemory;
	}

	public TestResultEnum getJudgement()
	{
		return result;
	}
}
