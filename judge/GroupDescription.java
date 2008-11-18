package judge;

import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

public class GroupDescription
{
	RunnerFiles files;
	
	RunnerLimits limits;
	
	public GroupDescription(int i, int testsCount, GlobalProblemInfo clone,
							String inputFileMask, String outputFileMask)
	{
		
	}

	public RunnerFiles getFiles()
	{
		return files;
	}
	
	public RunnerLimits getLimits()
	{
		return limits;
	}
}
