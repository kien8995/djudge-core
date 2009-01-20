package judge;

import runner.RunnerResultEnum;
import validator.ValidationResultEnum;

class TestResultEnumFactory
{
	static TestResultEnum getResult(RunnerResultEnum res)
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
	
	static TestResultEnum getResult(ValidationResultEnum res)
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
	AC,
	WA,
	TLE,
	MLE,
	OLE,
	RE,
	IE,
	SV,
	Undefined
}
