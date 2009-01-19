package judge;

import validator.Validator;
import common.XMLSerializable;
import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

public abstract class AbstractDescription extends XMLSerializable
{
	GlobalProblemInfo problemInfo;
	
	//public abstract void overrideLimits(RunnerLimits newLimits); 
	public abstract void overrideFiles(RunnerFiles newFiles); 
	
	public final RunnerFiles getFiles()
	{
		return problemInfo.files;
	}
	
	public final void setFiles(RunnerFiles files)
	{
		problemInfo.files = files;
	}
	
	public final RunnerLimits getLimits()
	{
		return problemInfo.limits;
	}
	
	public final void setLimits(RunnerLimits limits)
	{
		problemInfo.limits = limits;
	}
	
	public final Validator getValidator()
	{
		return problemInfo.validator;
	}
	
	public final void setValidator(Validator validator)
	{
		problemInfo.validator = validator;
	}
}
