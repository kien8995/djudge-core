package djudge.acmcontester.structures;

public class RemoteRowStub extends AbstractRemoteRow
{

	@Override
	int getColumnCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	Class<? extends AbstractRemoteTable> getTableClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Object getValueAt(int column)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void setValueAt(int column, String value)
	{
		// TODO Auto-generated method stub
	}

	@Override
	boolean save()
	{
		//table.getConnector().
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean create()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean delete()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
