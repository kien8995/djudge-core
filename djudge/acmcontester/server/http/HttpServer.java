package djudge.acmcontester.server.http;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import utils.NanoHTTPD;

import djudge.acmcontester.interfaces.ServerNativeInterface;
import djudge.acmcontester.server.XMLSettings;
import djudge.utils.HtmlUtils;

public class HttpServer extends NanoHTTPD implements Runnable
{
	private static final Logger log = Logger.getLogger(HttpServer.class);

	public static final int defaultPort = 8282;
	
	private ServerNativeInterface serverConnector;
	
	private final String username;
	
	private final String password;
	
	private final String serverEchoString = "Hello from " + this.toString();
	
	public HttpServer(String dataProviderClassName, int port, String username, String password) throws IOException
	{
		super(port);
		this.password = password;
		this.username = username;
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
		log.info("HttpMirror started at port " + port);
		log.info("ServerConnector: " + dataProviderClassName + " {" + username + ", " + password + "}");
	}
	
	public Response serve(String uri, String method, Properties header,
			Properties parms)
	{
		log.info("Request acepted \"" + uri + "\"");
		String res = "";
		boolean color = parms.containsKey("color");
		if (uri.startsWith("/sumatt"))
		{
			res = HtmlUtils.getSumatt(serverConnector.getTeamMonitor(username, password), color);
		}
		else if (uri.startsWith("/summary"))
		{
			res = HtmlUtils.getSummary(serverConnector.getTeamMonitor(username, password), color);
		}
		else if (uri.startsWith("/monitor") || uri.startsWith("/standings"))
		{
			res = HtmlUtils.getMonitor(serverConnector.getTeamMonitor(username, password), parms);
		}
		else if (uri.startsWith("/submissions") || uri.startsWith("/queue"))
		{
			res = HtmlUtils.getSubmissions(serverConnector.getTeamSubmissions(username, password), parms);
		}
		else
		{
			res = "<html><head><title>Contest index</title></head><body>" +
				"<li><a href='/sumatt.html?color=true'>Standings</a>" + 
				"<li><a href='/submissions.html?id=1&contesttime=1&realtime=1&realtime=1&user=1&problem=1&language=1&judgement=1&time=1&memory=1&output=1'>Submissions</a>" + 
				"<li>Users<br><br><hr>" +
				"PC^2-style reports" + 
				"<li><a href='/sumatt.html'>sumatt.html</a>" +
				"<li><a href='/summary.html'>summary.html</a>" +
				"<li><a href='/sumatt.html'>sumatt.html</a>" +
				"<li><a href='/sumatt.html'>sumatt.html</a>" +
					"</body></html>";
		}
/*		System.out.println(method + " '" + uri + "' ");
		String msg = "<html><body><h1>Hello server</h1>\n";
		if (parms.getProperty("username") == null)
			msg += "<form action='?' method='get'>\n"
					+ "  <p>Your name: <input type='text' name='username'></p>\n"
					+ "</form>\n";
		else
			msg += "<p>Hello, " + parms.getProperty("username") + "!</p>";

		msg += "</body></html>\n";*/
		return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, res);
	}

	
	
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}	
	
	public static void main(String[] args)
	{
		XMLSettings settings = new XMLSettings("server-http.xml");
		try
		{
			new HttpServer(settings.getProperty("data-provider"), settings.getInt(
					"port", 8283), settings.getProperty("server-username"),
					settings.getProperty("server-password")).run();
		} catch (IOException e)
		{
			log.fatal("HTTP Server failed", e);
		}
	}
}
