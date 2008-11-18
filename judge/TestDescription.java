package judge;

import validator.Validator;
import validator.ValidatorType;
import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

public class TestDescription
{
	int testNumber;
	Validator validator;
	RunnerFiles files;
	RunnerLimits limits;
	GlobalProblemInfo globalInfo;

	private void init(int num, RunnerLimits limits, RunnerFiles files, Validator val)
	{
		testNumber = num;
		this.limits = limits;
		this.files = files;
		this.validator = val;
	}
	
	private void initEmpty()
	{
		init(0, new RunnerLimits(), new RunnerFiles(), new Validator(ValidatorType.InternalExact));
	}
	
	public TestDescription()
	{
		initEmpty();
	}
	
	public TestDescription(int num, GroupDescription group)
	{
		
	}
	
	public void setFiles(RunnerFiles newFiles)
	{
		files = newFiles;
	}
	
	public void setLimits(RunnerLimits newLimits)
	{
		limits = newLimits;
	}
	
	public void setTestNumber(int num)
	{
		this.testNumber = num;
	}
	
	public int getTestNumber()
	{
		return testNumber;
	}
	
	public String getInputFilename()
	{
		return "input.txt";
	}
	
	public String getEtalonFilename()
	{
		return "output.txt";		
	}
	
	public RunnerFiles getFiles()
	{
		return files;
	}
	
	public RunnerLimits getLimits()
	{
		return limits;
	}

	public Validator getValidator()
	{
		return validator;
	}
}
