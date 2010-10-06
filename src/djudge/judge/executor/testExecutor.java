/* $Id$ */

package djudge.judge.executor;

import djudge.judge.dcompiler.DistributedFileset;
import junit.framework.TestCase;

public class testExecutor extends TestCase
{
	LocalExecutor ex;
	
	@Override
	public void setUp()
	{
		ex = new LocalExecutor();
	}

	public ExecutorTask createTask(String exeName, int time, int memory)
	{
		ExecutorTask task = new ExecutorTask();
		task.files = new ExecutorFiles();
		task.limits = new ExecutorLimits(time, memory);
		task.returnDirectoryContent = false;
		task.program = new ExecutorProgram();
		task.program.command = exeName;
		task.program.files = new DistributedFileset("./tests/runner/" + exeName);
		return task;
	}
	
	public ExecutorTask createTask(String exeName)
	{
		return createTask(exeName, 1000, 1024 * 1024 * 64);
	}
	
	public void testOK()
	{
		
		assertEquals(ExecutionResultEnum.OK, ex.execute(createTask(
				"Test_Runner_Memory_1MB.exe", 1000, 2 * 1024 * 1024)).result);
		assertEquals(
				ExecutionResultEnum.OK,
				ex.execute(createTask("Test_Runner_Memory_10MB.exe", 500, 1024 * 1024 * 12)).result);
		assertEquals(
				ExecutionResultEnum.OK,
				ex.execute(createTask("Test_Runner_Memory_100MB.exe", 10000, 1024 * 1024 * 102)).result);
		assertEquals(
				ExecutionResultEnum.OK,
				ex.execute(createTask("Test_Runner_Memory_200MB.exe", 10000, 1024 * 1024 * 202)).result);
		
	}
	
	public void testRE()
	{
		LocalExecutor ex;
		ex = new LocalExecutor();
		assertEquals(
				ExecutionResultEnum.RuntimeErrorGeneral,
				ex.execute(createTask("Test_Runner_Crash_AccessViolation.exe")).result);
		assertEquals(
				ExecutionResultEnum.RuntimeErrorGeneral,
				ex.execute(createTask("Test_Runner_Crash_DivisionByZero.exe")).result);
		assertEquals(
				ExecutionResultEnum.NonZeroExitCode,
				ex.execute(createTask("Test_Runner_Crash_ExitCode.exe")).result);
		assertEquals(
				ExecutionResultEnum.RuntimeErrorGeneral,
				ex.execute(createTask("Test_Runner_Crash_StackOverflow.exe")).result);
	}

	public void testTLE()
	{
		LocalExecutor ex;
		ex = new LocalExecutor();
		assertEquals(
				ExecutionResultEnum.TimeLimitExceeeded,
				ex.execute(createTask("Test_Runner_Time_LimitExceeded.exe")).result);
		assertEquals(
				ExecutionResultEnum.TimeLimitExceeeded,
				ex.execute(createTask("Test_Runner_Time_LimitExceeded_InputLock.exe")).result);
	}	

	public void testMLE()
	{
		LocalExecutor ex;
		ex = new LocalExecutor();
		assertEquals(
				ExecutionResultEnum.MemoryLimitExceeded,
				ex.execute(createTask("Test_Runner_Memory_1MB.exe", 500, 1024 * 1024 * 1)).result);
		assertEquals(
				ExecutionResultEnum.MemoryLimitExceeded,
				ex.execute(createTask("Test_Runner_Memory_10MB.exe", 500, 1024 * 1024 * 10)).result);
		assertEquals(
				ExecutionResultEnum.MemoryLimitExceeded,
				ex.execute(createTask("Test_Runner_Memory_100MB.exe", 10000, 1024 * 1024 * 100)).result);
		assertEquals(
				ExecutionResultEnum.MemoryLimitExceeded,
				ex.execute(createTask("Test_Runner_Memory_200MB.exe", 10000, 1024 * 1024 * 200)).result);
	}	
	
}
