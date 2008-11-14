package runner;

import common_data_structures.RunnerLimits;

import junit.framework.TestCase;

public class RunnerTest extends TestCase
{
	
	private String S(String s)
	{
		return "D:\\Temp\\Work\\eJudge\\DJudge\\tests\\runner\\" + s; 
	}
	
	public void testRun()
	{
		RunnerLimits limits = new RunnerLimits(5000, 100 * 1024 * 1024);
		Runner run = new Runner(limits);
		RunnerResult res;
		
		res = run.run(S("Test_Runner_Crash_AccessViolation.exe"));
		assertEquals(RunnerResultEnum.RuntimeErrorCrash, res.state);
		
		res = run.run(S("Test_Runner_Crash_DivisionByZero.exe"));
		assertEquals(RunnerResultEnum.RuntimeErrorCrash, res.state);
		
		res = run.run(S("Test_Runner_Crash_ExitCode.exe"));
		assertEquals(RunnerResultEnum.NonZeroExitCode, res.state);
		
		res = run.run(S("Test_Runner_Crash_StackOverflow.exe"));
		assertEquals(RunnerResultEnum.RuntimeErrorCrash, res.state);
		
		res = run.run(S("Test_Runner_Memory_1000MB.exe"));
		assertEquals(RunnerResultEnum.MemoryLimitExceeded, res.state);

		res = run.run(S("Test_Runner_Memory_500MB.exe"));
		assertEquals(RunnerResultEnum.MemoryLimitExceeded, res.state);

		res = run.run(S("Test_Runner_Memory_200MB.exe"));
		assertEquals(RunnerResultEnum.MemoryLimitExceeded, res.state);

		res = run.run(S("Test_Runner_Memory_100MB.exe"));
		assertEquals(RunnerResultEnum.MemoryLimitExceeded, res.state);

		res = run.run(S("Test_Runner_Memory_10MB.exe"));
		assertEquals(RunnerResultEnum.OK, res.state);

		res = run.run(S("Test_Runner_Memory_1MB.exe"));
		assertEquals(RunnerResultEnum.OK, res.state);

		res = run.run(S("Test_Runner_Memory_LimitExceeded.exe"));
		assertEquals(RunnerResultEnum.MemoryLimitExceeded, res.state);

		res = run.run(S("Test_Runner_Time_LimitExceeded.exe"));
		assertEquals(RunnerResultEnum.TimeLimitExceeeded, res.state);
		//Test_Runner_Memory_1000MB.exe
		
	}

}
