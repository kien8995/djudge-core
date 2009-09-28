package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface TeamSubmissionsInterface
{
	public HashMap<String, String>[] getOwnSubmissions(String username, String password);

	public HashMap<String, String>[] getAllSubmissions(String username, String password);
	
	public boolean submitSolution(String username, String password, String problemID, String languageID, String sourceCode);
	
}
