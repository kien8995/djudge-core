package djudge.acmcontester;

import java.util.HashMap;

import djudge.acmcontester.interfaces.ServerXmlRpcInterface;

public class ServerXmlRpcConnector extends TeamXmlRpcConnector implements ServerXmlRpcInterface
{
	@Override
	public boolean addLanguage(String username, String password, String sid,
			String shortName, String fullName, String compilationComand,
			String djudgeID)
	{
		return (Boolean) callRemoteMethod(serviceName + ".addLanguage", username, password, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean editLanguage(String username, String password, String id,
			String sid, String shortName, String fullName,
			String compilationComand, String djudgeID)
	{
		return (Boolean) callRemoteMethod(serviceName + ".editLanguage", username, password, id, sid, shortName, fullName, compilationComand, djudgeID);
	}

	@Override
	public boolean deleteLanguage(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod(serviceName + ".deleteLanguage", username, password, id);	
	}

	@Override
	public boolean addUser(String username, String password,
			String newUserName, String newPassword, String name, String role)
	{
		return (Boolean) callRemoteMethod(serviceName + ".addUser", username, password, newUserName, newPassword, name, role);
	}

	@Override
	public boolean deleteUser(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod(serviceName + ".deleteUser", username, password, id);
	}

	@Override
	public HashMap<String, String>[] getUsers(String username, String password)
	{
		Object[] params = {username, password};
		Object remoteResult = callRemoteMethod(serviceName + ".getUsers", params);
		return deserializeToHashMapArray(remoteResult);
	}
}
