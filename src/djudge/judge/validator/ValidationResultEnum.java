/* $Id$ */

package djudge.judge.validator;

/* Possible results of validation process */
public enum ValidationResultEnum 
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
