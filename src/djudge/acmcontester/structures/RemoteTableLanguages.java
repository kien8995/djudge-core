/* $Id$ */

package djudge.acmcontester.structures;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;
import djudge.utils.xmlrpc.AbstractRemoteRow;
import djudge.utils.xmlrpc.AbstractRemoteTable;
import djudge.utils.xmlrpc.HashMapSerializer;

public class RemoteTableLanguages extends AbstractRemoteTable
{

	private static final long serialVersionUID = 1L;

	public RemoteTableLanguages(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		super(serverConnector, authData);
	}
	
	@Override
	protected AbstractRemoteRow[] getRows()
	{
		return HashMapSerializer.deserializeFromHashMapArray(
				getConnector().getLanguages(
						getAuthentificationData().getUsername(),
						getAuthentificationData().getPassword()),
				LanguageData.class).toArray(new LanguageData[0]);
	}

	@Override
	protected Class<? extends AbstractRemoteRow> getRowClass()
	{
		return LanguageData.class;
	}

	protected String[] getColumnNames()
	{
		final String[] names = {
			"ID",
			"SID",
			"Name",
			"DJudgeID",
		};
		return names;
	}	
}
