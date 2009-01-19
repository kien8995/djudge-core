package judge;

public class TestResultSimple
{
	TestResultSimpleEnum result;
	int timeUsed;
	int memoryUsed;
	int outputUsed;
	String validatorMessage;
	
	{
		timeUsed = outputUsed = memoryUsed = -1;
		validatorMessage = "";
	}
	
	public int getTime()
	{
		return timeUsed;
	}
	
	public int getMemory()
	{
		return memoryUsed;
	}
	
	public int getOutput()
	{
		return memoryUsed;
	}
	
}
