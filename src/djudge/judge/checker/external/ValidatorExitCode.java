/* $Id$ */

package djudge.judge.checker.external;

import djudge.judge.checker.ValidationFailEnum;
import djudge.judge.checker.ValidationResultEnum;
import djudge.judge.executor.*;

public class ValidatorExitCode extends ValidatorExternalAbstract
{
	public ValidatorExitCode(String exeFile) 
	{
		super(exeFile);
	}
	
	protected void processData()
	{
		if (res.getRunInfo().state == RunnerResultEnum.OK)
		{
			res.setResult(ValidationResultEnum.OK);
		}
		else if (res.getRunInfo().state == RunnerResultEnum.NonZeroExitCode)
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
