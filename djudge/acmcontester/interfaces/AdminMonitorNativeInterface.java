package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.MonitorData;

public interface AdminMonitorNativeInterface
{
	public MonitorData getMonitor(String username, String password);
}
