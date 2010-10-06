/* $Id$ */

package djudge.judge.checker.external;

import org.apache.log4j.Logger;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.dexecutor.RunnerResultEnum;
import djudge.judge.executor.*;

public class CheckerExitCodeExtended extends CheckerExternalAbstract
{
	private static final Logger log = Logger.getLogger(CheckerExitCodeExtended.class);
	
	public CheckerExitCodeExtended(String executableFilename) 
	{
		super(executableFilename);
	}
	
	protected void processData()
	{
		if (res.getRunInfo().state == RunnerResultEnum.OK) res.setResult(CheckerResultEnum.OK);
		else if (res.getRunInfo().state == RunnerResultEnum.NonZeroExitCode) 
		{
			switch (res.getRunInfo().exitCode)
			{
			case 1:
				res.setResult(CheckerResultEnum.PresentationError); 
				break;
				
			case 3:
				res.setResult(CheckerResultEnum.InternalError);
				res.setFail(CheckerFailEnum.CheckerFail);
				res.setResultDetails("Checker's exit code: " + res.getRunInfo().exitCode);
				break;
				
			default:
				res.setResult(CheckerResultEnum.WrongAnswer);
			}
		}
		else
		{
			res.setResultDetails("Unknown error: " + res.getRunInfo().state);
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.CheckerFail);
			log.error(res.getRunInfo().state);
		}
	}
}
