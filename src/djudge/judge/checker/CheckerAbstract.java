/* $Id$ */

package djudge.judge.checker;

/**
 * Base class for all validators
 * @author alt
 */
public abstract class CheckerAbstract 
{
	// Provides information about validation result
	protected CheckerResult res;
	
	// executable filename (for external validator)
	private String checkerExecutableFilename;
	
	public String getExeFile()
	{
		return getExeFilename();
	}
	
	/**
	 * Performs validation of result 
	 * @param input - Judge input file
	 * @param output - Program output file
	 * @param answer - Judge output file
	 * @return object of type ValidationResult 
	 */
	public abstract CheckerResult validateOutput(String input, String output, String answer);

	public void setExecutableFilename(String exeFilename)
	{
		this.checkerExecutableFilename = exeFilename;
	}

	public String getExeFilename()
	{
		return checkerExecutableFilename;
	}
}
