package djudge.dservice.interfaces;

import java.util.HashMap;


@SuppressWarnings("unchecked")
public interface DServiceXmlRpcClientInterface extends DServiceCommonClientInterface
{
	public HashMap[] fetchResults(String uid);
}
