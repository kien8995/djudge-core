package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface AdminProblemsXmlRpcInterface extends AdminProblemsCommonInterface
{
	@SuppressWarnings("unchecked")
	HashMap[] getProblems(String username, String password);
}
