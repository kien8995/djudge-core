/* $Id$ */

package djudge.judge.validator;

/**
 * Base class for all validators
 * @author alt
 */
public abstract class ValidatorAbstract 
{
	// Provides information about validation result
	ValidationResult res;
	
	protected String exeFile;
	
	public String getExeFile()
	{
		return exeFile;
	}
	
	/**
	 * Performs validation of result 
	 * @param input Judge input file
	 * @param output Judge output file
	 * @param answer Program output file
	 * @return object of type ValidationResult 
	 */
	public abstract ValidationResult validateOutput(String input, String output, String answer);
	
}
