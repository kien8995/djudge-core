/* $Id: Validator_Int32.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

package djudge.judge.validator;

public class Validator_Int32 extends Validator_Token 
{
	@Override
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
