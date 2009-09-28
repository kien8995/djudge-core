package djudge.dservice;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class XmlRpcServer extends Thread
{
	private int port;
	
	private WebServer webServer;
	
	public XmlRpcServer(int port)
	{
		this.port = port;
	}
	
	public XmlRpcServer()
	{
		port = 8001;
	}
	
	@Override
	public void run()
	{	
		try
		{
			webServer = new WebServer(port);

			XmlRpcStreamServer xmlRpcServer = webServer.getXmlRpcServer();

			PropertyHandlerMapping phm = new PropertyHandlerMapping();

			phm.addHandler("DService", DServiceStub.class);

			xmlRpcServer.setHandlerMapping(phm);

			XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
			serverConfig.setEnabledForExtensions(true);
			serverConfig.setContentLengthOptional(false);

			webServer.start();
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}
