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
import djudge.judge.executor.ExecutionResultEnum;
import djudge.judge.executor.ExecutorFiles;
import djudge.judge.executor.ExecutorLimits;
import djudge.judge.executor.ExecutorProgram;
import djudge.judge.executor.ExecutorTask;
import djudge.judge.executor.LocalExecutor;

public abstract class CheckerExternalAbstract extends CheckerAbstract implements CheckerLimits
{
	private static final Logger log = Logger.getLogger(CheckerExternalAbstract.class);
	
	String validatorOutputFile;
		
	protected abstract void processData();
	
	public CheckerExternalAbstract(String executableName)
	{
		setExecutableFilename(executableName);
	}
	
	@Override
	public CheckerResult validateOutput(String judgeInputFilename, String generatedOutputFilename, String judgeAnswerFilename)
	{
		res = new CheckerResult(this.toString());
		File f = new File(getExeFilename());
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.CheckerNoExeFile);
			String msg = "Error. Cannot find validator executable file: " + getExeFilename();
			log.error(msg);
			res.setResultDetails(msg);
			return res;
		}
		
		// Checking whether input file exists
		f = new File(judgeInputFilename);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoJudgeInputFileError);
			String msg = "Error. Cannot find judge input file: " + judgeInputFilename;
			log.error(msg);
			res.setResultDetails(msg);
			return res;
		}
		
		// Checking whether judge answer file exists 
		f = new File(judgeAnswerFilename);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			String msg = "Error. Cannot find judge answer file: " + judgeAnswerFilename;
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoJudgeAnswerFileError);
			res.setResultDetails(msg);
			log.error(msg);
			return res;
		}
		
		// Checking whether generated output file exists
		f = new File(generatedOutputFilename);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.WrongAnswer);
			res.setFail(CheckerFailEnum.OK);
			String msg = "Cannot find generated output file: " + generatedOutputFilename;
			log.debug(msg);
			res.setResultDetails(msg);
			return res;
		}
		
		// All files are present, executing external validator
		validatorOutputFile = FileTools.getAbsolutePath(f.getParentFile() + "/" + "validator.output");
		res.setCheckerOutput(new String[0]);
		
		ExecutorFiles files = new ExecutorFiles(validatorOutputFile);
		
		ExecutorLimits limits = new ExecutorLimits(CheckerLimits.VALIDATOR_MAX_RUNNING_TIME, 
				CheckerLimits.VALIDATOR_MAX_CONSUMED_MEMORY, CheckerLimits.VALIDATOR_MAX_OUTPUT_SIZE);
		
		// FIXME ? quote values? -> like "input" "output" "answer"
		String cmd = getExeFilename() + " " + judgeInputFilename + " " + generatedOutputFilename + " " + judgeAnswerFilename;
		
		// TODO: FIMXE
		if (getExeFilename().endsWith(".jar"))
		{
			cmd = "java -cp " + getExeFilename() + " ru.ifmo.testlib.CheckerFramework Check" + " " +  judgeInputFilename + " " + judgeAnswerFilename + " " + generatedOutputFilename;
		}
		
		try
		{
			ExecutorProgram program = new ExecutorProgram(cmd);
			
			ExecutorTask task = new ExecutorTask(program, limits, files);
			//task.returnDirectoryContent = true;
			
			LocalExecutor runner = new LocalExecutor();
			
			// FIXME: this may cause problems with distributed files
			res.setRunInfo(runner.execute(task, f.getParent()));
		}
		catch (Exception exc)
		{
			String msg = "Exception while running external validator"; 
			log.error(msg, exc);
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.CheckerNoExeFile);
			res.setResultDetails(msg + ": " + exc.toString());
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
			
			if (res.getRunInfo().result != ExecutionResultEnum.OK && res.getRunInfo().result != ExecutionResultEnum.NonZeroExitCode)
			{
				log.error("Validator error - " + res.getRunInfo().result);
				res.setResult(CheckerResultEnum.InternalError);
				
				switch (res.getRunInfo().result)
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
				res.setResultDetails(res.getRunInfo().result.toString());
			}
			else
			{
				res.setFail(CheckerFailEnum.OK);
				processData();
			}
		}
		return res;		
	}
}
