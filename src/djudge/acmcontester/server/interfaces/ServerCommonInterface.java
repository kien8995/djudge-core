/* $Id$ */

package djudge.acmcontester.server.interfaces;

public interface ServerCommonInterface
{
	public String getVersion();
	
	public String echo(String what);
	
	public long getContestTimeElapsed(String username, String password);
	
	public long getContestTimeLeft(String username, String password);
	
	public String getContestStatus(String username, String password);
}
