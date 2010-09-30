/* $Id$ */

package djudge.judge.validator;

/*
 * Compares output and answer files as sequences of 32-bit signed integers
 */
public class ValidatorInt32 extends ValidatorToken 
{
	@Override
	protected boolean compareTokens(Object a, Object b)
	{
		return Integer.parseInt(a.toString()) == Integer.parseInt(b.toString());
	}

	@Override
	public String toString()
	{
		return "Validator-Int32";
	}
}
