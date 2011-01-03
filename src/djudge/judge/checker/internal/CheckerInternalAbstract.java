/* $Id$ */

package djudge.judge.checker.internal;

import java.io.*;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import djudge.judge.checker.CheckerFailEnum;
import djudge.judge.checker.CheckerResult;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.checker.CheckerAbstract;
import djudge.judge.checker.CheckerLimits;

import utils.StringTools;

/* Abstract class for all internal (built-in) validators */
public abstract class CheckerInternalAbstract extends CheckerAbstract implements CheckerLimits 
{
	private final static Logger log = Logger.getLogger(CheckerInternalAbstract.class);
	
	/* Readers for corresponding files */
	BufferedReader judgeInputReader, judgeAnswerReader, generatedOutputReader;
	
	@Override
	public CheckerResult validateOutput(String judgeInputFile, String generatedOutputFilename, String judgeAnswerFile)
	{
		long startTime = new Date().getTime();
		
		res = new CheckerResult(this.toString());
		
		// Checking whether judge input file exists
		File f = new File(judgeInputFile);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoJudgeInputFileError);
			String msg = "Cannot find judge input file: " + judgeInputFile;
			res.setCheckerOutput(new String[] {msg});
			res.setResultDetails(msg);
			log.error(msg);
			return res;
		}
		
		// Checking whether judge answer file exists 
		f = new File(judgeAnswerFile);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.NoJudgeAnswerFileError);
			String msg = "Cannot find judge answer file: " + judgeAnswerFile;
			res.setCheckerOutput(new String[] {msg});
			res.setResultDetails(msg);
			log.error(msg);
			return res;
		}
		
		// Checking whether output file exists 
		f = new File(generatedOutputFilename);
		if (!f.exists() && res.getResult() == CheckerResultEnum.Undefined)
		{
			res.setResult(CheckerResultEnum.WrongAnswer);
			res.setFail(CheckerFailEnum.OK);
			String msg = "Cannot find program's output file: " + generatedOutputFilename;
			res.setCheckerOutput(new String[] {msg});
			res.setResultDetails(msg);
			log.info(msg);
			return res;
		}
		
		// All files are present, validating data
		res.setCheckerOutput(new String[2]);
		
		try
		{
			res.setFail(CheckerFailEnum.OK);			
			judgeInputReader = new BufferedReader(new FileReader(judgeInputFile));
			judgeAnswerReader = new BufferedReader(new FileReader(judgeAnswerFile));
			generatedOutputReader = new BufferedReader(new FileReader(generatedOutputFilename));
			processData();
		}
		catch (Exception ex)
		{
			log.warn("Unknown exception", ex);
			res.setResult(CheckerResultEnum.WrongAnswer);
			res.setFail(CheckerFailEnum.OK);
			res.setResultDetails(ex.toString());
		}
		
		long finishTime = new Date().getTime();
		
		String[] array = res.getCheckerOutput();
		Vector<String> vector = new Vector<String>();
		for (String str: array)
			vector.add(str);
		vector.add(((finishTime - startTime) / 1) + " ms");
		res.setCheckerOutput(vector.toArray(new String[0]));
		
		return res;		
	}	
	
	// FIXME: Replace multiple returns with something more structured
	// FIXME: Now any exception causes WrongAnswer. This may be incorrect is some cases?
	protected void processData()
	{
		int tokensCount = 0;
		
		Object judgeAnswer = "", generatedOutput = "";
		
		try
		{
			// reading token from judge answer file
			while ((judgeAnswer = getToken(judgeAnswerReader)) != null)
			{
				tokensCount++;
				try
				{
					generatedOutput = getToken(generatedOutputReader);
					if (generatedOutput == null)
					{
						res.getCheckerOutput()[0] = "Wrong Answer";
						res.getCheckerOutput()[1] = "Answer too short: token #" + tokensCount + " not found";
						res.setResult(CheckerResultEnum.WrongAnswer);
						return;
					}
					try
					{
    					if (!compareTokens(judgeAnswer, generatedOutput))
    					{
    						res.getCheckerOutput()[0] = "Wrong Answer";
    						res.getCheckerOutput()[1] = "Token #" + tokensCount + ": [judge] '" + StringTools.truncate(judgeAnswer.toString()) + "' != '" + StringTools.truncate(generatedOutput.toString()) + "' [answer] [" + this.toString()+"]";
    						res.setResult(CheckerResultEnum.WrongAnswer);
    						return;
    					}
					}
					catch (Exception e)
					{
						res.getCheckerOutput()[0] = "Wrong Answer [" + e + "]";
						res.getCheckerOutput()[1] = "Token #" + tokensCount
								+ ": [etalon] '"
								+ StringTools.truncate(judgeAnswer.toString())
								+ "' != '"
								+ StringTools.truncate(generatedOutput.toString())
								+ "' [answer] [" + this.toString() + "]";
						res.setResult(CheckerResultEnum.WrongAnswer);
						return;
					}
				}
				catch (Exception exc)
				{
					log.warn("!!! Unknown checker exception", exc);
					res.getCheckerOutput()[0] = "Don't know:" + exc;
					res.getCheckerOutput()[1] = "Token '" + StringTools.truncate(judgeAnswer.toString()) + "' != '" + StringTools.truncate(generatedOutput.toString()) + "'";
					res.setResult(CheckerResultEnum.InternalError);
					return;				
				}
			}
			// if output still contains tokens
			if ((judgeAnswer = getToken(generatedOutputReader)) != null)
			{
				res.getCheckerOutput()[0] = "Wrong Answer";
				res.getCheckerOutput()[1] = "Extra token: '" + StringTools.truncate(judgeAnswer.toString()) + "'";				
				res.setResult(CheckerResultEnum.WrongAnswer);
				return;
			}				
		}
		catch (Exception exc)
		{
			log.warn("InternalError", exc);
			res.setFail(CheckerFailEnum.CheckerFail);
			res.setResult(CheckerResultEnum.InternalError);
			return;
		}
		res.getCheckerOutput()[0] = "OK" + (tokensCount == 1 ? "\"" + StringTools.truncate(generatedOutput.toString()) + "\"" : "");
		res.getCheckerOutput()[1] = "" + tokensCount + " token(s) compared [" + this.toString() +  "]";				
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
