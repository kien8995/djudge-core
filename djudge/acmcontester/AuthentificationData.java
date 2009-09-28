package djudge.acmcontester;

import djudge.acmcontester.interfaces.AuthentificationDataProvider;

public class AuthentificationData implements AuthentificationDataProvider
{
	public AuthentificationData(String username2, String password2)
	{
		username = username2;
		password = password2;
	}
	
	public AuthentificationData()
	{
		// TODO Auto-generated constructor stub
	}
	
	public String username;
	public String password;
	
	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getUsername()
	{
		return username;
	}
}
