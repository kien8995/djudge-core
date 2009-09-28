package djudge.acmcontester.server;

import org.apache.log4j.Logger;

import db.LanguagesDataModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import db.UsersDataModel;

public class ContestServer
{
	/*
	 * XML-RPC server
	 */
	private static AcmContesterXmlRpcServer rpcServer;
	
	private static final Logger log = Logger.getLogger(ContestServer.class);
	
	private static ContestCore core = new ContestCore();
	
	public static ContestCore getCore()
	{
		return core;
	}
	
	public static void startServer()
	{
		rpcServer = new AcmContesterXmlRpcServer();
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
