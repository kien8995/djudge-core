/* $Id$ */

package djudge.judge.validator;

import djudge.judge.executor.*;

public class ValidatorExitCodeExtended extends ValidatorExternalAbstract
{
	public ValidatorExitCodeExtended(String ExeFile) 
	{
		super(ExeFile);
	}
	
	protected void processData()
	{
		if (res.runInfo.state == RunnerResultEnum.OK) res.result = ValidationResultEnum.OK;
		else if (res.runInfo.state == RunnerResultEnum.NonZeroExitCode) 
		{
			switch (res.runInfo.exitCode)
			{
			case 1 : 
				res.result = ValidationResultEnum.PresentationError; 
				break;
				
			case 3 : 
				res.result = ValidationResultEnum.InternalError;
				res.fail = ValidationFailEnum.ValidatorFail;
				break;
				
			default : 
				res.result = ValidationResultEnum.WrongAnswer;
			}
		}
		else
		{
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.ValidatorFail;			
		}
	}
}
