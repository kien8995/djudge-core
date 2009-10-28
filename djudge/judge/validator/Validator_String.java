/* $Id: Validator_String.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.BufferedReader;
import java.io.IOException;

public class Validator_String extends ValidatorInternalAbstract
{
	@Override
	protected String getToken(BufferedReader rd) throws IOException
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
		return "Validator_String";
	}
}
