package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.ProblemData;

public interface AdminProblemsNativeInterface extends AdminProblemsCommonInterface
{
	ProblemData[] getProblems(String username, String password);
}
