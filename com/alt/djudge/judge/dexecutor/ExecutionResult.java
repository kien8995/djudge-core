package com.alt.djudge.judge.dexecutor;

import com.alt.djudge.judge.dcompiler.DFilePack;

public class ExecutionResult
{
	String stdOutputContent = "";
	
	String stdErrorContent = "";
	
	long timeConsumed;
	
	long memoryConsumed;
	
	long outputGenerated;
	
	int exitCode;
	
	ExecutionResultEnum result;
	
	DFilePack files;
	
	String runnerOutput;
	
	public DFilePack getFiles()
	{
		return files;
	}
	
	public int getExitCode()
	{
		return exitCode;
	}
	
	public ExecutionResultEnum getResult()
	{
		return result;
	}
}
