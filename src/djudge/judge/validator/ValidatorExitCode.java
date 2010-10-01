/* $Id$ */

package djudge.judge.validator;

import djudge.judge.executor.*;

public class ValidatorExitCode extends ValidatorExternalAbstract
{
	public ValidatorExitCode(String exeFile) 
	{
		super(exeFile);
	}
	
	protected void processData()
	{
		if (res.runInfo.state == RunnerResultEnum.OK)
		{
			res.setResult(ValidationResultEnum.OK);
		}
		else if (res.runInfo.state == RunnerResultEnum.NonZeroExitCode)
		{
			res.setResult(ValidationResultEnum.WrongAnswer);
		}
		else
		{
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(ValidationFailEnum.ValidatorFail);
		}
	}
}
