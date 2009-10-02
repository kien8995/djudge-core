package djudge.acmcontester.server.interfaces;

import java.util.HashMap;

interface AdminUsersCommonInterface
{
	public boolean addUser(String username, String password, String newUserName,
			String newPassword, String name, String role);
	
	public boolean editUser(String username, String password, String id, String newUserName,
			String newPassword, String name, String role);
	
	public boolean deleteUser(String username, String password, String id);
	
	public boolean deleteAllUsers(String username, String password);
	
	public boolean changePassword(String username, String oldPassword, String newPassword);
}

public interface AdminUsersXmlRpcInterface extends AdminUsersCommonInterface
{
	public HashMap<String, String>[] getUsers(String username, String password);
}
