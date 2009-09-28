/* $Id: Validator_FloatAbs.java, v 0.1 2008/08/05 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.BufferedReader;
import java.io.IOException;

public class Validator_FloatAbs extends ValidatorInternalAbstract 
{
	double epsilon;
	
	public Validator_FloatAbs(double eps)
	{
		this.epsilon = eps;		
	}
	
	protected String getToken(BufferedReader rd) throws IOException
	{
		return rd.readLine();
		
		/*
		StringBuffer res = new StringBuffer();
		int t = rd.read();
		// skipping whitespace
		while (t == 32 || t == 10 || t == 9 || t == 13)
		{
			t = rd.read();
		}
		// if EOF reached
		if (t == -1) return null;
		
		// reading till next whitespace character
		while (!(t == 32 || t == 10 || t == 9 || t == 13 || t == -1))
		{
			res.append((char)t);
			t = rd.read();
		}
		return res.toString();*/
	}

	protected boolean compareTokens(String judge, String answer)
	{
		double aa, bb;
		//System.out.println(" === <" + judge + ">    <"  + answer);
		while (answer.charAt(0) == ' ')
			answer = answer.substring(1);
		int k = answer.indexOf(','); 
		if (k != -1)
			answer = answer.substring(0, k) + "." + answer.substring(k + 1);
		aa = Double.parseDouble(judge);
		bb = Double.parseDouble(answer);
		return Math.abs(aa-bb) <= epsilon;
	}
	
	public String toString()
	{
		return "Validator.FloatAbs (eps=" + epsilon + ")";
	}

}
