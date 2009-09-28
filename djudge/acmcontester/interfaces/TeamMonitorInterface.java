package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.MonitorData;

public interface TeamMonitorInterface
{
	public MonitorData getMonitor(String username, String password);
}
