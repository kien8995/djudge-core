package djudge.utils.xmlrpc;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;


public class XmlRpcConnector extends HashMapSerializer
{
	private static final Logger log = Logger.getLogger(XmlRpcConnector.class);
	
	protected final String serviceName;
	
	protected final String serviceURL;
	
	private XmlRpcStateVisualizer vizi;
	
	protected XmlRpcClient client;
	
	public XmlRpcConnector(String serviceName, String serviceUrl)
	{
		this(serviceName, serviceUrl, null);
	}
	
	public XmlRpcConnector(String serviceName, String serviceUrl, XmlRpcStateVisualizer vizi)
	{
		this.vizi = vizi;
		this.serviceURL = serviceUrl;
		this.serviceName = serviceName;
		try
		{
			client = RPCClientFactory.getRPCClient(serviceURL);
			log.info("XmlRpcConnector connected to " + serviceName + " @ " + serviceURL);
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
			log.info("Calling " + methodName + " with params " + params);
			result = client.execute(methodName, params);
			log.info("Result: " + result.toString());
			if (vizi != null)
				vizi.onSuccess();
		}
		catch (Exception ex)
		{
			if (vizi != null)
				vizi.onFailure();
			log.warn("Call failed", ex);
		}
		return result;
	}
	
	public void setVizi(XmlRpcStateVisualizer vizi)
	{
		this.vizi = vizi;
	}
}
