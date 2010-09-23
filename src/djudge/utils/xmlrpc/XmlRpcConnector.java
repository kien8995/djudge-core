/* $Id$ */

package djudge.utils.xmlrpc;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;


public class XmlRpcConnector extends HashMapSerializer
{
	private static final Logger log = Logger.getLogger(XmlRpcConnector.class);
	
	private final int defaultConnectionTimeout = 2000;
	
	private final int defaultReplyTimeout = 2000;
	
	private final String serviceName;
	
	private final String serviceURL;
	
	private XmlRpcStateVisualizer vizi;
	
	private XmlRpcClient client;
	
	public XmlRpcConnector(String serviceName, String serviceUrl)
	{
		this.serviceURL = serviceUrl;
		this.serviceName = serviceName;
		initConnector(serviceName, serviceUrl, defaultConnectionTimeout, defaultReplyTimeout);
	}
	
	public XmlRpcConnector(String serviceName, String serviceUrl, XmlRpcStateVisualizer vizi)
	{
		this.serviceURL = serviceUrl;
		this.serviceName = serviceName;
		initConnector(serviceName, serviceUrl, defaultConnectionTimeout, defaultReplyTimeout);
		setVizi(vizi);
	}
	
	public XmlRpcConnector(String serviceName, String serviceUrl, int connectionTimeout, int replyTimeout)
	{
		this.serviceURL = serviceUrl;
		this.serviceName = serviceName;
		initConnector(serviceName, serviceUrl, connectionTimeout, replyTimeout);
	}	
	
	private void initConnector(String serviceName, String serviceUrl, int connectionTimeout, int replyTimeout)
	{
		try
		{
			client = RPCClientFactory.getRPCClient(serviceURL, connectionTimeout, replyTimeout);
			log.info("XmlRpcConnector connected to " + serviceName + " @ "
					+ serviceURL + "; timeouts: " + connectionTimeout + ", "
					+ replyTimeout);
		}
		catch (Exception ex)
		{
			log.fatal("Connection to " + serviceName + " @ " + serviceURL + " failed", ex);
		}
	}
	
	protected Object callRemoteMethod(String methodName, Object... params)
	{
		Object result = null;
		try
		{
			if (vizi != null)
				vizi.beforeMethodCall();
			log.debug("Calling " + serviceName + "." + methodName + " with params " + params);
			result = client.execute(serviceName + "." + methodName, params);
			log.debug("Result: " + (result != null ? result.toString() : "<null>"));
			if (vizi != null)
				vizi.onSuccess();
		}
		catch (Exception ex)
		{
			if (vizi != null)
				vizi.onFailure();
			log.debug("Call failed", ex);
		}
		return result;
	}
	
	public void setVizi(XmlRpcStateVisualizer vizi)
	{
		this.vizi = vizi;
	}
}
