package judge;

import common.XMLSerializable;

public abstract class AbstractResult extends XMLSerializable
{
	TestResultEnum result = TestResultEnum.Undefined;
	final String resultAttributeName = "result";
	
	int score = 0;
	final String scoreAttributeName = "score";
	
	final TestResultEnum getResult()
	{
		return result;
	}
	
	final void setResult(TestResultEnum result)
	{
		this.result = result;
	}

	final int getScore()
	{
		return score;
	}
	
	final void setScore(int score)
	{
		this.score = score;
	}
	
}
