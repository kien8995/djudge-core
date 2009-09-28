package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface TeamProblemsInterface
{
	public HashMap<String, String>[] getProblems(String username, String password);
	
}
