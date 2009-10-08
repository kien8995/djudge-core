/* $Id: ValidatorExternalAbstract.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

/* Copyright (C) 2008 Olexiy Palinkash <olexiy.palinkash@gmail.com> */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */ 

package djudge.judge.validator;

import java.io.*;
import java.util.ArrayList;

import utils.FileWorks;


import djudge.judge.common_data_structures.ExecutorFiles;
import djudge.judge.common_data_structures.ExecutorLimits;
import djudge.judge.executor.Runner2;
import djudge.judge.executor.RunnerResultEnum;

public abstract class ValidatorExternalAbstract extends ValidatorAbstract implements ValidatorLimits
{
	String ValidatorOutputFile;
	
	public ValidatorExternalAbstract(String ExeName)
	{
		exeFile = ExeName;
	}
	
	public ValidationResult Validate(String input, String output, String answer)
	{
		res = new ValidationResult(this.toString());
		File f = new File(exeFile);
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
		
		ExecutorLimits limits = new ExecutorLimits(ValidatorLimits.MAX_VALIDATOR_RUNNING_TIME, 
				ValidatorLimits.MAX_VALIDATOR_CONSUMED_MEMORY, ValidatorLimits.MAX_VALIDATOR_OUTPUT_SIZE);
		
		Runner2 runner = new Runner2(limits, files);
		
		// FIXME ? quote values? -> like "input" "output" "answer"
		String cmd = exeFile + " " + input + " " + answer + " " + output;
		
		//System.out.println("VOF: " + ValidatorOutputFile);
		
		try
		{
			//System.out.println("VOFCMD: " + cmd);
			res.runInfo = runner.run(cmd);
		}
		catch (Exception exc)
		{
			// FIXME
			System.out.println("!!![ValidatorExternalAbstract.Validate]: " + exc);
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
				res.validatorOutput = new String[tmp.size()];
				for (int i = 0; i < tmp.size(); i++)
					res.validatorOutput[i] = tmp.get(i);
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
	
}
