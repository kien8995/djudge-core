package djudge.utils.xmlrpc;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class XmlRpcServer extends Thread
{
	private final WebServer webServer;
	
	public XmlRpcServer(String serviceName, int port, Class<? extends Object> stubClass) 
	{
		webServer = new WebServer(port);
		try
		{
			XmlRpcStreamServer xmlRpcServer = webServer.getXmlRpcServer();

			PropertyHandlerMapping phm = new PropertyHandlerMapping();

			phm.addHandler(serviceName, stubClass);

			xmlRpcServer.setHandlerMapping(phm);

			XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
			serverConfig.setEnabledForExtensions(true);
			serverConfig.setContentLengthOptional(false);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			webServer.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

