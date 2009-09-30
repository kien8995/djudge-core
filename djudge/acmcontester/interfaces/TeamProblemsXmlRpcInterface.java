package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface TeamProblemsXmlRpcInterface
{
	public HashMap<String, String>[] getTeamProblems(String username, String password);
}
