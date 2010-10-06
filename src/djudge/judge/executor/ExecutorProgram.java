/* $Id$ */

package djudge.judge.executor;

import djudge.judge.dcompiler.DistributedFileset;

public class ExecutorProgram
{
	public DistributedFileset files = new DistributedFileset();
	
	public String command = "";
	
	public ExecutorProgram()
	{
		// TODO Auto-generated constructor stub
	}
	
	public ExecutorProgram(String cmd)
	{
		command = cmd;
	}	
	
	public String getCommand()
	{
		return command;
	}
	
	public String getCommand(String directory)
	{
		return command;
	}	
}
