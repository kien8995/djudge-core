package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;
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
				getConnector().getSubmissions(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				SubmissionData.class).toArray(new SubmissionData[0]);
	}

	@Override
	protected Class<? extends AbstractRemoteRow> getRowClass()
	{
		return SubmissionData.class;
	}

	public final static int getUserFieldIndex()
	{
		return 1;
	}
	
	public final static int getProblemIDFieldIndex()
	{
		return 18;
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
	
	@Override
	public boolean isCellEditable(int arg0, int arg1)
	{
		return false;
	}
}
