package djudge.acmcontester.interfaces;

public interface TeamUsersInterface
{
	public String registerUser(String username, String password);
	
	public boolean enterContest(String username, String password);
	
}
