/* $Id$ */

package djudge.judge.validator;

import java.io.*;
import java.util.ArrayList;

import utils.FileWorks;
import utils.Scripts;


import djudge.judge.common_data_structures.ExecutorFiles;
import djudge.judge.common_data_structures.ExecutorLimits;
import djudge.judge.executor.Runner;
import djudge.judge.executor.RunnerResultEnum;

public abstract class ValidatorExternalAbstract extends ValidatorAbstract implements ValidatorLimits
{
	String ValidatorOutputFile;
	
	public ValidatorExternalAbstract(String exeName)
	{
		exeFilename = exeName;
	}
	
	@Override
	public ValidationResult validateOutput(String input, String output, String answer)
	{
		res = new ValidationResult(this.toString());
		File f = new File(exeFilename);
		System.out.println("EXE: " + exeFilename);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			System.out.println("!!! Error: No validator exe: " + f);
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.ValidatorNoExeFile;
		}
		
		// Checking whether input file exists
		f = new File(input);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.NoInputFileError;
		}
		
		// Checking whether output file exists 
		f = new File(output);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.NoOutputFileError;
		}
		
		// Checking whether answer file exists
		f = new File(answer);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			res.result = ValidationResultEnum.WrongAnswer;
			res.fail = ValidationFailEnum.OK;
		}
		
		if (res.result != ValidationResultEnum.Undefined)
			return res;
		
		// All files are present, executing external validator
		ValidatorOutputFile = FileWorks.getAbsolutePath(f.getParentFile() + "/" + "validator.output");
		res.validatorOutput = new String[0];
		
		ExecutorFiles files = new ExecutorFiles(ValidatorOutputFile);
		
		ExecutorLimits limits = new ExecutorLimits(ValidatorLimits.VALIDATOR_MAX_RUNNING_TIME, 
				ValidatorLimits.VALIDATOR_MAX_CONSUMED_MEMORY, ValidatorLimits.VALIDATOR_MAX_OUTPUT_SIZE);
		
		Runner runner = new Runner(limits, files);
		
		// FIXME ? quote values? -> like "input" "output" "answer"
		String cmd = exeFilename + " " + input + " " + answer + " " + output;
		
		// TODO: FIMXE
		if (exeFilename.endsWith(".jar"))
		{
			//String s = exeFile.replaceFirst(".jar", "");
			cmd = "java -cp " + exeFilename + " ru.ifmo.testlib.CheckerFramework Check" + " " +  input + " " + answer + " " + output;
		}
		
		try
		{
			//System.out.println(cmd);
			res.runInfo = runner.run(cmd);
		}
		catch (Exception exc)
		{
			// FIXME
			System.out.println("!!![ValidatorExternalAbstract.Validate]2: " + exc);
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
				BufferedReader out = new BufferedReader(new FileReader(ValidatorOutputFile));
				String t;
				while ((t = out.readLine()) != null)
					tmp.add(t);
			}
			catch (Exception exc)
			{
				// FIXME
				System.out.println("!!![ValidatorExternalAbstract.Validate]: " + exc);
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
				System.out.println("Validator error - " + res.runInfo.state);
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

	public static void main(String arg[]) throws Exception
	{
		Scripts.generateProblemReport("ORSPC-2009", "C");
	}	
}
