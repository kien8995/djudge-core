package djudge.utils;


import java.util.Date;
import org.apache.log4j.Logger;

public abstract class CachedObject
{
	private static final Logger log = Logger.getLogger(CachedObject.class);
	
	private Object cachedData = null;
	
	private long lastUpdateTime = 0;
	
	private final long updateInterval;
	
	private int updateFailuresCounter = 0;
	
	public CachedObject(long updateInterval)
	{
		this.updateInterval = updateInterval;
	}
	
	protected abstract Object updateData() throws Exception;
	
	private boolean updateCache()
	{
		log.info("Updating cache");
		try
		{
			cachedData = updateData();
			log.info("Cache updated");
			return true;
		}
		catch (Exception e)
		{
			log.info("Cache update failed. Already " + updateFailuresCounter + " times");
		}
		return false;
	}	
	
	synchronized public Object getData()
	{
		long timeNow = new Date().getTime();
		if (timeNow - lastUpdateTime > updateInterval * (1 + updateFailuresCounter))
		{
			if (updateCache())
    		{
    			lastUpdateTime = timeNow;
    			updateFailuresCounter = 0;
    		}
			else
			{
				updateFailuresCounter++;
			}
		}
		return cachedData;
	}
	
	public long getLastUpdateTime()
	{
		return lastUpdateTime;
	}
}
