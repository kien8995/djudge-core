package djudge.acmcontester.server.interfaces;

import java.util.HashMap;

public interface AdminLanguagesXmlRpcInterface extends AdminLanguagesCommonInterface
{
	@SuppressWarnings("unchecked")
	public HashMap[] getLanguages(String username, String password);
}
