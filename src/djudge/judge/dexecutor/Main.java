/* $Id$ */

package djudge.judge.dexecutor;


public class Main
{
	public static void main(String[] args)
	{
		LocalExecutor exec = new LocalExecutor();
		ExecutorProgram pr = new ExecutorProgram();
		pr.command = "calc.exe";
		ExecutionResult r = exec.execute(new ExecutorTask(pr, new ExecutorLimits(1000), new ExecutorFiles()));
		System.out.println(r.runnerOutput);
		//exec.execute(new ExecutorTask());
	}
}
