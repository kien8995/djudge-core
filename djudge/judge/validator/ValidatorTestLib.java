/* $Id: ValidatorTestLib.java, v 0.1 2008/07/26 05:13:08 alt Exp $ */

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

import djudge.judge.executor.RunnerResultEnum;

public class ValidatorTestLib extends ValidatorExternalAbstract
{
	public ValidatorTestLib(String ExeFile) 
	{
		super(ExeFile);
	}

	protected void processData() 
	{
		if (res.runInfo.state == RunnerResultEnum.OK)
		{
			res.result = ValidationResultEnum.OK;
		}
		else if (res.runInfo.state == RunnerResultEnum.NonZeroExitCode)
		{
			switch (res.runInfo.exitCode)
			{
			case 1:
			case 3:
			case 4:
				res.result = ValidationResultEnum.WrongAnswer;
				break;
				
			case 2: 
				res.result = ValidationResultEnum.PresentationError;
				break;
			
			default:
				System.out.println("rr2");
				res.fail = ValidationFailEnum.ValidatorFail;
				res.result = ValidationResultEnum.InternalError;
			}
		}
		else
		{
			System.out.println("rr");
			res.fail = ValidationFailEnum.ValidatorFail;
			res.result = ValidationResultEnum.InternalError;
		}
	}
	
	@Override
	public String toString()
	{
		return "Validator.TestLib";
	}
}
