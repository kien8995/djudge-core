/* $Id$ */

package djudge.judge.validator;

public enum ValidationFailEnum 
{
	Undefined,
	OK,
	NoInputFileError,
	NoOutputFileError,
	NoAnswerFileError,
	ValidatorNotFounded,
	ValidatorFail,
	ValidatorNoExeFile,
	ValidatorTLE,
	ValidatorMLE,
	ValidatorOLE,
	ValidatorCrash
}
