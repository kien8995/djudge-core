/* $Id: Validator_FloatRel.java, v 0.1 2008/08/05 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.BufferedReader;
import java.io.IOException;

public class Validator_FloatRel extends ValidatorInternalAbstract 
{
	double epsilon;
	
	public Validator_FloatRel(double eps)
	{
		this.epsilon = eps;
	}
	
	protected String getToken(BufferedReader rd) throws IOException
	{
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
		return res.toString();
	}

	protected boolean compareTokens(String judge, String answer)
	{
		double j, a;
		int k = answer.indexOf(','); 
		if (k != -1)
			answer = answer.substring(0, k) + "." + answer.substring(k + 1);
		j = Double.parseDouble(judge);
		a = Double.parseDouble(answer);
		if (Math.abs(j) < 1E-9)
			return Math.abs(a-j) <= epsilon;
		return Math.abs((a-j)/j) <= epsilon;
	}

	@Override
	public String toString()
	{
		return "Validator_FloatRel ["  + epsilon + "]";
	}
}
