package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.ServerXmlRpcConnector;
import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.common.HashMapSerializer;

public class RemoteTableLanguages extends AbstractRemoteTable
{

	private static final long serialVersionUID = 1L;

	public RemoteTableLanguages(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		super(serverConnector, authData);
	}
	
	@Override
	AbstractRemoteRow[] getRows()
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				getConnector().getLanguages(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				LanguageData.class).toArray(new LanguageData[0]);
	}

	@Override
	Class<? extends AbstractRemoteRow> getRowClass()
	{
		return LanguageData.class;
	}

}
