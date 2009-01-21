package judge;

import common.Loggable;
import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

import validator.Validator;
import validator.ValidatorType;

public class GlobalProblemInfo extends Loggable implements Cloneable
{
	String problemID;
	String contestID;
	
	RunnerFiles files;
	RunnerLimits limits;
	Validator validator;
	String solutions[];
	
	ProblemTypeEnum type;
	
	public String programInputFilename;
	public String programOutputFilename;
	String problemRoot;
	
	boolean fFrozenLimits = false;
	
	public void print()
	{
		log("*** Problem Info ***");
		log("ContestID: " + contestID);
		log("ProblemID: " + problemID);
		log("Directory: " + problemID);
	}
	
	
	public GlobalProblemInfo()
	{
		type = ProblemTypeEnum.ACM;
		problemID = contestID = "unknown";
		files = new RunnerFiles();
		limits = new RunnerLimits();
		validator = new Validator(ValidatorType.InternalExact);
		problemRoot = "";
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
