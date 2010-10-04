/* $Id$ */

package djudge.judge.checker;

/**
 * Base class for all validators
 * @author alt
 */
public abstract class ValidatorAbstract 
{
	// Provides information about validation result
	ValidationResult res;
	
	// executable filename (for external validator)
	String exeFilename;
	
	public String getExeFile()
	{
		return exeFilename;
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
