package djudge.acmcontester.server;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class ContesterXmlRpcServer extends Thread
{
	private int port;
	
	private String serviceName;
	
	private WebServer webServer;
	
	public ContesterXmlRpcServer()
	{
		port = Integer.parseInt(ServerSettings.getProperty("rpc-port"));
		serviceName = ServerSettings.getProperty("rpc-name");
	}
	
	@Override
	public void run()
	{
		try
		{
			webServer = new WebServer(port);

			XmlRpcStreamServer xmlRpcServer = webServer.getXmlRpcServer();

			PropertyHandlerMapping phm = new PropertyHandlerMapping();

			phm.addHandler(serviceName, ServerXmlRpcInterfaceStub.class);

			xmlRpcServer.setHandlerMapping(phm);

			XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
			serverConfig.setEnabledForExtensions(true);
			serverConfig.setContentLengthOptional(false);

			webServer.start();
			System.out.println(serviceName + " XML-RPC interface started on port " + port);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}
