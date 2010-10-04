/* $Id$ */

package djudge.judge.checker.internal;

import java.io.IOException;
import java.io.BufferedReader;


/* 
 * Compares files token-by-token
 * i.e., it skips whitespace characters
 */
public class CheckerToken extends CheckerInternalAbstract 
{
	//TODO: review code
	@Override
	protected Object getToken(BufferedReader rd) throws IOException
	{
		StringBuffer res = new StringBuffer();
		int t = rd.read();
		// skipping whitespace
		while (t == 32 || t == 10 || t == 9 || t == 13)
		{
			t = rd.read();
		}
		// if EOF reached
		if (t == -1)
			return null;
		
		// reading till next whitespace character
		while (!(t == 32 || t == 10 || t == 9 || t == 13 || t == -1))
		{
			res.append((char) t);
			t = rd.read();
		}
		return res.toString();
	}

	@Override
	protected boolean compareTokens(Object a, Object b)
	{
		return a.equals(b);
	}

	@Override
	public String toString()
	{
		return "Checker-Token";
	}
}
