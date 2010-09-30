/* $Id$ */

package djudge.common;

import utils.FileWorks;

public class JudgeDirs
{
	final static String rootDirectory;
	
	static
	{
		rootDirectory = FileWorks.getAbsolutePath(".") + "/";
	}	
	
	public static String getWorkDir()
	{
		return rootDirectory + "work/";
	}
	
	public static String getTempDir()
	{
		return rootDirectory + "temp/";
	}

	public static String getProblemsDir()
	{
		return rootDirectory + "problems/";
	}

	public static String getToolsDir()
	{
		return rootDirectory + "tools/";
	}
}
