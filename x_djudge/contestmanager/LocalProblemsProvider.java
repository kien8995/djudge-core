package x_djudge.contestmanager;

public interface LocalProblemsProvider
{
	public LocalProblemDescription[] getProblems();
	
	public boolean checkProblem(String problemSid);
	
	public boolean saveData();
	
	public LocalProblemDescription getProblem(String problemSid); 
}
