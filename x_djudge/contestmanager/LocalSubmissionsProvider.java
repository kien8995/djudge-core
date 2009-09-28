package x_djudge.contestmanager;

public interface LocalSubmissionsProvider
{
	public String addSubmission(String userSid, String problemSid, String languageSid, String source, long contestTime);
	
	public boolean setSubmissionResult(String submissionId, String judgement, String xml);
	
	public LocalSubmissionDescription[] getUserSubmissions(String userSid);
}
