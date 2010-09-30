/* $Id$ */

package djudge.judge.validator;

import java.io.*;

import utils.StringWorks;

/* Abstract class for all internal (built-in) validators */
public abstract class ValidatorInternalAbstract extends ValidatorAbstract implements ValidatorLimits 
{
	/* Readers for corresponding files */
	BufferedReader judgeInput, judgeAnswer, programOutput;
	
	@Override
	public ValidationResult validateOutput(String judgeInputFile, String judgeAnswerFile, String programOutputFile)
	{
		res = new ValidationResult(this.toString());
		
		// Checking whether input file exists
		File f = new File(judgeInputFile);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.NoInputFileError;
			res.validatorOutput = new String[]{"Cannot find input file: " + judgeInputFile};
			return res;
		}
		
		// Checking whether output file exists 
		f = new File(judgeAnswerFile);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.NoOutputFileError;
			res.validatorOutput = new String[]{"Cannot find answer file: " + judgeAnswerFile};
			return res;
		}
		
		// Checking whether answer file exists 
		f = new File(programOutputFile);
		if (!f.exists() && res.result == ValidationResultEnum.Undefined)
		{
			res.result = ValidationResultEnum.WrongAnswer;
			res.fail = ValidationFailEnum.OK;
			res.validatorOutput = new String[]{"Cannot find program's output file: " + programOutputFile};
			return res;
		}
		
		// All files are present, validating data
		res.validatorOutput = new String[2];
		
		try
		{
			res.fail = ValidationFailEnum.OK;			
			judgeInput = new BufferedReader(new FileReader(judgeInputFile));
			judgeAnswer = new BufferedReader(new FileReader(judgeAnswerFile));
			programOutput = new BufferedReader(new FileReader(programOutputFile));
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
		
		Object correct = "", guess = "";
		
		try
		{
			// reading token from answer file
			while ((correct = getToken(judgeAnswer)) != null)
			{
				cTokens++;
				try
				{
					guess = getToken(programOutput);
					if (guess == null)
					{
						res.validatorOutput[0] = "Wrong Answer";
						res.validatorOutput[1] = "Answer too short: token #" + cTokens + " not founded";
						res.result = ValidationResultEnum.WrongAnswer;
						return;
					}
					try
					{
    					if (!compareTokens(correct, guess))
    					{
    						res.validatorOutput[0] = "Wrong Answer";
    						res.validatorOutput[1] = "Token #" + cTokens + ": [etalon] '" + StringWorks.truncate(correct.toString()) + "' != '" + StringWorks.truncate(guess.toString()) + "' [answer] [" + this.toString()+"]";
    						res.result = ValidationResultEnum.WrongAnswer;
    						return;
    					}
					}
					catch (Exception e)
					{
						res.validatorOutput[0] = "Wrong Answer [" + e + "]";
						res.validatorOutput[1] = "Token #" + cTokens
								+ ": [etalon] '"
								+ StringWorks.truncate(correct.toString())
								+ "' != '"
								+ StringWorks.truncate(guess.toString())
								+ "' [answer] [" + this.toString() + "]";
						res.result = ValidationResultEnum.WrongAnswer;
						return;
					}
				}
				catch (Exception exc)
				{
					res.validatorOutput[0] = "Don't know:" + exc;
					res.validatorOutput[1] = "Token '" + StringWorks.truncate(correct.toString()) + "' != '" + StringWorks.truncate(guess.toString()) + "'";
					res.result = ValidationResultEnum.InternalError;
					return;				
				}
			}
			// if output still contains tokens
			if ((correct = getToken(programOutput)) != null)
			{
				res.validatorOutput[0] = "Wrong Answer";
				res.validatorOutput[1] = "Extra token: '" + StringWorks.truncate(correct.toString()) + "'";				
				res.result = ValidationResultEnum.WrongAnswer;
				return;
			}				
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			res.fail = ValidationFailEnum.ValidatorFail;
			res.result = ValidationResultEnum.InternalError;
			return;
		}
		res.validatorOutput[0] = "OK" + (cTokens == 1 ? "\"" + StringWorks.truncate(guess.toString()) + "\"" : "");
		res.validatorOutput[1] = "" + cTokens + " token(s) compared [" + this.toString() +  "]";				
		res.result = ValidationResultEnum.OK;
	}

	/**
	 * Provides next token for current validator (integer number, word, line, etc)
	 * @param rd Stream to read from
	 * @return Next token if available, null otherwise
	 * @throws IOException
	 */
	protected abstract Object getToken(BufferedReader rd) throws IOException;

	/**
	 * Compares two tokens for equality
	 * Tokens are passing as strings but actually they can be integer, real numbers or other 
	 * @param a First token
	 * @param b Second token
	 * @return (a == b)
	 */
	protected abstract boolean compareTokens(Object a, Object b);

	
}
