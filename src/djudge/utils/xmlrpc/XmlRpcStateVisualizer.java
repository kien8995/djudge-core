/* $Id$ */

package djudge.utils.xmlrpc;

public interface XmlRpcStateVisualizer
{
	public void beforeMethodCall();
	
	public void onSuccess();
	
	public void onFailure();
}
