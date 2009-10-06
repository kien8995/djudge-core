package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;
import djudge.acmcontester.structures.SubmissionData.SubmissionDataColumnsEnum;
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
	
	protected String[] getColumnNames()
	{
		final String[] names = {
			"ID",
			"Team",
			"Problem",
			"Language",
			"Time",
			"Real Time",
			"Result",
			"Time consumed",
			"Memory consumed",
			"Wrong test",
			"Score",
			"Judged",
			"Active",
			"Src",
			"DJudge",
			"XML",
		};
		return names;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1)
	{
		return false;
	}
}
