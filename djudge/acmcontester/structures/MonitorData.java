package djudge.acmcontester.structures;

import java.util.HashMap;

import djudge.common.HashMapSerializable;

public class MonitorData extends HashMapSerializable
{
	public MonitorRow[] rows;
	
	public MonitorData()
	{
		rows = new MonitorRow[0];
	}

	@SuppressWarnings("unchecked")
	public MonitorData(HashMap map)
	{
		fromHashMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromHashMap(HashMap map)
	{
		Object[] data = (Object[]) map.get("rows");
		int n = data.length;
		rows = new MonitorRow[n];
		for (int i = 0; i < n; i++)
		{
			rows[i] = new MonitorRow((HashMap) data[i]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap toHashMap()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap[] hm = new HashMap[rows.length];
		for (int i = 0; i < rows.length; i++)
		{
			hm[i] = rows[i].toHashMap();
		}
		map.put("rows", hm);
		return map;
	}
}