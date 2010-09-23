/* $Id$ */

package djudge.utils;

import java.util.HashMap;

import org.apache.log4j.Logger;

public abstract class CachedObjectsSet<K, V>
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(CachedObjectsSet.class);
	
	protected final long updateInterval;
	
	private HashMap<K, CachedObject<V> > objects = new HashMap<K, CachedObject<V> >();
	
	public CachedObjectsSet(long updateInterval)
	{
		this.updateInterval = updateInterval;
	}
	
	public abstract CachedObject<V> getObjectForKey(K key);
	
	public synchronized V getData(K key)
	{
		CachedObject<V> cachedObject = objects.get(key);
		if (null == cachedObject)
		{
			cachedObject = getObjectForKey(key);
			objects.put(key, cachedObject);
		}
		return cachedObject.getData();
	}
}
