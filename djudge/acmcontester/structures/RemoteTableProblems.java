package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;
import djudge.utils.xmlrpc.AbstractRemoteRow;
import djudge.utils.xmlrpc.AbstractRemoteTable;
import djudge.utils.xmlrpc.HashMapSerializer;

public class RemoteTableProblems extends AbstractRemoteTable
{

	private static final long serialVersionUID = 1L;

	public RemoteTableProblems(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		super(serverConnector, authData);
	}
	
	@Override
	protected AbstractRemoteRow[] getRows()
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				getConnector().getTeamProblems(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				ProblemData.class).toArray(new ProblemData[0]);
	}

	@Override
	protected Class<? extends AbstractRemoteRow> getRowClass()
	{
		return ProblemData.class;
	}

}
