package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface TeamMonitorXmlRpcInterface
{
	@SuppressWarnings("unchecked")
	public HashMap getTeamMonitor(String username, String password);

}
