/* $Id$ */

package djudge.acmcontester.server.interfaces;

import java.util.HashMap;

public interface TeamProblemsXmlRpcInterface
{
	public HashMap<String, String>[] getTeamProblems(String username, String password);
}
