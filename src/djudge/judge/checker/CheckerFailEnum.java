/* $Id$ */

package djudge.judge.checker;

/*
 * Enumeration that represents possible causes
 * why the check process failed to finish 
 */
public enum CheckerFailEnum 
{
	/* Unknown reason */
	Undefined,
	
	/* Check succeeded */
	OK,
	
	/* Input file not found */
	NoInputFileError,
	
	/* Output (judge-generated) file not found */
	NoOutputFileError,
	
	/* Answer (contestant-generated) file not found */
	NoAnswerFileError,
	
	/* Unknown checker type */
	CheckerNotFound,
	
	/* Checker failed to terminate normally */
	CheckerFail,
	
	/* Checker's executable file not found */
	CheckerNoExeFile,
	
	/* External checker failed to run in limits */
	CheckerTLE,
	CheckerMLE,
	CheckerOLE,
	
	/* Validator finished crashed during execution */
	CheckerCrash
}
