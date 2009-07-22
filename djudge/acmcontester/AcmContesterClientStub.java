package djudge.acmcontester;

import java.util.HashMap;

import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;
import djudge.common.HashMapSerializer;

public class AcmContesterClientStub extends HashMapSerializer implements AcmContesterXmlRpcClientInterface
{
	@Override
	public HashMap<String, String>[] getProblems(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestCore.getProblems(userInfo));
	}

	@Override
	public boolean enterContest(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return ContestCore.enterContest(userInfo);
	}

	@Override
	public String registerUser(String username, String password)
	{
		return ContestCore.registerUser(username, password);
	}

	@Override
	public HashMap<String, String>[] getAllSubmissions(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestCore.getAllSubmissions(userInfo));
	}

	@Override
	public HashMap<String, String>[] getOwnSubmissions(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestCore.getOwnSubmissions(userInfo));
	}

	@Override
	public boolean submitSolution(String username, String password,
			String problemID, String languageID, String sourceCode)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return ContestCore.submitSolution(userInfo, problemID, languageID, sourceCode);
	}

	@Override
	public HashMap<String, String>[] getLanguages(String username, String password)
	{
		AuthentificationData userInfo = new AuthentificationData(username, password);
		return serializeToHashMap(ContestCore.getLanguages(userInfo));
	}
	
	@Override
	public String getVersion()
	{
		return ContestCore.getVersion();
	}

}
