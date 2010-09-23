/* $Id$ */

package djudge.judge;

import utils.Scripts;

public class Temp
{
	public static void main(String arg[]) throws Exception
	{
		Scripts.generateProblemReport("ORSPC-2009", "C");
		//Scripts.generateContestReport("@TEST");
		
		/*CheckParams params = new CheckParams();
		ProblemDescription pd = new ProblemDescription("NEERC-2009", "D");
		params.inputFilename = "";
		params.outputFilename = "";
		pd.overrideParams(params);
		System.out.println(Judge.judgeSourceFile("d:\\1.cpp", "GCC", pd, params).getJudgement());*/
	}
}
