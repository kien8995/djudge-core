package com.alt.djudge.judge.dcompiler;

public class CompiledProgram
{
	public DFilePack files;
	
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
