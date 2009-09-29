package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.ServerXmlRpcConnector;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.common.HashMapSerializer;

public class RemoteTableProblems extends AbstractRemoteTable
{

	private static final long serialVersionUID = 1L;

	public RemoteTableProblems(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		super(serverConnector, authData);
	}
	
	@Override
	AbstractRemoteRow[] getRows()
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				getConnector().getProblems(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				ProblemData.class).toArray(new ProblemData[0]);
	}

	@Override
	Class<? extends AbstractRemoteRow> getRowClass()
	{
		return ProblemData.class;
	}

}
