package djudge.acmcontester.server;

import org.apache.log4j.Logger;

public class ContestServer
{
	/*
	 * XML-RPC server
	 */
	private static ContesterXmlRpcServer rpcServer;
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ContestServer.class);
	
	private static ContestCore core = new ContestCore();
	
	public static ContestCore getCore()
	{
		return core;
	}
	
	public static void startServer()
	{
		core.stopCore();
		core = new ContestCore(true);
		rpcServer = new ContesterXmlRpcServer();
		rpcServer.start();
	}
	
	public static void main(String[] args)
	{
		startServer();
	}
	
	public static void stopServer()
	{
		core.stopCore();
	}
}
