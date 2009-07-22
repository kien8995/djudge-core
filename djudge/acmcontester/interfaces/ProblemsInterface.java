package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface ProblemsInterface
{
	public HashMap<String, String>[] getProblems(String username, String password);
	
}
