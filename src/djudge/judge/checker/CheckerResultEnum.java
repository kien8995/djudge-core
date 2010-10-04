/* $Id$ */

package djudge.judge.checker;

/* Possible results of check process */
public enum CheckerResultEnum 
{
	/* Result accepted */
	OK,
	
	/* Data format is wrong (often is mixed with WrongAnswer) */
	PresentationError,
	
	/* Wrong answer is given */
	WrongAnswer,
	
	/* Some internal error occurred during validation */
	InternalError,
	
	/* Validation not done */
	Undefined
}
