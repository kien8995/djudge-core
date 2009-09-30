package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.MonitorData;

public interface TeamMonitorNativeInterface
{
	public MonitorData getTeamMonitor(String username, String password);
}
