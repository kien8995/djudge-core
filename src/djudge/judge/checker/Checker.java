/* $Id$ */

package djudge.judge.checker;

import djudge.judge.checker.external.CheckerExitCode;
import djudge.judge.checker.external.CheckerExitCodeExtended;
import djudge.judge.checker.external.CheckerTestLib;
import djudge.judge.checker.external.CheckerTestLibJava;
import djudge.judge.checker.internal.CheckerFloat;
import djudge.judge.checker.internal.CheckerInt32;
import djudge.judge.checker.internal.CheckerSortedToken;
import djudge.judge.checker.internal.CheckerLine;
import djudge.judge.checker.internal.CheckerToken;

public class Checker
{
	public final static String XMLRootElement = "validator";
	
	CheckerDescription desc;
	
	public Checker()
	{
		// TODO Auto-generated constructor stub
	}
	
	public Checker(CheckerDescription desc)
	{
		this.desc = desc;
	}
	
	public CheckerResult validateOutput(String input, String output, String answer)
	{
		CheckerResult res = new CheckerResult("" + desc.type);
		switch (desc.type)
		{
		case ExternalExitCode:
			res = (new CheckerExitCode(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case ExternalExitCodeExtended:
			res = (new CheckerExitCodeExtended(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case ExternalTestLib:
			res = (new CheckerTestLib(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case ExternalTestLibJava:
			res = (new CheckerTestLibJava(desc.getCheckerPath())).validateOutput(input, output, answer);
			break;

		case InternalExact:	
			res = (new CheckerLine()).validateOutput(input, output, answer);
			break;

		case InternalInt32:
			res = (new CheckerInt32()).validateOutput(input, output, answer);
			break;

		case InternalFloatAbs:
			double eps = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps, false, true).validateOutput(input, output, answer));
			break;
		
		case InternalFloatRel:
			double eps3 = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps3, true, false).validateOutput(input, output, answer));
			break;
		
		case InternalFloatOther:
			double eps4 = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps4).validateOutput(input, output, answer));
			break;
		
		case InternalFloatAbsRel:
			double eps2 = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps2, true, true).validateOutput(input, output, answer));
			break;

		case InternalToken:
			res = (new CheckerToken()).validateOutput(input, output, answer);
			break;

		case InternalSortedToken:
			res = (new CheckerSortedToken()).validateOutput(input, output, answer);
			break;

		case ExternalPC2:
//			break;

		case InternalInt64:
//			break;

		case ExternalCustom:
//			break;

		case Unknown:
		default:
			res.setResult(CheckerResultEnum.InternalError);
			res.setFail(CheckerFailEnum.ValidatorNotFounded);
			System.out.println("Unknown validator: " + desc.type);
		}

		return res;
	}	
}
