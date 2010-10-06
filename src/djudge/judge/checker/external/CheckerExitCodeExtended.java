/* $Id$ */

package djudge.judge.checker.external;

import org.apache.log4j.Logger;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.executor.*;

public class CheckerExitCodeExtended extends CheckerExternalAbstract
{
	private static final Logger log = Logger.getLogger(CheckerExitCodeExtended.class);
	
	public CheckerExitCodeExtended(String executableFilename) 
	{
		super(executableFilename);
	}
	
	public void processData()
	{
		if (res.getRunInfo().result == ExecutionResultEnum.OK) res.setResult(CheckerResultEnum.OK);
		else if (res.getRunInfo().result == ExecutionResultEnum.NonZeroExitCode) 
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
			res.setResultDetails("Unknown error: " + res.getRunInfo().result);
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.CheckerFail);
			log.error(res.getRunInfo().result);
		}
	}
}
