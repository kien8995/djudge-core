/* $Id$ */

package djudge.utils.xmlrpc;

public class RemoteRowStub extends AbstractRemoteRow
{

	@Override
	protected int getColumnCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Class<? extends AbstractRemoteTable> getTableClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getValueAt(int column)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setValueAt(int column, String value)
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean save()
	{
		//table.getConnector().
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean create()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean delete()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
