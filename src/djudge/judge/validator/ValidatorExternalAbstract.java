/* $Id$ */

package djudge.judge.validator;

import java.util.ArrayList;

import java.io.*;
import org.apache.log4j.Logger;

import utils.FileWorks;

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
		exeFilename = exeName;
	}
	
	@Override
	public ValidationResult validateOutput(String input, String output, String answer)
	{
		res = new ValidationResult(this.toString());
		File f = new File(exeFilename);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			log.error("Error. Cannot find validator executable file: " + exeFilename);
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.ValidatorNoExeFile;
		}
		
		// Checking whether input file exists
		f = new File(input);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			log.error("Error. Cannot find input file: " + input);
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.NoInputFileError;
			return res;
		}
		
		// Checking whether output file exists 
		f = new File(output);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			log.error("Error. Cannot find output file: " + output);
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.NoOutputFileError;
			return res;
		}
		
		// Checking whether answer file exists
		f = new File(answer);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			log.debug("Cannot answer file: " + answer);
			res.result = ValidationResultEnum.WrongAnswer;
			res.fail = ValidationFailEnum.OK;
			return res;
		}
		
		// All files are present, executing external validator
		validatorOutputFile = FileWorks.getAbsolutePath(f.getParentFile() + "/" + "validator.output");
		res.validatorOutput = new String[0];
		
		ExecutorFiles files = new ExecutorFiles(validatorOutputFile);
		
		ExecutorLimits limits = new ExecutorLimits(ValidatorLimits.VALIDATOR_MAX_RUNNING_TIME, 
				ValidatorLimits.VALIDATOR_MAX_CONSUMED_MEMORY, ValidatorLimits.VALIDATOR_MAX_OUTPUT_SIZE);
		
		Runner runner = new Runner(limits, files);
		
		// FIXME ? quote values? -> like "input" "output" "answer"
		String cmd = exeFilename + " " + input + " " + answer + " " + output;
		
		// TODO: FIMXE
		if (exeFilename.endsWith(".jar"))
		{
			cmd = "java -cp " + exeFilename + " ru.ifmo.testlib.CheckerFramework Check" + " " +  input + " " + answer + " " + output;
		}
		
		try
		{
			res.runInfo = runner.run(cmd);
		}
		catch (Exception exc)
		{
			log.error("Exception while running external validator", exc);
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.ValidatorNoExeFile;
		}
		
		if (res.result == ValidationResultEnum.Undefined)
		{
			res.exitCode = res.runInfo.exitCode;
			res.fail = ValidationFailEnum.OK;
			
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
				res.validatorOutput = new String[tmp.size()+1];
				for (int i = 0; i < tmp.size(); i++)
					res.validatorOutput[i] = tmp.get(i);
				res.validatorOutput[tmp.size()] = "[" + this.toString() + "]";
			}
			
			if (res.runInfo.state != RunnerResultEnum.OK && res.runInfo.state != RunnerResultEnum.NonZeroExitCode)
			{
				log.error("Validator error - " + res.runInfo.state);
				res.result = ValidationResultEnum.InternalError;
				
				switch (res.runInfo.state)
				{
				case MemoryLimitExceeded:
					res.fail = ValidationFailEnum.ValidatorMLE;
					break;
					
				case TimeLimitExceeeded:
					res.fail = ValidationFailEnum.ValidatorTLE;
					break;
					
				case OutputLimitExceeded:
					res.fail = ValidationFailEnum.ValidatorOLE;
					break;
					
				case RuntimeErrorCrash:
				case RuntimeErrorAccessViolation:
				case RuntimeErrorGeneral:
					res.fail = ValidationFailEnum.ValidatorCrash;
					break;
				}
			}
			else
			{
				res.fail = ValidationFailEnum.OK;
				processData();
			}
		}
		return res;		
	}
	
	protected abstract void processData();
}
