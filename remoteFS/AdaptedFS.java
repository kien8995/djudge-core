package remoteFS;

public interface AdaptedFS
{
	public boolean getProblemDescription(String localFilename, String contestID, String problemID);
	public boolean getCheckerExecutable(String localFilename, String contestID, String problemID);
	public boolean getTestInput(String localFilename, String contestID, String problemID);
	public boolean getTestOutput(String localFilename, String contestID, String problemID);
}
