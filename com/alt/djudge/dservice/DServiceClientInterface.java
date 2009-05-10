package com.alt.djudge.dservice;

public interface DServiceClientInterface
{
	String createUser(String username, String password);
	
	int deleteUser(String username, String password);
	
	int submitSolution(String uid, String contestId, String problemId, String languageId, String source, String clientData);
	
	//DServiceTaskResult[] getSubmissionResult(String uid, int count);
}
