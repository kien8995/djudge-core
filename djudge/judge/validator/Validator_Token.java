/* $Id: Validator_String.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.IOException;
import java.io.BufferedReader;

public class Validator_Token extends ValidatorInternalAbstract 
{
	@Override
	//TODO: review code
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
			res.append((char) t);
			t = rd.read();
		}
		return res.toString();
	}

	@Override
	protected boolean compareTokens(String a, String b)
	{
		return a.equals(b);
	}

	@Override
	public String toString()
	{
		return "Validator_Token";
	}
}
