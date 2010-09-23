package djudge.acmcontester.server;

import org.apache.log4j.Logger;

import djudge.utils.XMLSettings;
import djudge.utils.xmlrpc.XmlRpcServer;

public class ContesterXmlRpcServer extends Thread
{
	private static final Logger log = Logger.getLogger(ContesterXmlRpcServer.class);
	
	private static final int defaultXmlRpcServicePort = 8202;
	
	private static final String defaultXmlRpcServiceName = "AcmContester";
	
	private static final int currentXmlRpcServicePort;
	
	private static final String currentXmlRpcServiceName;
	
	private static XmlRpcServer xmlRpcServer;
	
	private static final XMLSettings settings;
	
	static
	{
		settings = new XMLSettings(ContesterXmlRpcServer.class);
		currentXmlRpcServicePort = settings.getInt("rpc-port", defaultXmlRpcServicePort);
		currentXmlRpcServiceName = settings.getString("rpc-name", defaultXmlRpcServiceName);
	}
	
	public ContesterXmlRpcServer()
	{
		xmlRpcServer = new XmlRpcServer(currentXmlRpcServiceName, currentXmlRpcServicePort, ServerXmlRpcInterfaceStub.class);
	}
	
	@Override
	public void run()
	{
		try
		{
			xmlRpcServer.start();
			log.info(currentXmlRpcServiceName + " XML-RPC interface started on port " + currentXmlRpcServicePort);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}