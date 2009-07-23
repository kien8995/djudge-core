package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.MonitorData;

public interface MonitorInterface
{
	public MonitorData getMonitor(String username, String password);
}
