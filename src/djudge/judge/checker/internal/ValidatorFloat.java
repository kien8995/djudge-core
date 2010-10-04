/* $Id$ */

package djudge.judge.checker.internal;


/*
 * Compares output and answer files as sequences of floating point numbers
 * Comparison is done using either relative, either absolute error (or both)   
 */
public class ValidatorFloat extends ValidatorToken
{
	/* Error */
	double epsilon;
	
	boolean fRelative = true;
	
	boolean fAbsolute = true;
	
	public ValidatorFloat(double eps)
	{
		this.epsilon = eps;
	}
	
	public ValidatorFloat(double eps, boolean doRelativeCheck, boolean doAbsoluteCheck)
	{
		this.epsilon = eps;
		fRelative = doRelativeCheck;
		fAbsolute = doAbsoluteCheck;
	}
	
	@Override
	protected boolean compareTokens(Object answerO, Object outputO)
	{
		String answer = answerO.toString();
		String output = outputO.toString().replace(',', '.');
		double j, a;
		j = Double.parseDouble(answer);
		a = Double.parseDouble(output);
		return (fRelative && Math.abs((a-j)/j) <= epsilon) || (fAbsolute && Math.abs(a-j) <= epsilon);
	}

	@Override
	public String toString()
	{
		return "Validator-Float ["  + epsilon + ", abs: " + fAbsolute + ", rel: " + fRelative + "]";
	}
}
