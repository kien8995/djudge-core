package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.utils.xmlrpc.AbstractRemoteRow;
import djudge.utils.xmlrpc.AbstractRemoteTable;
import djudge.utils.xmlrpc.HashMapSerializer;

public class RemoteTableSubmissions extends AbstractRemoteTable
{		
	private static final long serialVersionUID = 1L;

	public RemoteTableSubmissions(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		super(serverConnector, authData);
	}
	
	@Override
	protected AbstractRemoteRow[] getRows()
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				getConnector().getTeamSubmissions(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				SubmissionData.class).toArray(new SubmissionData[0]);
	}

	@Override
	protected Class<? extends AbstractRemoteRow> getRowClass()
	{
		return SubmissionData.class;
	}

	/*
	 	public String id; //0
	public String userID; //1
	public String problemID; //2
	public String languageID; //3
	public int contestTime;//4
	public String realTime;//5
	public String judgement;//6
	public int maxTime;//7
	public int maxMemory;//8
	public int maxOutput;//9
	public int failedTest;//10
	public int score;/11
	public int judged;//12
	public int active;//13
	public String sourceCode;//14
	public int djudgeFlag;//15
	public String xml;

	 */
	
	public final static int getUserFieldIndex()
	{
		return 1;
	}
	
	public final static int getProblemFieldIndex()
	{
		return 2;
	}
	
	public final static int getLanguageFieldIndex()
	{
		return 3;
	}
	
	public final static int getContestTimeFieldIndex()
	{
		return 4;
	}
	
	
	public final static int getActiveFlagIndex()
	{
		return 13;
	}
	
	public final static int getDJudgeFlagIndex()
	{
		return 15;
	}
	
	public final static int getJudgementFieldIndex()
	{
		return 6;
	}
	
	public final static int getRuntimeFieldIndex()
	{
		return 7;
	}
	
	public final static int getMemoryFieldIndex()
	{
		return 8;
	}
	
	public final static int getOutputFieldIndex()
	{
		return 9;
	}
	
	public final static int getFailedTestFieldIndex()
	{
		return 10;
	}
	
	public final static int getScoreFieldIndex()
	{
		return 11;
	}	
}
