/* $Id$ */

package djudge.judge.checker.external;

import org.apache.log4j.Logger;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.dexecutor.RunnerResultEnum;
import djudge.judge.executor.*;

public class CheckerExitCode extends CheckerExternalAbstract
{
	private static final Logger log = Logger.getLogger(CheckerExitCode.class);
	
	public CheckerExitCode(String executableFilename)
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
			res.setResult(CheckerResultEnum.WrongAnswer);
		}
		else
		{
			log.error(res.getRunInfo().state);
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.CheckerFail);
		}
	}
}
