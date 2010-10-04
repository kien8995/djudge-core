/* $Id$ */

package djudge.judge.checker.external;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.executor.RunnerResultEnum;

public class CheckerTestLib extends CheckerExternalAbstract
{
	public CheckerTestLib(String exeFile) 
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
			switch (res.getRunInfo().exitCode)
			{
			case 1:
			case 3:
			case 4:
				res.setResult(CheckerResultEnum.WrongAnswer);
				break;
				
			case 2: 
				res.setResult(CheckerResultEnum.PresentationError);
				break;
			
			default:
				System.out.println("rr2");
				res.setFail(CheckerFailEnum.ValidatorFail);
				res.setResult(CheckerResultEnum.InternalError);
			}
		}
		else
		{
			System.out.println("rr");
			res.setFail(CheckerFailEnum.ValidatorFail);
			res.setResult(CheckerResultEnum.InternalError);
		}
	}
	
	@Override
	public String toString()
	{
		return "Validator-TestLib";
	}
}
