package djudge.acmcontester.interfaces;

public interface UsersInterface
{
	public String registerUser(String username, String password);
	
	public boolean enterContest(String username, String password);
	
}
