package judge;

import validator.Validator;
import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

public class TestDescription
{
	int testNumber;
	
	
	public int getTestNumber()
	{
		return testNumber;
	}
	
	public String getInputFilename()
	{
		return "input.txt";
	}
	
	public String getAnswerFilename()
	{
		return "output.txt";
	}
	
	public RunnerFiles getFiles()
	{
		return new RunnerFiles("output.txt");
	}
	
	public RunnerLimits getLimits()
	{
		return new RunnerLimits();
	}

	public Validator getValidator()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
