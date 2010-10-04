/* $Id$ */

package djudge.judge.checker.external;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.ValidationResultEnum;
import djudge.judge.executor.*;

public class ValidatorExitCodeExtended extends ValidatorExternalAbstract
{
	public ValidatorExitCodeExtended(String exeFile) 
	{
		super(exeFile);
	}
	
	protected void processData()
	{
		if (res.getRunInfo().state == RunnerResultEnum.OK) res.setResult(ValidationResultEnum.OK);
		else if (res.getRunInfo().state == RunnerResultEnum.NonZeroExitCode) 
		{
			switch (res.getRunInfo().exitCode)
			{
			case 1 : 
				res.setResult(ValidationResultEnum.PresentationError); 
				break;
				
			case 3 : 
				res.setResult(ValidationResultEnum.InternalError);
				res.setFail(CheckerFailEnum.ValidatorFail);
				break;
				
			default : 
				res.setResult(ValidationResultEnum.WrongAnswer);
			}
		}
		else
		{
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(CheckerFailEnum.ValidatorFail);			
		}
	}
}
