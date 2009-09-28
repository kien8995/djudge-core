/* $Id: ValidatorType.java, v 0.1 2008/07/23 05:13:08 alt Exp $ */

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

public enum ValidatorType
{
	/**
	 * Line-by-line comparing (whitespace is counted)
	 */
	InternalExact,
	
	/**
	 * Token-by-token comparing  
	 */
	InternalToken,
	
	/**
	 * Same as 'InternalToken', but each token must present valid 32-bit signed integer value
	 */
	InternalInt32,
	
	/**
	 * Same as 'InternalToken', but each token must present valid 64-bit signed integer value
	 */
	InternalInt64,

	InternalFloatAbs,
	InternalFloatRel,
	InternalFloatAbsRel,
	ExternalTestLib,
	ExternalPC2,
	ExternalCustom,
	ExternalExitCode,
	ExternalExitCodeExtended,
	Unknown;
	
	public static ValidatorType parse(String str)
	{
		ValidatorType res = ValidatorType.Unknown;
		for (ValidatorType curr : ValidatorType.values())
		{
			if (curr.toString().equals(str))
			{
				res = curr;
			}
		}
		return res;
	}
	
	public boolean isExternal()
	{
		return this.toString().startsWith("External");
	}

	public boolean isParametrized()
	{
		return this == InternalFloatAbs || this == InternalFloatAbsRel || this == InternalFloatRel;
	}	
}
