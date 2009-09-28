/* $Id: Validator_String.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.BufferedReader;
import java.io.IOException;

public class Validator_String extends ValidatorInternalAbstract 
{

	protected String getToken(BufferedReader rd) throws IOException
	{
		return rd.readLine();
	}

	protected boolean compareTokens(String a, String b)
	{
		return a.equals(b);
	}

}
