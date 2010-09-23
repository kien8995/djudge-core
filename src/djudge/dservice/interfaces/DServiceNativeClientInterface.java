package djudge.dservice.interfaces;

import djudge.dservice.DServiceTaskResult;

public interface DServiceNativeClientInterface extends DServiceCommonClientInterface
{
	public DServiceTaskResult[] fetchResults(String uid);
}
