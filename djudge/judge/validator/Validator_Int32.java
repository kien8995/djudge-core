/* $Id: Validator_Int32.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.BufferedReader;
import java.io.IOException;

public class Validator_Int32 extends ValidatorInternalAbstract 
{

	// FIXME add checking for non-numeric symbols
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
		if (t == '-')
		{
			res.append((char)t);
			t = rd.read();
		}
		while (t >= '0' && t <= '9')
		{
			res.append(t);
			t = rd.read();
		}
		// if not a whitespace character after number
		if (!(t == 32 || t == 10 || t == 9 || t == 13 || t == -1))
			return null;

		return res.toString();
	}

	protected boolean compareTokens(String a, String b)
	{
		return Integer.parseInt(a) == Integer.parseInt(b);
	}

	@Override
	public String toString()
	{
		return "Validator_Int32";
	}
}
