package djudge.common;

import java.util.HashMap;

import djudge.acmcontester.structures.RemoteRowStub;

public abstract class HashMapSerializable extends RemoteRowStub
{
	@SuppressWarnings("unchecked")
	public abstract HashMap toHashMap();
	
	public abstract void fromHashMap(HashMap map);

}
