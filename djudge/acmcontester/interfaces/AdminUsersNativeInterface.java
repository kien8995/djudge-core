package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.UserData;

public interface AdminUsersNativeInterface extends AdminUsersCommonInterface
{
	public UserData[] getUsers(String username, String password);
}
