/* $Id$ */

package djudge.judge.checker.external;

import djudge.judge.checker.ValidationFailEnum;
import djudge.judge.checker.ValidationResultEnum;
import djudge.judge.executor.RunnerResultEnum;

public class ValidatorTestLibJava extends ValidatorExternalAbstract
{
	public ValidatorTestLibJava(String exeFilename)
	{
		super(exeFilename);
	}

	protected void processData() 
	{
		if (res.runInfo.state == RunnerResultEnum.OK)
		{
			res.setResult(ValidationResultEnum.OK);
		}
		else if (res.runInfo.state == RunnerResultEnum.NonZeroExitCode)
		{
			switch (res.runInfo.exitCode)
			{
			case 1:
			case 3:
			case 4:
				res.setResult(ValidationResultEnum.WrongAnswer);
				break;
				
			case 2: 
				res.setResult(ValidationResultEnum.PresentationError);
				break;
			
			default:
				System.out.println("rr2");
				res.setFail(ValidationFailEnum.ValidatorFail);
				res.setResult(ValidationResultEnum.InternalError);
			}
		}
		else
		{
			System.out.println("rr");
			res.setFail(ValidationFailEnum.ValidatorFail);
			res.setResult(ValidationResultEnum.InternalError);
		}
	}
	
	@Override
	public String toString()
	{
		return "Validator.TestLib";
	}
}
