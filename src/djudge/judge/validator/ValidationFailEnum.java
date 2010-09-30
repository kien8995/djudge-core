/* $Id$ */

package djudge.judge.validator;

/*
 * Enumeration that represents possible causes
 * why the validation process failed to finish 
 */
public enum ValidationFailEnum 
{
	/* Unknown reason */
	Undefined,
	
	/* Validation succeeded */
	OK,
	
	/* Input file not found */
	NoInputFileError,
	
	/* Output (judge-generated) file not found */
	NoOutputFileError,
	
	/* Answer (contestant-generated) file not found */
	NoAnswerFileError,
	
	/* Unknown validator type */
	ValidatorNotFounded,
	
	/* Validator failed to terminate normally */
	ValidatorFail,
	
	/* Validator's executable file not found */
	ValidatorNoExeFile,
	
	/* External validator failed to run in limits */
	ValidatorTLE,
	ValidatorMLE,
	ValidatorOLE,
	
	/* Validator finished crashed during execution */
	ValidatorCrash
}
