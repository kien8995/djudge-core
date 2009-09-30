package djudge.acmcontester.interfaces;

public interface AdminServerCommonInterface
{
	public boolean setContestTimePast(String username, String password, long timePast);  // milliseconds
	
	public boolean setContestTimeLeft(String username, String password, long timeLeft);  // milliseconds
	
	public boolean setContestRunning(String username, String password, boolean isRunning);
	
	public boolean setContestFreezeTime(String username, String password, long tillTimeLeft); // milliseconds
	
	public boolean deleteAllData(String username, String password);
}
