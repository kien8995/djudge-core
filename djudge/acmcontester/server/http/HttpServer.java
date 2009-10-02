package djudge.acmcontester.server.http;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import utils.NanoHTTPD;

import djudge.acmcontester.server.interfaces.ServerNativeInterface;
import djudge.acmcontester.structures.MonitorData;
import djudge.utils.CachedObject;
import djudge.utils.HtmlUtils;
import djudge.utils.XMLSettings;

public class HttpServer extends NanoHTTPD implements Runnable
{
	private static final Logger log = Logger.getLogger(HttpServer.class);

	public static final int defaultPort = 8282;
	
	public static final String defaultUsername = "board";
	
	public static final String defaultPassword = "board";
	
	private ServerNativeInterface serverConnector;
	
	private final String username;
	
	private final String password;
	
	private final String serverEchoString = "Hello from " + this.toString();
	
	private final CachedMonitor monitor;
	
	private final static long defaultCacheUpdateInterval = 10; // 10 seconds
	
	private class CachedMonitor extends CachedObject
	{
		public CachedMonitor(long cacheUpdateInterval)
		{
			super(cacheUpdateInterval);
		}
		
		public MonitorData getMonitor()
		{
			return (MonitorData) getData();
		}

		@Override
		protected Object updateData() throws Exception
		{
			return serverConnector.getTeamMonitor(username, password);
		}
	}
	
	public HttpServer() throws Exception
	{
		super(new XMLSettings(HttpServer.class).getInt("http-port", defaultPort));
		XMLSettings settings = new XMLSettings(this.getClass());
		this.username = settings.getString("server-username", defaultUsername);
		this.password = settings.getString("server-password", defaultPassword);
		monitor = new CachedMonitor(settings.getLong("cache-update-monitor-interval", defaultCacheUpdateInterval) * 1000);
		initConnector(settings.getString("server-data-provider", "wefdwerf"));
		printInfo(settings.getString("server-data-provider", "wefdwerf"), settings.getInt("http-port", defaultPort));
	}
	
	private void initConnector(String dataProviderClassName)
	{
		try
		{
			serverConnector = (ServerNativeInterface) Class.forName(dataProviderClassName).newInstance();
			if (!serverEchoString.equals(serverConnector.echo(serverEchoString)))
				throw new Exception();
		}
		catch (IllegalAccessException ex)
		{
			log.fatal("Wrong class name: " + dataProviderClassName, ex);
			System.exit(1);
		}
		catch (ClassNotFoundException ex)
		{
			log.fatal("Cannot find data provider class: " + dataProviderClassName, ex);
			System.exit(1);
		}
		catch (InstantiationException ex)
		{
			log.fatal("Cannot create class: " + dataProviderClassName, ex);
			System.exit(1);
		}
		catch (Exception ex)
		{
			log.fatal("Cannot connect to server: ", ex);
			System.exit(2);
		}
	}
	
	public HttpServer(String dataProviderClassName, int port, String username, String password) throws Exception
	{
		super(port);
		monitor = new CachedMonitor(defaultCacheUpdateInterval * 1000);
		this.password = password;
		this.username = username;
		initConnector(dataProviderClassName);
		printInfo(dataProviderClassName, port);
	}
	
	private void printInfo(String dataProviderClassName, int port)
	{
		log.info("");
		log.info("");
		log.info("");
		log.info("***********************************");
		log.info("HttpMirror started at port " + port);
		log.info("ServerConnector: " + dataProviderClassName + " {" + username + ", " + password + "}");
	}
	
	public Response serve(String uri, String method, Properties header,
			Properties parms)
	{
		try
		{
    		log.info("Request acepted \"" + uri + "\"");
    		String res = "";
    		boolean color = parms.containsKey("color");
    		if (uri.startsWith("/sumatt"))
    		{
    			res = HtmlUtils.getSumatt(monitor.getMonitor(), color);
    		}
    		else if (uri.startsWith("/summary"))
    		{
    			res = HtmlUtils.getSummary(monitor.getMonitor(), color);
    		}
    		else if (uri.startsWith("/monitor") || uri.startsWith("/standings"))
    		{
    			res = HtmlUtils.getMonitorACM(monitor.getMonitor(), parms);
    		}
    		else if (uri.startsWith("/school_monitor") || uri.startsWith("/school_standings"))
    		{
    			res = HtmlUtils.getMonitorIOI(monitor.getMonitor(), parms);
    		}
    		else if (uri.startsWith("/submissions") || uri.startsWith("/queue"))
    		{
    			res = HtmlUtils.getSubmissions(serverConnector.getTeamSubmissions(username, password), parms);
    		}
    		else
    		{
    			res = "<html><head><title>Contest index</title></head><body>" +
    				"<li><a href='/standings?bgcolor=true&txcolor=true&rowcolor=1&info=tt'>ACM-standings</a>" + 
    				"<li><a href='/school_standings?bgcolor=true&txcolor=true&rowcolor=1&info=tt'>IOI-standings</a> (<a href='/school_standings?bgcolor=true&txcolor=true&rowcolor=1&info=st'>w/time</a>)" + 
    				"<li><a href='/submissions.html?id=1&contesttime=1&realtime=1&realtime=1&user=1&problem=1&language=1&judgement=1&time=1&memory=1&output=1'>Submissions</a>" + 
    				"<li>Users<br><br><hr>" +
    				"PC^2-style reports" + 
    				"<li><a href='/sumatt.html'>sumatt.html</a>" +
    				"<li><a href='/summary.html'>summary.html</a>" +
    				"<li><a href='/sumatt.html'>sumatt.html</a>" +
    				"<li><a href='/sumatt.html'>sumatt.html</a>" +
    					"</body></html>";
    		}
    		return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, res);
		}
		catch (Exception e)
		{
			log.debug("Exceprion while processing request");
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			e.printStackTrace(ps);
			return new NanoHTTPD.Response(HTTP_INTERNALERROR, MIME_HTML, "<html><body><h1>Internal server error</h1><font color='red'>" + new String(os.toByteArray()) + "</font></body></html>");
		}
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(10000);
			} catch (InterruptedException e)
			{
				log.info("Error while Thread.sleep", e);
			}
		}
	}	
	
	public static void main(String[] args)
	{
		try
		{
			new HttpServer().run();
		} catch (Exception e)
		{
			log.fatal("HTTP Server failed", e);
		}
	}
}
