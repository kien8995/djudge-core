/* $Id$ */

package djudge.judge.checker.external;

import org.apache.log4j.Logger;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.dexecutor.RunnerResultEnum;

public class CheckerTestLibJava extends CheckerExternalAbstract
{
	private final static Logger log = Logger.getLogger(CheckerTestLibJava.class);
	
	public CheckerTestLibJava(String executableFilename)
	{
		super(executableFilename);
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
				log.warn("Checker failed");
				res.setFail(CheckerFailEnum.CheckerFail);
				res.setResult(CheckerResultEnum.InternalError);
			}
		}
		else
		{
			log.warn("Checker failed");
			res.setFail(CheckerFailEnum.CheckerFail);
			res.setResult(CheckerResultEnum.InternalError);
		}
	}
	
	@Override
	public String toString()
	{
		return "Checker-TestLib";
	}
}
