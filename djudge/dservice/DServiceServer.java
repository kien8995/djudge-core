package djudge.dservice;

import java.io.IOException;
import org.apache.log4j.Logger;

import utils.NanoHTTPD;

import djudge.utils.XMLSettings;
import djudge.utils.xmlrpc.XmlRpcServer;

public class DServiceServer extends NanoHTTPD
{
	private static final Logger log = Logger.getLogger(DServiceServer.class);
	
	private static final int defaultXmlRpcServicePort = 8001;
	
	private static final int defaultHttpPort = 5555;
	
	private static final String defaultXmlRpcServiceName = "DService";
	
	private static final int currentXmlRpcServicePort;
	
	private static final int currentHttpPort;
	
	private static final String currentXmlRpcServiceName;
	
	private static XmlRpcServer xmlRpcServer;
	
	private static final DServiceCore core = new DServiceCore();
	
	static final XMLSettings settings;
	
	static
	{
		settings = new XMLSettings(DServiceServer.class);
		currentHttpPort = settings.getInt("http-port", defaultHttpPort);
		currentXmlRpcServicePort = settings.getInt("rpc-port", defaultXmlRpcServicePort);
		currentXmlRpcServiceName = settings.getString("rpc-name", defaultXmlRpcServiceName);
	}
	
	public DServiceServer(int port) throws IOException
	{
		super(port);
	}	
	
	public static DServiceCore getCore()
	{
		return core;
	}
	
	public static void main(String[] args)
	{
		try
		{
			new DServiceServer(currentHttpPort);
			log.info("Http server started at port " + currentHttpPort);
		}
		catch (Exception e)
		{
			log.error("Could not start http server", e);
		}
		xmlRpcServer = new XmlRpcServer(currentXmlRpcServiceName, currentXmlRpcServicePort, DServiceXmlRpcStub.class);
		xmlRpcServer.start();
		log.info("XmlRpc server <" + currentXmlRpcServiceName + "> starter at port " + currentXmlRpcServicePort);
	}	
}
