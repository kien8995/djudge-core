package judge;

import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

import validator.Validator;
import validator.ValidatorType;

public class GlobalProblemInfo implements Cloneable
{
	String problemID;
	String contestID;
	
	RunnerFiles files;
	RunnerLimits limits;
	Validator validator;
	String solutions[];
	
	String problemRootDirectory;
	
	ProblemTypeEnum type;
	
	String programInputFilename;
	String programOutputFilename;
	String problemRoot;
	
	boolean fFrozenLimits = false;
	
	public GlobalProblemInfo()
	{
		type = ProblemTypeEnum.ACM;
		problemID = contestID = "unknown";
		files = new RunnerFiles();
		limits = new RunnerLimits();
		validator = new Validator(ValidatorType.InternalExact);
		problemRootDirectory = "";
		solutions = new String[0];
	}
	
	public GlobalProblemInfo clone()
	{
		try
		{
			return (GlobalProblemInfo)super.clone();
		}
		catch (CloneNotSupportedException exc)
		{
			System.out.println("Exception occured while cloning class judge.GlobalProblemInfo: " + exc);
		}
		return this;
	}
}
