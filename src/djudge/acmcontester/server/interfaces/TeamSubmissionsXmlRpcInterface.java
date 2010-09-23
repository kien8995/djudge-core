package djudge.acmcontester.server.interfaces;

import java.util.HashMap;

public interface TeamSubmissionsXmlRpcInterface
{
	public HashMap<String, String>[] getTeamSubmissions(String username, String password);
	
	public boolean submitSolution(String username, String password, String problemID, String languageID, String sourceCode);
	
	public boolean testSolution(String username, String password, String problemID, String languageID, String sourceCode);
}
