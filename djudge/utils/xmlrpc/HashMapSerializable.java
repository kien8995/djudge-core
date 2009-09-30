package djudge.utils.xmlrpc;

import java.util.HashMap;


public abstract class HashMapSerializable extends RemoteRowStub
{
	@SuppressWarnings("unchecked")
	public abstract HashMap toHashMap();
	
	@SuppressWarnings("unchecked")
	public abstract void fromHashMap(HashMap map);

}
