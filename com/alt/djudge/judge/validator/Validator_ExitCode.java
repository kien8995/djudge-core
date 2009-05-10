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

package com.alt.djudge.judge.validator;

import com.alt.djudge.judge.runner.*;

public class Validator_ExitCode extends ValidatorExternalAbstract
{
	public Validator_ExitCode(String ExeFile) 
	{
		super(ExeFile);
	}
	
	protected void processData()
	{
		if (res.RunInfo.state == RunnerResultEnum.OK)
		{
			res.Result = ValidationResultEnum.OK;
		}
		else if (res.RunInfo.state == RunnerResultEnum.NonZeroExitCode)
		{
			res.Result = ValidationResultEnum.WrongAnswer;
		}
		else
		{
			res.Result = ValidationResultEnum.InternalError;
			res.Fail = ValidationFailEnum.ValidatorFail;			
		}
	}
}
