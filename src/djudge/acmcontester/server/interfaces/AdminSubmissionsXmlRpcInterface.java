package djudge.acmcontester.server.interfaces;

import java.util.HashMap;

interface AdminSubmissionsCommonInterface
{
	public boolean rejudgeSubmissions(String username, String password, String key, String value);
	
	public boolean deleteSubmission(String username, String password, String id);
	
	public boolean deleteAllSubmissions(String username, String password);
	
	public boolean activateSubmission(String username, String password, String id, int fActive); 
}

@SuppressWarnings("unchecked")
public interface AdminSubmissionsXmlRpcInterface extends AdminSubmissionsCommonInterface
{
	
	public HashMap[] getSubmissions(String username, String password);
	
	public boolean editSubmission(String username, String password, String id, HashMap data);
}
