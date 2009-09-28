package djudge.acmcontester.interfaces;

import java.util.HashMap;



public interface TeamXmlRpcInterface extends TeamProblemsInterface, TeamUsersInterface, TeamSubmissionsInterface, TeamLanguagesInterface
{
	public String getVersion();
	
	public String echo(String what);
	
	public long getContestTimeElapsed(String username, String password);
	
	public long getContestTimeLeft(String username, String password);
	
	public String getContestStatus(String username, String password);
	
	@SuppressWarnings("unchecked")
	public HashMap getMonitor(String username, String password);
}
