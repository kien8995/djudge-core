package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface TeamSubmissionsXmlRpcInterface
{
	public HashMap<String, String>[] getTeamSubmissions(String username, String password);
	
	public boolean submitSolution(String username, String password, String problemID, String languageID, String sourceCode);
}
