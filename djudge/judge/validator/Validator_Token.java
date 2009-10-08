/* $Id: Validator_String.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

package djudge.judge.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class Validator_Token extends ValidatorInternalAbstract 
{

	protected String getToken(BufferedReader rd) throws IOException
	{
		Scanner sc = new Scanner(rd);
		return sc.next();
	}

	protected boolean compareTokens(String a, String b)
	{
		return a.equals(b);
	}

}
