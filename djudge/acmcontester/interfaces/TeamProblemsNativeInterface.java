package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.ProblemData;

public interface TeamProblemsNativeInterface
{
	public ProblemData[] getTeamProblems(String username, String password);
	
}
