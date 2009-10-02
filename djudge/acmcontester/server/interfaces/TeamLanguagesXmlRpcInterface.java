package djudge.acmcontester.server.interfaces;

import java.util.HashMap;

public interface TeamLanguagesXmlRpcInterface
{
	public HashMap<String, String>[] getTeamLanguages(String username, String password);
	
}
