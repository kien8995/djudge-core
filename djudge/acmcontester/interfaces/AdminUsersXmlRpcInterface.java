package djudge.acmcontester.interfaces;

import java.util.HashMap;

import djudge.acmcontester.structures.UserData;

interface AdminUsersCommonInterface
{
	public boolean addUser(String username, String password, String newUserName,
			String newPassword, String name, String role);
	
	public boolean editUser(String username, String password, String id, String newUserName,
			String newPassword, String name, String role);
	
	public boolean deleteUser(String username, String password, String id);
}

public interface AdminUsersXmlRpcInterface extends AdminUsersCommonInterface
{
	public HashMap<String, String>[] getUsers(String username, String password);
}
