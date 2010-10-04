/* $Id$ */

package djudge.judge.checker.external;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.executor.*;

public class CheckerExitCode extends CheckerExternalAbstract
{
	public CheckerExitCode(String exeFile) 
	{
		super(exeFile);
	}
	
	protected void processData()
	{
		if (res.getRunInfo().state == RunnerResultEnum.OK)
		{
			res.setResult(CheckerResultEnum.OK);
		}
		else if (res.getRunInfo().state == RunnerResultEnum.NonZeroExitCode)
		{
			res.setResult(CheckerResultEnum.WrongAnswer);
		}
		else
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.ValidatorFail);
		}
	}
}
