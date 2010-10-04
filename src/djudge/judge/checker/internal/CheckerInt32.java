/* $Id$ */

package djudge.judge.checker.internal;


/*
 * Compares output and answer files as sequences of 32-bit signed integers
 */
public class CheckerInt32 extends CheckerToken 
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
