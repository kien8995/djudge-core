/* $Id$ */

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

/**
 * Base class for all validators
 * @author alt
 */
public abstract class ValidatorAbstract 
{
	// Provides information about validation result
	ValidationResult res;
	
	protected String exeFile;
	
	public String getExeFile()
	{
		return exeFile;
	}
	
	/**
	 * Performs validation of result 
	 * @param input Judge input file
	 * @param output Judge output file
	 * @param answer Program output file
	 * @return object of type ValidationResult 
	 */
	public abstract ValidationResult validateOutput(String input, String output, String answer);
	
}
