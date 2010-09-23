package djudge.acmcontester.server.interfaces;

public interface AdminServerCommonInterface
{
	public boolean setContestTimePast(String username, String password, long timePast);  // milliseconds
	
	public boolean setContestTimeLeft(String username, String password, long timeLeft);  // milliseconds
	
	public boolean incrementContestTimeLeft(String username, String password, long timeLeftAdd);  // milliseconds
	
	public boolean setContestRunning(String username, String password, boolean isRunning);
	
	public boolean setContestFreezeTime(String username, String password, long tillTimeLeft); // milliseconds
	
	public long getContestFreezeTime(String username, String password); // milliseconds
	
	public boolean deleteAllData(String username, String password);
}
