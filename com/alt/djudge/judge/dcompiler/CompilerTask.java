package com.alt.djudge.judge.dcompiler;

public class CompilerTask
{
	/*
	 * Files to compile
	 */
	DFilePack files;
	
	/*
	 * Language ID
	 */
	String languageId;
	
	String mainFile;
	
	public CompilerTask()
	{
		// TODO Auto-generated constructor stub
	}
	
	public CompilerTask(String filename, String languageId)
	{
		files = new DFilePack(filename);
		this.languageId = languageId;
	}
		
}
