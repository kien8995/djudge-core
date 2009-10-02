package djudge.acmcontester.server.interfaces;

import java.util.HashMap;

public interface AdminMonitorXmlRpcInterface
{
	@SuppressWarnings("unchecked")
	public HashMap getMonitor(String username, String password);

}
