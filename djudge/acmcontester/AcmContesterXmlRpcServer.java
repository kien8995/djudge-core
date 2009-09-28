package djudge.acmcontester;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import djudge.acmcontester.server.ServerInterfaceStub;

public class AcmContesterXmlRpcServer extends Thread
{
	private int port;
	
	private WebServer webServer;
	
	public AcmContesterXmlRpcServer(int port)
	{
		this.port = port;
	}
	
	public AcmContesterXmlRpcServer()
	{
		port = 8202;
	}
	
	@Override
	public void run()
	{	
		try
		{
			webServer = new WebServer(port);

			XmlRpcStreamServer xmlRpcServer = webServer.getXmlRpcServer();

			PropertyHandlerMapping phm = new PropertyHandlerMapping();

			phm.addHandler("AcmContester", ServerInterfaceStub.class);

			xmlRpcServer.setHandlerMapping(phm);

			XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
			serverConfig.setEnabledForExtensions(true);
			serverConfig.setContentLengthOptional(false);

			webServer.start();
			System.out.println("AcmContester XML-RPC Interface started on " + port);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}
