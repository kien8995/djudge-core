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

package com.alt.djudge.judge.validator;

import com.alt.djudge.judge.executor.RunnerResultEnum;

public class ValidatorTestLib extends ValidatorExternalAbstract
{
	public ValidatorTestLib(String ExeFile) 
	{
		super(ExeFile);
	}

	protected void processData() 
	{
	//
		if (res.RunInfo.state == RunnerResultEnum.OK)
		{
			res.Result = ValidationResultEnum.OK;
		}
		else if (res.RunInfo.state == RunnerResultEnum.NonZeroExitCode)
		{
			switch (res.RunInfo.exitCode)
			{
			case 1:
			case 3:
			case 4:
				res.Result = ValidationResultEnum.WrongAnswer;
				break;
				
			case 2: 
				res.Result = ValidationResultEnum.PresentationError;
				break;
			
			default:
				System.out.println("rr2");
				res.Fail = ValidationFailEnum.ValidatorFail;
				res.Result = ValidationResultEnum.InternalError;
			}
		}
		else
		{
			System.out.println("rr");
			res.Fail = ValidationFailEnum.ValidatorFail;
			res.Result = ValidationResultEnum.InternalError;
		}
	}
		
}
