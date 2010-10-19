/* $Id$ */

package djudge.judge;

import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.executor.ExecutionResultEnum;

class TestResultEnumFactory
{
	static TestResultEnum getResult(ExecutionResultEnum res)
	{
		switch (res)
		{
		case MemoryLimitExceeded:
			return TestResultEnum.MLE;
		
		case TimeLimitExceeeded:
			return TestResultEnum.TLE;
			
		case OutputLimitExceeded:
			return TestResultEnum.OLE;
		
		case InternalError:
			return TestResultEnum.IE;
			
		case Undefined:
			return TestResultEnum.Undefined;
			
		case NonZeroExitCode:
		case RuntimeErrorAccessViolation:
		case RuntimeErrorCrash:
		case RuntimeErrorGeneral:
			return TestResultEnum.RE;
			
		case SecurityViolation:
			return TestResultEnum.SV;
			
		case OK:
			return TestResultEnum.AC;
		}
		
		return TestResultEnum.Undefined;
	}
	
	static TestResultEnum getResult(CheckerResultEnum res)
	{
		switch (res)
		{
		case InternalError:
			return TestResultEnum.IE;

		case OK:
			return TestResultEnum.AC;

		case PresentationError:
		case WrongAnswer:
			return TestResultEnum.WA;
		
		}
		return TestResultEnum.Undefined;
	}
	
}

public enum TestResultEnum
{
	// Accepted
	AC,
	
	// Compilation Error	
	CE,
	
	// Wrong Answer
	WA,
	
	// Time Limit Exceeded
	TLE,
	
	// Memory Limit Exceeded
	MLE,
	
	// Output Limit Exceeded
	OLE,
	
	// Runtime Error
	RE,
	
	// Internal Error
	IE,
	
	// Checker Error
	CheckerError,
	
	// Problem Error
	ProblemError,
	
	// Security Violation
	SV,
	
	// Undefined
	Undefined,
}
