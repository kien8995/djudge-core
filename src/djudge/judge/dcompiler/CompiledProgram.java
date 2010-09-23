/* $Id$ */

package djudge.judge.dcompiler;

public class CompiledProgram
{
	public DistributedFileset files;
	
	public String runCommand;
	
	public String getRunCommand()
	{
		return runCommand;
	}
	
	public String getRunCommand(String rootDirectory)
	{
		return "";
	}
	
}
