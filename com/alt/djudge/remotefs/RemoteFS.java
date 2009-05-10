package com.alt.djudge.remotefs;

import com.alt.utils.FileWorks;

// FIXME: this is just a stub
public class RemoteFS
{
	public static String readContent(String filename)
	{
		return FileWorks.readFile(filename);
	}
	
	public static boolean writeContent(String content, String filename)
	{
		FileWorks.saveToFile(content, filename);
		return true;
	}
}
