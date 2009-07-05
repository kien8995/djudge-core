package com.alt.djudge.judge.dcompiler;

import java.io.File;
import java.util.HashMap;

import com.alt.utils.FileWorks;

class DFile
{
	String filename;
	byte[] content;
	
	public DFile(String filename)
	{
		loadFile(filename);
	}
	
	public boolean loadFile(String filename)
	{
		String name = FileWorks.getFileName(filename);
		this.filename = name;
		content = FileWorks.readFileContent(filename);
		return true;
	}
	
	public boolean saveFile(String rootDirectory)
	{
		FileWorks.writeFileContent(FileWorks.ConcatPaths(rootDirectory, filename), content);
		return true;
	}
}

public class DFilePack
{
	public HashMap<String, DFile> map = new HashMap<String, DFile>();
	
	public DFilePack()
	{
		
	}
	
	public boolean addFile(String filename)
	{
		DFile f = new DFile(filename);
		map.put(f.filename, f);
		return true;
	}
	
	public DFilePack(String filename)
	{
		addFile(filename);
	}
	
	public DFilePack(String[] files)
	{
		for (String file : files)
		{
			addFile(file);
		}
	}
	
	public void readDirectory(String directory)
	{
		File f = new File(directory);
		for (File file : f.listFiles())
		{
			if (file.isFile())
			{
				addFile(file.getPath());
			}
		}
	}
	
	public void unpack(String directory)
	{
		String[] files = map.keySet().toArray(new String[0]);
		for (String file : files)
		{
			map.get(file).saveFile(directory);
		}
	}
	
	public String getFile()
	{
		return map.keySet().toArray(new String[0])[0];
	}
}
