/* $Id$ */

package djudge.judge.checker;

import djudge.judge.executor.*;

public class ValidatorExitCodeExtended extends ValidatorExternalAbstract
{
	public ValidatorExitCodeExtended(String exeFile) 
	{
		super(exeFile);
	}
	
	protected void processData()
	{
		if (res.runInfo.state == RunnerResultEnum.OK) res.setResult(ValidationResultEnum.OK);
		else if (res.runInfo.state == RunnerResultEnum.NonZeroExitCode) 
		{
			switch (res.runInfo.exitCode)
			{
			case 1 : 
				res.setResult(ValidationResultEnum.PresentationError); 
				break;
				
			case 3 : 
				res.setResult(ValidationResultEnum.InternalError);
				res.setFail(ValidationFailEnum.ValidatorFail);
				break;
				
			default : 
				res.setResult(ValidationResultEnum.WrongAnswer);
			}
		}
		else
		{
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(ValidationFailEnum.ValidatorFail);			
		}
	}
}
