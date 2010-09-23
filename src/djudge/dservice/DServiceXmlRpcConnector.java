package djudge.dservice;

import java.util.HashMap;

import djudge.dservice.interfaces.DServiceXmlRpcInterface;
import djudge.utils.XMLSettings;
import djudge.utils.xmlrpc.XmlRpcConnector;

public class DServiceXmlRpcConnector extends XmlRpcConnector implements DServiceXmlRpcInterface
{
	private static final String defaultServiceName = "DService";
	
	private static final String defaultServiceUrl = "http://127.0.0.1:8001/xmlrpc"; 
	
	private static final String serviceName;
	
	private static final String serverURL;
	
	private static final int connetionTimeout;
	
	private static final int replyTimeout;
	
	static
	{
		XMLSettings settings = new XMLSettings(DServiceXmlRpcConnector.class);
		serviceName = settings.getString("service-name", defaultServiceName);
		serverURL = settings.getString("service-url", defaultServiceUrl);
		connetionTimeout = settings.getInt("connnection-timeout", 2000);
		replyTimeout = settings.getInt("reply-timeout", 2000);
	}
	
	public DServiceXmlRpcConnector()
	{
		super(serviceName, serverURL, connetionTimeout, replyTimeout);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap[] fetchResults(String uid)
	{
		Object remoteResult = callRemoteMethod("fetchResults", uid);
		return objectToHashMapArray(remoteResult);
	}

	@Override
	public int submitSolution(String uid, String contestId, String problemId,
			String languageId, String source, String clientData, String params)
	{
		return (Integer) callRemoteMethod("submitSolution", uid,
				contestId, problemId, languageId, source, clientData, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap getTask(int judgeID)
	{
		return (HashMap) callRemoteMethod("getTask", judgeID);
	}

	@Override
	public boolean setTaskResult(int taskID, String judgement, String xmlData)
	{
		return (Boolean) callRemoteMethod("setTaskResult", taskID, judgement, xmlData);
	}
}
