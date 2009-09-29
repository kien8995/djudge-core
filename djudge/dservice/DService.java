package djudge.dservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Date;
import java.util.Vector;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import djudge.acmcontester.server.ContestCore;
import djudge.filesystem.RemoteFS;
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

