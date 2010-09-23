/* $Id$ */

package djudge.judge.validator;

import djudge.judge.executor.*;

public class ValidatorExitCode extends ValidatorExternalAbstract
{
	public ValidatorExitCode(String ExeFile) 
	{
		super(ExeFile);
	}
	
	protected void processData()
	{
		if (res.runInfo.state == RunnerResultEnum.OK)
		{
			res.result = ValidationResultEnum.OK;
		}
		else if (res.runInfo.state == RunnerResultEnum.NonZeroExitCode)
		{
			res.result = ValidationResultEnum.WrongAnswer;
		}
		else
		{
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.ValidatorFail;			
		}
	}
}
