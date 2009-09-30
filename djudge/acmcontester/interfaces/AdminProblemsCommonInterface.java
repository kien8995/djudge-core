package djudge.acmcontester.interfaces;

public interface AdminProblemsCommonInterface
{
	public boolean addProblem(String username, String password, String sid,
			String name, String djudgeProblem, String djudgeContest);
	
	public boolean editProblem(String username, String password, String id, String sid,
			String name, String djudgeProblem, String djudgeContest);
	
	public boolean deleteProblem(String username, String password, String id);
}
