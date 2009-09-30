package djudge.acmcontester.server;

import org.apache.log4j.Logger;

public class ContestServer
{
	/*
	 * XML-RPC server
	 */
	private static ContesterXmlRpcServer rpcServer;
	
	private static final Logger log = Logger.getLogger(ContestServer.class);
	
	private static ContestCore core = new ContestCore();
	
	public static ContestCore getCore()
	{
		return core;
	}
	
	public static void startServer()
	{
		rpcServer = new ContesterXmlRpcServer();
		core.stopCore();
		core = new ContestCore(true);
		rpcServer.start();
	}
	
	public static void main(String[] args)
	{
		startServer();
	}
	
	public static void stopServer()
	{
		
	}
}
