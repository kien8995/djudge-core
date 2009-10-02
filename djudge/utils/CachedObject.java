package djudge.utils;

import java.util.Date;

public abstract class CachedObject
{
	protected Object cachedData = null;
	
	protected long lastUpdateTime = 0;
	
	private final long updateInterval;
	
	public CachedObject(long updateInterval)
	{
		this.updateInterval = updateInterval;
	}
	
	protected abstract boolean updateCache();
	
	public Object getData()
	{
		long timeNow = new Date().getTime();
		if (timeNow - lastUpdateTime > updateInterval && updateCache())
		{
			lastUpdateTime = timeNow;
		}
		return cachedData;
	}
	
	public long getLastUpdateTime()
	{
		return lastUpdateTime;
	}
}
