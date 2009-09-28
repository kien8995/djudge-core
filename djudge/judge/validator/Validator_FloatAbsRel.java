/* $Id: Validator_FloatAbsRel.java, v 0.1 2008/08/06 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.BufferedReader;
import java.io.IOException;

public class Validator_FloatAbsRel extends ValidatorInternalAbstract 
{
	double epsilon;
	
	public Validator_FloatAbsRel(double eps)
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
		return Math.abs((a-j)/j) <= epsilon || Math.abs(a-j) <= epsilon;
	}

}
