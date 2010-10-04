/* $Id$ */

package djudge.judge.checker.external;

import java.util.ArrayList;

import java.io.*;
import org.apache.log4j.Logger;

import utils.FileWorks;

import djudge.judge.checker.ValidationFailEnum;
import djudge.judge.checker.ValidationResult;
import djudge.judge.checker.ValidationResultEnum;
import djudge.judge.checker.ValidatorAbstract;
import djudge.judge.checker.ValidatorLimits;
import djudge.judge.common_data_structures.ExecutorFiles;
import djudge.judge.common_data_structures.ExecutorLimits;
import djudge.judge.executor.Runner;
import djudge.judge.executor.RunnerResultEnum;

public abstract class ValidatorExternalAbstract extends ValidatorAbstract implements ValidatorLimits
{
	private static final Logger log = Logger.getLogger(ValidatorAbstract.class);
	
	String validatorOutputFile;
	
	public ValidatorExternalAbstract(String exeName)
	{
		setExeFilename(exeName);
	}
	
	@Override
	public ValidationResult validateOutput(String input, String output, String answer)
	{
		res = new ValidationResult(this.toString());
		File f = new File(getExeFilename());
		if (!f.exists() && res.getResult() == ValidationResultEnum.Undefined)
		{
			log.error("Error. Cannot find validator executable file: " + getExeFilename());
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(ValidationFailEnum.ValidatorNoExeFile);
		}
		
		// Checking whether input file exists
		f = new File(input);
		if (!f.exists() && res.getResult() == ValidationResultEnum.Undefined)
		{
			log.error("Error. Cannot find input file: " + input);
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(ValidationFailEnum.NoInputFileError);
			return res;
		}
		
		// Checking whether output file exists 
		f = new File(output);
		if (!f.exists() && res.getResult() == ValidationResultEnum.Undefined)
		{
			log.error("Error. Cannot find output file: " + output);
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(ValidationFailEnum.NoOutputFileError);
			return res;
		}
		
		// Checking whether answer file exists
		f = new File(answer);
		if (!f.exists() && res.getResult() == ValidationResultEnum.Undefined)
		{
			log.debug("Cannot answer file: " + answer);
			res.setResult(ValidationResultEnum.WrongAnswer);
			res.setFail(ValidationFailEnum.OK);
			return res;
		}
		
		// All files are present, executing external validator
		validatorOutputFile = FileWorks.getAbsolutePath(f.getParentFile() + "/" + "validator.output");
		res.setValidatorOutput(new String[0]);
		
		ExecutorFiles files = new ExecutorFiles(validatorOutputFile);
		
		ExecutorLimits limits = new ExecutorLimits(ValidatorLimits.VALIDATOR_MAX_RUNNING_TIME, 
				ValidatorLimits.VALIDATOR_MAX_CONSUMED_MEMORY, ValidatorLimits.VALIDATOR_MAX_OUTPUT_SIZE);
		
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
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(ValidationFailEnum.ValidatorNoExeFile);
		}
		
		if (res.getResult() == ValidationResultEnum.Undefined)
		{
			res.setExitCode(res.getRunInfo().exitCode);
			res.setFail(ValidationFailEnum.OK);
			
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
			}
			finally
			{
				res.setValidatorOutput(new String[tmp.size()+1]);
				for (int i = 0; i < tmp.size(); i++)
					res.getValidatorOutput()[i] = tmp.get(i);
				res.getValidatorOutput()[tmp.size()] = "[" + this.toString() + "]";
			}
			
			if (res.getRunInfo().state != RunnerResultEnum.OK && res.getRunInfo().state != RunnerResultEnum.NonZeroExitCode)
			{
				log.error("Validator error - " + res.getRunInfo().state);
				res.setResult(ValidationResultEnum.InternalError);
				
				switch (res.getRunInfo().state)
				{
				case MemoryLimitExceeded:
					res.setFail(ValidationFailEnum.ValidatorMLE);
					break;
					
				case TimeLimitExceeeded:
					res.setFail(ValidationFailEnum.ValidatorTLE);
					break;
					
				case OutputLimitExceeded:
					res.setFail(ValidationFailEnum.ValidatorOLE);
					break;
					
				case RuntimeErrorCrash:
				case RuntimeErrorAccessViolation:
				case RuntimeErrorGeneral:
					res.setFail(ValidationFailEnum.ValidatorCrash);
					break;
				}
			}
			else
			{
				res.setFail(ValidationFailEnum.OK);
				processData();
			}
		}
		return res;		
	}
	
	protected abstract void processData();
}
