package com.alt.djudge.judge.dcompiler;

import com.alt.djudge.judge.dexecutor.ExecutionResult;


public class CompilerResult
{
	
	CompiledProgram program;
	
	CompilationResult result = CompilationResult.Undefined;
	
	ExecutionResult compilerExecution;
	
	public boolean compilationSuccessfull()
	{
		return result == CompilationResult.OK;
	}

	public String[] getCompilerOutput()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCompilerOutput(String[] native_output)
	{
		// TODO Auto-generated method stub
		
	}
	
}
