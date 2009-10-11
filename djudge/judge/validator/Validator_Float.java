/* $Id: Validator_FloatAbsRel.java, v 0.1 2008/08/06 05:13:08 alt Exp $ */

package djudge.judge.validator;

public class Validator_Float extends Validator_Token
{
	double epsilon;
	
	boolean fRelative = true;
	
	boolean fAbsolute = true;
	
	public Validator_Float(double eps)
	{
		this.epsilon = eps;
	}
	
	public Validator_Float(double eps, boolean doRelativeCheck, boolean doAbsoluteCheck)
	{
		this.epsilon = eps;
		fRelative = doRelativeCheck;
		fAbsolute = doAbsoluteCheck;
	}
	
	@Override
	protected boolean compareTokens(String answer, String output)
	{
		try
		{
			double j, a;
			int k = output.indexOf(','); 
			if (k != -1)
				output = output.substring(0, k) + "." + output.substring(k + 1);
			j = Double.parseDouble(answer);
			a = Double.parseDouble(output);
			return (fRelative && Math.abs((a-j)/j) <= epsilon) || (fAbsolute && Math.abs(a-j) <= epsilon);
		}
		catch (Exception e)
		{
			return answer.equals(output);
		}
	}

	@Override
	public String toString()
	{
		return "Validator_Float ["  + epsilon + ", abs: " + fAbsolute + ", rel: " + fRelative + "]";
	}
}
