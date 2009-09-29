package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.ServerXmlRpcConnector;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.common.HashMapSerializer;

public class RemoteTableUsers extends AbstractRemoteTable
{
	private static final long serialVersionUID = 1L;

	public RemoteTableUsers(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		super(serverConnector, authData);
	}
	
	@Override
	AbstractRemoteRow[] getRows()
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				getConnector().getUsers(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				UserData.class).toArray(new UserData[0]);
	}

	@Override
	Class<? extends AbstractRemoteRow> getRowClass()
	{
		return UserData.class;
	}

}
