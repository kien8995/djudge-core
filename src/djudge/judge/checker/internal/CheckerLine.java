/* $Id$ */

package djudge.judge.checker.internal;

import java.io.BufferedReader;
import java.io.IOException;


/* Compares files line-by-line */
public class CheckerLine extends CheckerInternalAbstract
{
	@Override
	protected Object getToken(BufferedReader rd) throws IOException
	{
		return rd.readLine();
	}
	
	@Override
	protected boolean compareTokens(Object a, Object b)
	{
		return a.equals(b);
	}
	
	@Override
	public String toString()
	{
		return "Checker-String";
	}
}
