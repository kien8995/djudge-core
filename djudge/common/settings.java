package djudge.common;

public class settings
{
	private final static String rootDir = "./";
	
	public static String getWorkDir()
	{
		return rootDir + "work/";
	}
	
	public static String getTempDir()
	{
		return rootDir + "temp/";
	}

	public static String getProblemsDir()
	{
		return rootDir + "problems/";
	}
}
