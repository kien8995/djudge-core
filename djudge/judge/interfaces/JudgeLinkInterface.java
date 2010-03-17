package djudge.judge.interfaces;

public interface JudgeLinkInterface
{
	public void initLink(String[] params, JudgeLinkCallbackInterface callback, int judgeLinkId);
	
	public void startLink();
	
	public void stopLink();
	
	public boolean getRunning();
}
