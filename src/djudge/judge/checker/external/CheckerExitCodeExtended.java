/* $Id$ */

package djudge.judge.checker.external;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.executor.*;

public class CheckerExitCodeExtended extends CheckerExternalAbstract
{
	public CheckerExitCodeExtended(String exeFile) 
	{
		super(exeFile);
	}
	
	protected void processData()
	{
		if (res.getRunInfo().state == RunnerResultEnum.OK) res.setResult(CheckerResultEnum.OK);
		else if (res.getRunInfo().state == RunnerResultEnum.NonZeroExitCode) 
		{
			switch (res.getRunInfo().exitCode)
			{
			case 1 : 
				res.setResult(CheckerResultEnum.PresentationError); 
				break;
				
			case 3 : 
				res.setResult(CheckerResultEnum.InternalError);
				res.setFail(CheckerFailEnum.ValidatorFail);
				break;
				
			default : 
				res.setResult(CheckerResultEnum.WrongAnswer);
			}
		}
		else
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.ValidatorFail);			
		}
	}
}
