package com.alt.djudge.judge.dcompiler;

import com.alt.utils.FileWorks;

public class DFile
{
	public String filename;
	public byte[] content;
	
	public DFile(String filename)
	{
		loadFile(filename);
	}
	
	public DFile(String filename, String addAs)
	{
		loadFile(filename, addAs);
	}
	
	public boolean loadFile(String filename, String addAs)
	{
		String name = FileWorks.getFileName(addAs);
		this.filename = name;
		content = FileWorks.readFileContent(filename);
		return true;
	}
	
	public boolean loadFile(String filename)
	{
		return loadFile(filename, filename);
	}
	
	public boolean saveFile(String rootDirectory)
	{
		FileWorks.writeFileContent(FileWorks.ConcatPaths(rootDirectory, filename), content);
		return true;
	}
}
