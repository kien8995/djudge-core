package djudge.acmcontester.admin;

import java.util.HashMap;

import djudge.acmcontester.TeamXmlRpcConnector;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.utils.xmlrpc.XmlRpcStateVisualizer;

public class AdminXmlRpcConnector extends TeamXmlRpcConnector implements ServerXmlRpcInterface
{
	public AdminXmlRpcConnector()
	{
		// TODO Auto-generated constructor stub
	}
	
	public AdminXmlRpcConnector(XmlRpcStateVisualizer vizi)
	{
		super(vizi);
	}
	
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
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public boolean editUser(String username, String password, String id,
			String newUserName, String newPassword, String name, String role)
	{
		return (Boolean) callRemoteMethod(serviceName + ".editUser", username, password, id, newUserName, newPassword, name, role);
	}

	@Override
	public boolean addProblem(String username, String password, String sid,
			String name, String djudgeProblem, String djudgeContest)
	{
		return (Boolean) callRemoteMethod(serviceName + ".addProblem", username, password, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteProblem(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod(serviceName + ".deleteProblem", username, password, id);
	}

	@Override
	public boolean editProblem(String username, String password, String id,
			String sid, String name, String djudgeProblem, String djudgeContest)
	{
		return (Boolean) callRemoteMethod(serviceName + ".editProblem", username, password, id, sid, name, djudgeProblem, djudgeContest);
	}

	@Override
	public boolean deleteSubmission(String username, String password, String id)
	{
		return (Boolean) callRemoteMethod(serviceName + ".deleteSubmission", username, password, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean editSubmission(String username, String password, String id,
			HashMap data)
	{
		return (Boolean) callRemoteMethod(serviceName + ".editSubmission", username, password, id, data);
	}

	@Override
	public boolean rejudgeSubmissions(String username, String password,
			String key, String value)
	{
		return (Boolean) callRemoteMethod(serviceName + ".rejudgeSubmissions", username, password, key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap[] getSubmissions(String username, String password)
	{
		Object remoteResult = callRemoteMethod(serviceName + ".getSubmissions", username, password);
		return objectToHashMapArray(remoteResult);
	}
}
