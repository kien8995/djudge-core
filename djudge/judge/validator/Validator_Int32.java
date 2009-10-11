/* $Id: Validator_Int32.java, v 0.1 2008/07/24 05:13:08 alt Exp $ */

package djudge.judge.validator;

public class Validator_Int32 extends Validator_Token 
{
	@Override
	protected boolean compareTokens(Object a, Object b)
	{
		return Integer.parseInt(a.toString()) == Integer.parseInt(b.toString());
	}

	@Override
	public String toString()
	{
		return "Validator_Int32";
	}
}
