/* $Id: ValidatorInternalAbstract.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

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

import utils.StringWorks;





public abstract class ValidatorInternalAbstract extends ValidatorAbstract implements ValidatorLimits 
{
//	String param;
	
	BufferedReader in, out, ans;
	
	public ValidationResult Validate(String input, String output, String answer)
	{
		res = new ValidationResult(this.toString());
		
		// Checking whether input file exists		
		File f = new File(input);
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
		
		// All files are present, validating data

		res.validatorOutput = new String[2];
		
		try
		{
			res.fail = ValidationFailEnum.OK;			
			in = new BufferedReader(new FileReader(input));
			out = new BufferedReader(new FileReader(output));
			ans = new BufferedReader(new FileReader(answer));
			processData();
		}
		catch (Exception exc)
		{
			res.result = ValidationResultEnum.WrongAnswer;
			res.fail = ValidationFailEnum.OK;
		}
		
		return res;		
	}	
	
	// FIXME: Replace multiple returns with something more structured
	// FIXME: Now any exception causes WrongAnswer. This may be incorrect is some cases?
	protected void processData()
	{
		int cTokens = 0;
		
		String line = "", str = "";
		
		try
		{
			while ((line = getToken(out)) != null)
			{
				cTokens++;
				try
				{
					str = getToken(ans);
					if (str == null)
					{
						res.validatorOutput[0] = "Wrong Answer";
						res.validatorOutput[1] = "Answer too short: token #" + cTokens + " not founded";
						res.result = ValidationResultEnum.WrongAnswer;
						return;						
					}
					try
					{
    					if (!compareTokens(line, str))
    					{
    						res.validatorOutput[0] = "Wrong Answer";
    						res.validatorOutput[1] = "Token #" + cTokens + ": [etalon] '" + StringWorks.truncate(line) + "' != '" + StringWorks.truncate(str) + "' [answer] [" + this.toString()+"]";
    						res.result = ValidationResultEnum.WrongAnswer;
    						return;
    					}
					}
					catch (Exception e)
					{
						res.validatorOutput[0] = "Wrong Answer [" + e + "]";
						res.validatorOutput[1] = "Token #" + cTokens + ": [etalon] '" + StringWorks.truncate(line) + "' != '" + StringWorks.truncate(str) + "' [answer] [" + this.toString()+"]";
						res.result = ValidationResultEnum.WrongAnswer;
						return;
					}
				}
				catch (Exception exc)
				{
					res.validatorOutput[0] = "Don't know:" + exc;
					res.validatorOutput[1] = "Token '" + StringWorks.truncate(line) + "' != '" + StringWorks.truncate(str) + "'";
					res.result = ValidationResultEnum.WrongAnswer;
					return;				
				}
			}
			if ((line = getToken(ans)) != null)
			{
				res.validatorOutput[0] = "Wrong Answer";
				res.validatorOutput[1] = "Extra token: '" + StringWorks.truncate(line) + "'";				
				res.result = ValidationResultEnum.WrongAnswer;
				return;
			}				
		}
		catch (Exception exc)
		{
			res.fail = ValidationFailEnum.ValidatorFail;
			res.result = ValidationResultEnum.InternalError;
			return;
		}
		res.validatorOutput[0] = "OK" + (cTokens == 1 ? "\"" + StringWorks.truncate(str) + "\"" : "");
		res.validatorOutput[1] = "" + cTokens + " tokens compared [" + this.toString() +  "]";				
		res.result = ValidationResultEnum.OK;
	}

	/**
	 * Provides next token for current validator (integer number, word, line, etc)
	 * @param rd Stream to read from
	 * @return Next token if available, null otherwise
	 * @throws IOException
	 */
	protected abstract String getToken(BufferedReader rd) throws IOException;

	/**
	 * Compares two tokens for equality
	 * Tokens are passing as strings but actually they can be integer, real numbers or other 
	 * @param a First token
	 * @param b Second token
	 * @return (a == b)
	 */
	protected abstract boolean compareTokens(String a, String b);

	
}
