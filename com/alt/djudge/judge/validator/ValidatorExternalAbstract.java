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

package com.alt.djudge.judge.validator;

import java.io.*;
import java.util.ArrayList;

import com.alt.djudge.judge.common_data_structures.RunnerFiles;
import com.alt.djudge.judge.common_data_structures.RunnerLimits;
import com.alt.djudge.judge.runner.Runner2;
import com.alt.djudge.judge.runner.RunnerResultEnum;
import com.alt.utils.FileWorks;




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

		// Checking validator exists 
		File f = new File(exeFile);
		if (!f.exists() && res.Result == ValidationResultEnum.Undefined)
		{
			res.Result = ValidationResultEnum.InternalError;
			res.Fail = ValidationFailEnum.ValidatorNoExeFile;
		}
		
		// Checking whether input file exists
		f = new File(input);
		if (!f.exists() && res.Result == ValidationResultEnum.Undefined)
		{
			res.Result = ValidationResultEnum.InternalError;
			res.Fail = ValidationFailEnum.NoInputFileError;
		}
		
		// Checking whether output file exists 
		f = new File(output);
		if (!f.exists() && res.Result == ValidationResultEnum.Undefined)
		{
			res.Result = ValidationResultEnum.InternalError;
			res.Fail = ValidationFailEnum.NoOutputFileError;
		}
		
		// Checking whether answer file exists
		f = new File(answer);
		if (!f.exists() && res.Result == ValidationResultEnum.Undefined)
		{
			res.Result = ValidationResultEnum.WrongAnswer;
			res.Fail = ValidationFailEnum.OK;
		}
		
		if (res.Result != ValidationResultEnum.Undefined)
			return res;
		
		// All files are present, executing external validator

		ValidatorOutputFile = FileWorks.getAbsolutePath(f.getParentFile() + "/" + "validator.output");
		res.ValidatorOutput = new String[0];
		
		RunnerFiles files = new RunnerFiles(ValidatorOutputFile);
		
		RunnerLimits limits = new RunnerLimits(ValidatorLimits.MAX_VALIDATOR_RUNNING_TIME, 
				ValidatorLimits.MAX_VALIDATOR_CONSUMED_MEMORY, ValidatorLimits.MAX_VALIDATOR_OUTPUT_SIZE);
		
		Runner2 runner = new Runner2(limits, files);
		
		// FIXME ? quote values? -> like "input" "output" "answer"
		String cmd = exeFile + " " + input + " " + output + " " + answer;
		
		System.out.println("VOF: " + ValidatorOutputFile);
		
		try
		{
			System.out.println("VOFCMD: " + cmd);
			res.RunInfo = runner.run(cmd);
		}
		catch (Exception exc)
		{
			// FIXME
			System.out.println("!!![ValidatorExternalAbstract.Validate]: " + exc);
			res.Result = ValidationResultEnum.InternalError;
			res.Fail = ValidationFailEnum.ValidatorNoExeFile;
		}
		
		if (res.Result == ValidationResultEnum.Undefined)
		{
			res.ValidatorExitCode = res.RunInfo.exitCode;
			res.Fail = ValidationFailEnum.OK;
			
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
				res.ValidatorOutput = new String[tmp.size()];
				for (int i = 0; i < tmp.size(); i++)
					res.ValidatorOutput[i] = tmp.get(i);
			}
			
			if (res.RunInfo.state != RunnerResultEnum.OK && res.RunInfo.state != RunnerResultEnum.NonZeroExitCode)
			{
				System.out.println("Validator error - " + res.RunInfo.state);
				res.Result = ValidationResultEnum.InternalError;
				
				switch (res.RunInfo.state)
				{
				case MemoryLimitExceeded:
					res.Fail = ValidationFailEnum.ValidatorMLE;
					break;
					
				case TimeLimitExceeeded:
					res.Fail = ValidationFailEnum.ValidatorTLE;
					break;
					
				case OutputLimitExceeded:
					res.Fail = ValidationFailEnum.ValidatorOLE;
					break;
					
				case RuntimeErrorCrash:
				case RuntimeErrorAccessViolation:
				case RuntimeErrorGeneral:
					res.Fail = ValidationFailEnum.ValidatorCrash;
					break;
				}
			}
			else
			{
				res.Fail = ValidationFailEnum.OK;
				processData();
			}
		}
		return res;		
	}
	
	protected abstract void processData();
	
}
