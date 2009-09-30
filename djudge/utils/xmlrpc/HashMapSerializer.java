package djudge.utils.xmlrpc;

import java.util.HashMap;
import java.util.Vector;


public class HashMapSerializer
{
	@SuppressWarnings("unchecked")
	public static HashMap<String, String>[] objectToHashMapArray(Object obj)
	{
		try
		{
    		Object[] array = (Object[]) obj;
    		HashMap[] map = new HashMap[array.length];
    		for (int i = 0; i < map.length; i++)
    			map[i] = (HashMap) array[i];
    		return map;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String>[] serializeToHashMapArray(HashMapSerializable[] data)
	{
		if (data == null)
			return new HashMap[0];
		HashMap<String, String>[] res = new HashMap[data.length];
		for (int k = 0; k < data.length; k++)
		{
			res[k] = data[k].toHashMap();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public static <T extends HashMapSerializable> Vector<T> deserializeFromHashMapArray(HashMap[] data, Class<T> type)
	{
		Vector<T> res = new Vector<T>();
		for (int i = 0; i < data.length; i++)
		{
			try
			{
				T item = type.newInstance();
				item.fromHashMap(data[i]);
				res.add(item);
			} catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
}
