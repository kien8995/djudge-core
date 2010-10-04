/* $Id$ */

package djudge.judge.checker;

/**
 * Base class for all validators
 * @author alt
 */
public abstract class ValidatorAbstract 
{
	// Provides information about validation result
	protected ValidationResult res;
	
	// executable filename (for external validator)
	private String exeFilename;
	
	public String getExeFile()
	{
		return getExeFilename();
	}
	
	/**
	 * Performs validation of result 
	 * @param input Judge input file
	 * @param output Judge output file
	 * @param answer Program output file
	 * @return object of type ValidationResult 
	 */
	public abstract ValidationResult validateOutput(String input, String output, String answer);

	public void setExeFilename(String exeFilename)
	{
		this.exeFilename = exeFilename;
	}

	public String getExeFilename()
	{
		return exeFilename;
	}
}
