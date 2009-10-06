package djudge.acmcontester.server;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class ContesterXmlRpcServer extends Thread
{
//	private static final Logger log = Logger.getLogger(ContesterXmlRpcServer.class);
	
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

/*
package djudge.dservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import utils.NanoHTTPD;
import utils.NanoHTTPD.Response;

import djudge.utils.HtmlUtils;
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
	
	
	public Response serve(String uri, String method, Properties header,
			Properties parms)
	{
		try
		{
    		log.info("Request acepted \"" + uri + "\"");
    		String res = "";
    		if (uri.startsWith("/clients"))
    		{
    			
    		}
    		else
    		{
    			res = core.getHtmlPage(uri);
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
 
 */
