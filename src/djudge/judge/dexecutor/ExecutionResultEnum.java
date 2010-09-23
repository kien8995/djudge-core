/* $Id: RunResult.java, v 0.1 2008/07/15 05:13:08 alt Exp $ */

/* Copyright (C) 2008 Olexiy Palinkash <olexiy.palinkash@gmail.com> */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package djudge.judge.dexecutor;

/**
 * Enumeration with possible run results
 * @author alt
 */
public enum ExecutionResultEnum 
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
