/* $Id$ */

package djudge.judge.checker.external;

import java.util.ArrayList;

import java.io.*;
import org.apache.log4j.Logger;

import utils.FileTools;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResult;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.checker.CheckerAbstract;
import djudge.judge.checker.CheckerLimits;
import djudge.judge.common_data_structures.ExecutorFiles;
import djudge.judge.common_data_structures.ExecutorLimits;
import djudge.judge.dexecutor.RunnerResultEnum;
import djudge.judge.executor.Runner;

public abstract class CheckerExternalAbstract extends CheckerAbstract implements CheckerLimits
{
	private static final Logger log = Logger.getLogger(CheckerExternalAbstract.class);
	
	String validatorOutputFile;
	
	public CheckerExternalAbstract(String executableName)
	{
		setExecutableFilename(executableName);
	}
	
	@Override
	public CheckerResult validateOutput(String input, String output, String answer)
	{
		res = new CheckerResult(this.toString());
		File f = new File(getExeFilename());
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			log.error("Error. Cannot find validator executable file: " + getExeFilename());
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.CheckerNoExeFile);
			res.setResultDetails("Error. Cannot find validator executable file: " + getExeFilename());
			return res;
		}
		
		// Checking whether input file exists
		f = new File(input);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			log.error("Error. Cannot find input file: " + input);
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoInputFileError);
			res.setResultDetails("Error. Cannot find input file: " + input);
			return res;
		}
		
		// Checking whether output file exists 
		f = new File(output);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			log.error("Error. Cannot find output file: " + output);
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoOutputFileError);
			res.setResultDetails("Error. Cannot find output file: " + output);
			return res;
		}
		
		// Checking whether answer file exists
		f = new File(answer);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			log.debug("Cannot answer file: " + answer);
			res.setResult(CheckerResultEnum.WrongAnswer);
			res.setFail(CheckerFailEnum.OK);
			res.setResultDetails("Cannot answer file: " + answer);
			return res;
		}
		
		// All files are present, executing external validator
		validatorOutputFile = FileTools.getAbsolutePath(f.getParentFile() + "/" + "validator.output");
		res.setCheckerOutput(new String[0]);
		
		ExecutorFiles files = new ExecutorFiles(validatorOutputFile);
		
		ExecutorLimits limits = new ExecutorLimits(CheckerLimits.VALIDATOR_MAX_RUNNING_TIME, 
				CheckerLimits.VALIDATOR_MAX_CONSUMED_MEMORY, CheckerLimits.VALIDATOR_MAX_OUTPUT_SIZE);
		
		Runner runner = new Runner(limits, files);
		
		// FIXME ? quote values? -> like "input" "output" "answer"
		String cmd = getExeFilename() + " " + input + " " + answer + " " + output;
		
		// TODO: FIMXE
		if (getExeFilename().endsWith(".jar"))
		{
			cmd = "java -cp " + getExeFilename() + " ru.ifmo.testlib.CheckerFramework Check" + " " +  input + " " + answer + " " + output;
		}
		
		try
		{
			res.setRunInfo(runner.run(cmd));
		}
		catch (Exception exc)
		{
			log.error("Exception while running external validator", exc);
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.CheckerNoExeFile);
			res.setResultDetails("Exception while running validator: " + exc.toString());
		}
		
		if (res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setExitCode(res.getRunInfo().exitCode);
			res.setFail(CheckerFailEnum.OK);
			
			ArrayList<String> tmp = new ArrayList<String>();
			try
			{
				BufferedReader out = new BufferedReader(new FileReader(validatorOutputFile));
				String t;
				while ((t = out.readLine()) != null)
					tmp.add(t);
			}
			catch (Exception exc)
			{
				// FIXME
				log.error("Unknown error", exc);
				res.setResultDetails(exc.toString());
			}
			finally
			{
				res.setCheckerOutput(new String[tmp.size()+1]);
				for (int i = 0; i < tmp.size(); i++)
					res.getCheckerOutput()[i] = tmp.get(i);
				res.getCheckerOutput()[tmp.size()] = "[" + this.toString() + "]";
			}
			
			if (res.getRunInfo().state != RunnerResultEnum.OK && res.getRunInfo().state != RunnerResultEnum.NonZeroExitCode)
			{
				log.error("Validator error - " + res.getRunInfo().state);
				res.setResult(CheckerResultEnum.InternalError);
				
				switch (res.getRunInfo().state)
				{
				case MemoryLimitExceeded:
					res.setFail(CheckerFailEnum.CheckerMLE);
					break;
					
				case TimeLimitExceeeded:
					res.setFail(CheckerFailEnum.CheckerTLE);
					break;
					
				case OutputLimitExceeded:
					res.setFail(CheckerFailEnum.CheckerOLE);
					break;
					
				case RuntimeErrorCrash:
				case RuntimeErrorAccessViolation:
				case RuntimeErrorGeneral:
					res.setFail(CheckerFailEnum.CheckerCrash);
					break;
				}
				res.setResultDetails(res.getRunInfo().state.toString());
			}
			else
			{
				res.setFail(CheckerFailEnum.OK);
				processData();
			}
		}
		return res;		
	}
	
	protected abstract void processData();
}
