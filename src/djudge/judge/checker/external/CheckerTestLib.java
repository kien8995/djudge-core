/* $Id$ */

package djudge.judge.checker.external;

import org.apache.log4j.Logger;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.dexecutor.RunnerResultEnum;

// TODO: review
public class CheckerTestLib extends CheckerExternalAbstract
{
	private final static Logger log = Logger.getLogger(CheckerTestLib.class);
	
	public CheckerTestLib(String executableFilename) 
	{
		super(executableFilename);
	}

	protected void processData() 
	{
		if (res.getRunInfo().state == RunnerResultEnum.OK)
		{
			res.setResult(CheckerResultEnum.OK);
			res.setResultDetails("OK: Accepted");
		}
		else if (res.getRunInfo().state == RunnerResultEnum.NonZeroExitCode)
		{
			System.out.println("Valiator's exit code: " + res.getRunInfo().exitCode);
			switch (res.getRunInfo().exitCode)
			{
			case 1:
			case 3:
			case 4:
				res.setResult(CheckerResultEnum.WrongAnswer);
				res.setResultDetails("OK: WA");
				break;
				
			case 2: 
				res.setResult(CheckerResultEnum.PresentationError);
				res.setResultDetails("OK: PE");
				break;
			
			default:
				log.warn("Checker failed");
				res.setFail(CheckerFailEnum.CheckerFail);
				res.setResult(CheckerResultEnum.InternalError);
				res.setResultDetails("Error: IE. Exit-code: " + res.getRunInfo().exitCode);
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
