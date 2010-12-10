/* $Id$ */

package djudge.judge.checker.internal;


/*
 * Compares output and answer files as sequences of floating point numbers
 * Comparison is done using either relative, either absolute error (or both)   
 */
public class CheckerFloat extends CheckerToken
{
	/* Error */
	double epsilon;
	
	boolean fRelative = true;
	
	boolean fAbsolute = true;
	
	public CheckerFloat(double eps)
	{
		this.epsilon = eps;
	}
	
	public CheckerFloat(double eps, boolean doRelativeCheck, boolean doAbsoluteCheck)
	{
		this.epsilon = eps;
		fRelative = doRelativeCheck;
		fAbsolute = doAbsoluteCheck;
	}
	
	@Override
	protected boolean compareTokens(Object answerO, Object outputO)
	{
		// to skip non-numerical equal values 
		String answerStr = answerO.toString();
		String outputStr = outputO.toString();
		if (answerStr.equals(outputStr))
			return true;
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
		return "Checker-Float ["  + epsilon + ", abs: " + fAbsolute + ", rel: " + fRelative + "]";
	}
}
