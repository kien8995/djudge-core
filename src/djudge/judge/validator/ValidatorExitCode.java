/* $Id: ValidatorExitCode.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

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

import djudge.judge.executor.*;

public class ValidatorExitCode extends ValidatorExternalAbstract
{
	public ValidatorExitCode(String ExeFile) 
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
			res.result = ValidationResultEnum.WrongAnswer;
		}
		else
		{
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.ValidatorFail;			
		}
	}
}
