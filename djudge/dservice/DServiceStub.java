package djudge.dservice;

import java.util.HashMap;

import djudge.dservice.interfaces.DServiceXmlRpcInterface;
import djudge.utils.xmlrpc.HashMapSerializer;

public class DServiceStub implements DServiceXmlRpcInterface
{

	@Override
	public int submitSolution(String uid, String contestId, String problemId,
			String languageId, String source, String clientData)
	{
		return DService.getCore().submitSolution(uid, contestId, problemId, languageId, source, clientData);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap[] fetchResults(String uid)
	{
		return HashMapSerializer.serializeToHashMapArray(DService.getCore().fetchResults(uid));
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap getTask(int judgeID)
	{
		DServiceTask task = DService.getCore().getTask(judgeID);
		return task == null ? null : task.toHashMap();
	}

	@Override
	public boolean setTaskResult(int taskID, String judgement, String xmlData)
	{
		return DService.getCore().setTaskResult(taskID, judgement, xmlData);
	}
	
	/*
	@SuppressWarnings("unchecked")
	public HashMap<String, String>[] fetchResults(String uid)
	{
		DServiceTaskResult[] res = DService.fetchResults(uid);
		if (res == null) return null;
		int c = res.length;
		HashMap res2[] = new HashMap[c];
		for (int i = 0; i < c; i++)
		{
			res2[i] = res[i].toHashMap();
		}
		return res2;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String>[] getSubmissionResult(String uid, int count)
	{
		DServiceTaskResult[] res = DService.getSubmissionResult(uid, count);
		if (res == null) return null;
		int c = res.length;
		HashMap res2[] = new HashMap[c];
		for (int i = 0; i < c; i++)
		{
			res2[i] = res[i].toHashMap();
		}
		return res2;
	}
	
	public boolean setTaskResult(int taskID, String judgement, String xmlData)
	{
		DService.setTaskResult(taskID, judgement, xmlData);
		return true;
	}
	
	public HashMap<String, String> getTask(int judgeID)
	{
		DServiceTask task = DService.getTask(judgeID);
		return task != null ? task.toHashMap() : null;
	}
	
	@Override
	public String createUser(String username, String password)
	{
		return DService.createUser(username, password);
	}

	@Override
	public int deleteUser(String username, String password)
	{
		return DService.deleteUser(username, password);
	}

	@Override
	public int submitSolution(String uid, String contestId, String problemId,
			String languageId, String source, String clientData)
	{
		return DService.submitSolution(uid, contestId, problemId, languageId, source, clientData);
	}*/

}
