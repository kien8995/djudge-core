package djudge.acmcontester.interfaces;

public interface TeamUsersInterface
{
	public String registerTeam(String username, String password);
	
	public boolean enterContestTeam(String username, String password);
	
}
