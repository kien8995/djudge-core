/* $Id$ */

package djudge.judge.validator;

import djudge.judge.executor.RunnerResultEnum;

public class ValidatorTestLib extends ValidatorExternalAbstract
{
	public ValidatorTestLib(String exeFile) 
	{
		super(exeFile);
	}

	protected void processData() 
	{
		if (res.runInfo.state == RunnerResultEnum.OK)
		{
			res.result = ValidationResultEnum.OK;
		}
		else if (res.runInfo.state == RunnerResultEnum.NonZeroExitCode)
		{
			switch (res.runInfo.exitCode)
			{
			case 1:
			case 3:
			case 4:
				res.result = ValidationResultEnum.WrongAnswer;
				break;
				
			case 2: 
				res.result = ValidationResultEnum.PresentationError;
				break;
			
			default:
				System.out.println("rr2");
				res.fail = ValidationFailEnum.ValidatorFail;
				res.result = ValidationResultEnum.InternalError;
			}
		}
		else
		{
			System.out.println("rr");
			res.fail = ValidationFailEnum.ValidatorFail;
			res.result = ValidationResultEnum.InternalError;
		}
	}
	
	@Override
	public String toString()
	{
		return "Validator-TestLib";
	}
}
