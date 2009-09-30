package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.SubmissionData;

public interface TeamSubmissionsNativeInterface
{
	public SubmissionData[] getTeamSubmissions(String username, String password);
	
	public boolean submitSolution(String username, String password, String problemID, String languageID, String sourceCode);
}
