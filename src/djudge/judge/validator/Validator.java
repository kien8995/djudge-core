/* $Id$ */

package djudge.judge.validator;

public class Validator
{
	public final static String XMLRootElement = "validator";
	
	ValidatorDescription desc;
	
	public Validator()
	{
		// TODO Auto-generated constructor stub
	}
	
	public Validator(ValidatorDescription desc)
	{
		this.desc = desc;
	}
	
	public ValidationResult validateOutput(String input, String output, String answer)
	{
		ValidationResult res = new ValidationResult("" + desc.type);
		switch (desc.type)
		{
		case ExternalExitCode:
			res = (new ValidatorExitCode(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case ExternalExitCodeExtended:
			res = (new ValidatorExitCodeExtended(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case ExternalTestLib:
			res = (new ValidatorTestLib(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case ExternalTestLibJava:
			res = (new ValidatorTestLibJava(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case InternalExact:	
			res = (new ValidatorString()).validateOutput(input, output, answer);
			break;

		case InternalInt32:
			res = (new ValidatorInt32()).validateOutput(input, output, answer);
			break;

		case InternalFloatAbs:
			double eps = Double.parseDouble(desc.param);
			res = (new ValidatorFloat(eps, false, true).validateOutput(input, output, answer));
			break;
		
		case InternalFloatRel:
			double eps3 = Double.parseDouble(desc.param);
			res = (new ValidatorFloat(eps3, true, false).validateOutput(input, output, answer));
			break;
		
		case InternalFloatOther:
			double eps4 = Double.parseDouble(desc.param);
			res = (new ValidatorFloat(eps4).validateOutput(input, output, answer));
			break;
		
		case InternalFloatAbsRel:
			double eps2 = Double.parseDouble(desc.param);
			res = (new ValidatorFloat(eps2, true, true).validateOutput(input, output, answer));
			break;

		case InternalToken:
			res = (new ValidatorToken()).validateOutput(input, output, answer);
			break;

		case InternalSortedToken:
			res = (new ValidatorSortedToken()).validateOutput(input, output, answer);
			break;

		case ExternalPC2:
//			break;

		case InternalInt64:
//			break;

		case ExternalCustom:
//			break;

		case Unknown:
		default:
			res.setResult(ValidationResultEnum.InternalError);
			res.setFail(ValidationFailEnum.ValidatorNotFounded);
			System.out.println("Unknown validator: " + desc.type);
		}

		return res;
	}	
}
