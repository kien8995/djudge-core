package djudge.judge;

import djudge.judge.dexecutor.ExecutorLimits;
import utils.Scripts;

public class Temp
{
	public static void main(String arg[]) throws Exception
	{
		//Scripts.generateProblemReport("NEERC-2009", "D");
		//Scripts.generateContestReport("@TEST");
		
		CheckParams params = new CheckParams();
		ProblemDescription pd = new ProblemDescription("NEERC-2009", "D");
		params.inputFilename = "";
		params.outputFilename = "";
		pd.overrideParams(params);
		System.out.println(Judge.judgeSourceFile("d:\\1.cpp", "GCC", pd, params).getJudgement());
	}
}
