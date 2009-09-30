package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.utils.xmlrpc.AbstractRemoteRow;
import djudge.utils.xmlrpc.AbstractRemoteTable;
import djudge.utils.xmlrpc.HashMapSerializer;

public class RemoteTableUsers extends AbstractRemoteTable
{
	private static final long serialVersionUID = 1L;

	public RemoteTableUsers(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		super(serverConnector, authData);
	}
	
	@Override
	protected AbstractRemoteRow[] getRows()
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				getConnector().getUsers(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				UserData.class).toArray(new UserData[0]);
	}

	@Override
	protected Class<? extends AbstractRemoteRow> getRowClass()
	{
		return UserData.class;
	}

}
