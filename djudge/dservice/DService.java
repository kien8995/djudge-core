package djudge.dservice;

import org.apache.log4j.Logger;

import djudge.utils.SimpleHttpServer;
import djudge.utils.SimpleHttpServerDataProvider;

public class DService
{
	private static final Logger log = Logger.getLogger(DService.class);
	
	private static final int dservicePort = 8001;
	
	private static final int httpPort = 5555;
	
	public static final String serviceName = "dservice";
	
	private static XmlRpcServer xmlRpcServer;
	
	private static SimpleHttpServer simpleHttpServer;
	
	private static final DServiceCore core = new DServiceCore();
	
	public static void main(String[] args)
	{
		xmlRpcServer  = new XmlRpcServer("DService", dservicePort);
		xmlRpcServer.start();
		simpleHttpServer = new SimpleHttpServer(new Temp(), httpPort);
	}
	
	public static String getHtmlPage(String query)
	{
		return core.getHtmlPage(query);
	}
	
	public static DServiceCore getCore()
	{
		return core;
	}
}

class Temp implements SimpleHttpServerDataProvider
{
	@Override
	public String getHtmlPage(String query)
	{
		return DService.getHtmlPage(query);
	}
}

