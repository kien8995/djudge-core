/* $Id$ */

package djudge.judge.checker.internal;

import java.io.*;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResult;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.checker.CheckerAbstract;
import djudge.judge.checker.CheckerLimits;

import utils.StringWorks;

/* Abstract class for all internal (built-in) validators */
public abstract class CheckerInternalAbstract extends CheckerAbstract implements CheckerLimits 
{
	/* Readers for corresponding files */
	BufferedReader judgeInput, judgeAnswer, programOutput;
	
	@Override
	public CheckerResult validateOutput(String judgeInputFile, String judgeAnswerFile, String programOutputFile)
	{
		res = new CheckerResult(this.toString());
		
		// Checking whether input file exists
		File f = new File(judgeInputFile);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoInputFileError);
			res.setValidatorOutput(new String[]{"Cannot find input file: " + judgeInputFile});
			res.setResultDetails("Cannot find input file: " + judgeInputFile);
			return res;
		}
		
		// Checking whether output file exists 
		f = new File(judgeAnswerFile);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoOutputFileError);
			res.setValidatorOutput(new String[]{"Cannot find answer file: " + judgeAnswerFile});
			res.setResultDetails("Cannot find answer file: " + judgeAnswerFile);
			return res;
		}
		
		// Checking whether answer file exists 
		f = new File(programOutputFile);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.WrongAnswer);
			res.setFail(CheckerFailEnum.OK);
			res.setValidatorOutput(new String[] {"Cannot find program's output file: " + programOutputFile});
			res.setResultDetails("Cannot find program's output file: " + programOutputFile);
			return res;
		}
		
		// All files are present, validating data
		res.setValidatorOutput(new String[2]);
		
		try
		{
			res.setFail(CheckerFailEnum.OK);			
			judgeInput = new BufferedReader(new FileReader(judgeInputFile));
			judgeAnswer = new BufferedReader(new FileReader(judgeAnswerFile));
			programOutput = new BufferedReader(new FileReader(programOutputFile));
			processData();
		}
		catch (Exception exc)
		{
			res.setResult(CheckerResultEnum.WrongAnswer);
			res.setFail(CheckerFailEnum.OK);
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
						res.getValidatorOutput()[0] = "Wrong Answer";
						res.getValidatorOutput()[1] = "Answer too short: token #" + cTokens + " not founded";
						res.setResult(CheckerResultEnum.WrongAnswer);
						return;
					}
					try
					{
    					if (!compareTokens(correct, guess))
    					{
    						res.getValidatorOutput()[0] = "Wrong Answer";
    						res.getValidatorOutput()[1] = "Token #" + cTokens + ": [etalon] '" + StringWorks.truncate(correct.toString()) + "' != '" + StringWorks.truncate(guess.toString()) + "' [answer] [" + this.toString()+"]";
    						res.setResult(CheckerResultEnum.WrongAnswer);
    						return;
    					}
					}
					catch (Exception e)
					{
						res.getValidatorOutput()[0] = "Wrong Answer [" + e + "]";
						res.getValidatorOutput()[1] = "Token #" + cTokens
								+ ": [etalon] '"
								+ StringWorks.truncate(correct.toString())
								+ "' != '"
								+ StringWorks.truncate(guess.toString())
								+ "' [answer] [" + this.toString() + "]";
						res.setResult(CheckerResultEnum.WrongAnswer);
						return;
					}
				}
				catch (Exception exc)
				{
					res.getValidatorOutput()[0] = "Don't know:" + exc;
					res.getValidatorOutput()[1] = "Token '" + StringWorks.truncate(correct.toString()) + "' != '" + StringWorks.truncate(guess.toString()) + "'";
					res.setResult(CheckerResultEnum.InternalError);
					return;				
				}
			}
			// if output still contains tokens
			if ((correct = getToken(programOutput)) != null)
			{
				res.getValidatorOutput()[0] = "Wrong Answer";
				res.getValidatorOutput()[1] = "Extra token: '" + StringWorks.truncate(correct.toString()) + "'";				
				res.setResult(CheckerResultEnum.WrongAnswer);
				return;
			}				
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			res.setFail(CheckerFailEnum.ValidatorFail);
			res.setResult(CheckerResultEnum.InternalError);
			return;
		}
		res.getValidatorOutput()[0] = "OK" + (cTokens == 1 ? "\"" + StringWorks.truncate(guess.toString()) + "\"" : "");
		res.getValidatorOutput()[1] = "" + cTokens + " token(s) compared [" + this.toString() +  "]";				
		res.setResult(CheckerResultEnum.OK);
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
