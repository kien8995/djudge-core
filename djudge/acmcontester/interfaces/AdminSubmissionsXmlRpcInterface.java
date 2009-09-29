package djudge.acmcontester.interfaces;

import java.util.HashMap;

interface AdminSubmissionsCommonInterface
{
	public boolean deleteSubmission(String username, String password, String id);
	
	public boolean rejudgeSubmissions(String username, String password, String key, String value);
}

public interface AdminSubmissionsXmlRpcInterface extends AdminSubmissionsCommonInterface
{
	@SuppressWarnings("unchecked")
	public HashMap[] getSubmissions(String username, String password);
	
	@SuppressWarnings("unchecked")
	public boolean editSubmission(String username, String password, String id, HashMap data);
}
