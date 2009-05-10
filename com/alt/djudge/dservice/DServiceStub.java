package com.alt.djudge.dservice;

import java.util.HashMap;

public class DServiceStub implements DServiceClientInterface
{
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
	}

}
