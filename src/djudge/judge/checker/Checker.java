/* $Id$ */

package djudge.judge.checker;

import org.apache.log4j.Logger;

import djudge.judge.checker.external.CheckerExitCode;
import djudge.judge.checker.external.CheckerExitCodeExtended;
import djudge.judge.checker.external.CheckerTestLib;
import djudge.judge.checker.external.CheckerTestLibJava;
import djudge.judge.checker.internal.CheckerFloat;
import djudge.judge.checker.internal.CheckerInt32;
import djudge.judge.checker.internal.CheckerSortedString;
import djudge.judge.checker.internal.CheckerSortedToken;
import djudge.judge.checker.internal.CheckerLine;
import djudge.judge.checker.internal.CheckerToken;

public class Checker
{
	private final static Logger log = Logger.getLogger(Checker.class);
	
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
	
	/*
	 * @param input  - judge-generated-input
	 * @param output - solution-generated-output
	 * @param answer - judge-generated-answer
	 */
	public CheckerResult validateOutput(String judgeInputFilename, String generatedOutputFile, String judgeAnswerFilename)
	{
		CheckerResult res = new CheckerResult("" + desc.type);
		switch (desc.type)
		{
		case ExternalExitCode:
			res = (new CheckerExitCode(desc.getCheckerPath())).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
			break;

		case ExternalExitCodeExtended:
			res = (new CheckerExitCodeExtended(desc.getCheckerPath())).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
			break;

		case ExternalTestLib:
			res = (new CheckerTestLib(desc.getCheckerPath())).validateOutput(judgeInputFilename,generatedOutputFile, judgeAnswerFilename);
			break;

		case ExternalTestLibJava:
			res = (new CheckerTestLibJava(desc.getCheckerPath())).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
			break;

		case InternalExact:	
			res = (new CheckerLine()).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
			break;

		case InternalInt32:
			res = (new CheckerInt32()).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
			break;

		case InternalFloatAbs:
			double eps = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps, false, true).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename));
			break;
		
		case InternalFloatRel:
			double eps3 = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps3, true, false).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename));
			break;
		
		case InternalFloatOther:
			double eps4 = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps4).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename));
			break;
		
		case InternalFloatAbsRel:
			double eps2 = Double.parseDouble(desc.param);
			res = (new CheckerFloat(eps2, true, true).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename));
			break;

		case InternalToken:
			res = (new CheckerToken()).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
			break;

		case InternalSortedToken:
			res = (new CheckerSortedToken()).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
			break;

		case InternalSortedExact:
			res = (new CheckerSortedString()).validateOutput(judgeInputFilename, generatedOutputFile, judgeAnswerFilename);
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
			res.setFail(CheckerFailEnum.CheckerNotFound);
			log.error("Unknown validator: " + desc.type);
		}

		return res;
	}	
}
