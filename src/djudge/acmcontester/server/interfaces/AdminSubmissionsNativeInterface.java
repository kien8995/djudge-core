package djudge.acmcontester.server.interfaces;

import djudge.acmcontester.structures.SubmissionData;

public interface AdminSubmissionsNativeInterface extends AdminSubmissionsCommonInterface
{
	public SubmissionData[] getSubmissions(String username, String password);
	
	public boolean editSubmission(String username, String password, String id, SubmissionData data);
}
