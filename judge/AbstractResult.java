package judge;

import common.XMLSerializable;

public abstract class AbstractResult extends XMLSerializable
{
	TestResultEnum result = TestResultEnum.Undefined;
	
	final TestResultEnum getResult()
	{
		return result;
	}
	
	final void setResult(TestResultEnum result)
	{
		this.result = result;
	}
}
