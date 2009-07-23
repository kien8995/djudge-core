package djudge.acmcontester.interfaces;


public interface AcmContesterXmlRpcClientInterface extends ProblemsInterface, UsersInterface, SubmissionsInterface, LanguagesInterface
{
	public String getVersion();
	
	public String echo(String what);
	
	public long getContestTimeElapsed(String username, String password);
	
	public long getContestTimeLeft(String username, String password);
	
	public String getContestStatus(String username, String password);
}
