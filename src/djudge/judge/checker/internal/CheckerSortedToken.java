/* $Id$ */

package djudge.judge.checker.internal;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Vector;


public class CheckerSortedToken extends CheckerToken 
{
	protected Object getToken(BufferedReader rd) throws IOException
	{
		Object s;
		Vector<String> res = new Vector<String>();
		while (null != (s = super.getToken(rd)))
		{
			res.add(s.toString());
		}
		String[] r = res.toArray(new String[0]);
		Arrays.sort(r);
		return r.length > 0 ? r : null;
	}

	@Override
	protected boolean compareTokens(Object a, Object b)
	{
		String[] aa = (String[]) a;
		String[] bb = (String[]) b;
		if (aa.length != bb.length)
			return false;
		for (int i = 0; i < aa.length; i++)
			if (!aa[i].equals(bb[i]))
				return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Checker-SortedToken";
	}
}
