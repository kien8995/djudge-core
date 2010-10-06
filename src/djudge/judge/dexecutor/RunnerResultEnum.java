/* $Id$ */

package djudge.judge.dexecutor;

/**
 * Enumeration with possible run results
 * @author alt
 */
public enum RunnerResultEnum 
{
	// Program successfully exited
	OK,
	// Program terminated with non-zero exit code
	NonZeroExitCode,
	// Undefined
	Undefined,
	// Some other situation
	Other,
	// Program terminated due time limit
	TimeLimitExceeeded,
	// Program terminated due memory limit
	MemoryLimitExceeded,
	// Program terminated due output limit
	OutputLimitExceeded,
	// Some runtime error
	RuntimeErrorGeneral,
	// Runtime error - Crash
	RuntimeErrorCrash,
	// Runtime error - Access Violation
	RuntimeErrorAccessViolation,
	// Security Violation 
	SecurityViolation,
	// Internal error
	InternalError
}
