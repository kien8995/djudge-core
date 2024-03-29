/* $Id$ */

package djudge.common;

import utils.FileTools;

/* 
 * Providers paths to different application directories 
 * Now they all are subdirectories of `./`, but this can changed in future
 */
public class JudgeDirs
{
	final static String rootDirectory;
	
	static
	{
		rootDirectory = FileTools.getAbsolutePath(".") + "/";
	}	
	
	public static String getRootDir()
	{
		return rootDirectory;
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

	public static String getProblemDir(String contestId, String problemId)
	{
		return getProblemsDir() + contestId + "/" + problemId + "/";
	}
	
	public static String getToolsDir()
	{
		return rootDirectory + "tools/";
	}
}
