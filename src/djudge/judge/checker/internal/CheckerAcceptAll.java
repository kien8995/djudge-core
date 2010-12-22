/* $Id$ */

package djudge.judge.checker.internal;

import java.io.BufferedReader;
import java.io.IOException;


public class CheckerAcceptAll extends CheckerInternalAbstract 
{
	@Override
	protected Object getToken(BufferedReader rd) throws IOException
	{
		return null;
	}
	
	@Override
	protected boolean compareTokens(Object a, Object b)
	{
		return true;
	}

	@Override
	public String toString()
	{
		return "Checker-AcceptAll";
	}
}
