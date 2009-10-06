package djudge.utils.xmlrpc;


import java.util.Vector;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;

public abstract class AbstractRemoteTable extends AbstractDataTable
{
	private ServerXmlRpcInterface serverConnector;
	
	private AuthentificationData authData;
	
	Vector<AbstractRemoteRow> rows = new Vector<AbstractRemoteRow>();
	
	private static final long serialVersionUID = 1L;

	protected abstract AbstractRemoteRow[] getRows();
	
	protected abstract Class<?extends AbstractRemoteRow> getRowClass();
	
	@Override
	public int getColumnCount()
	{
		int res = 0;
		try
		{
			res = ((AbstractRemoteRow)(getRowClass().newInstance())).getColumnCount();
		}
		catch (Exception e)
		{
		}
		return res;
	}

	@Override
	public int getRowCount()
	{
		return rows.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1)
	{
		return rows.get(arg0).getValueAt(arg1);
	}
	
	@Override
	public boolean isCellEditable(int arg0, int arg1)
	{
		return arg1 > 0;
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2)
	{
		AbstractRemoteRow row = rows.get(arg1);
		row.setValueAt(arg0, arg2);
	}
	
	@Override
	public String getColumnName(int arg0)
	{
		String[] names = getColumnNames();
		if (names == null)
			return "" + arg0;
		return names[arg0];
	}
	
	protected String[] getColumnNames()
	{
		return null;
	}
	
	public AbstractRemoteTable(ServerXmlRpcInterface serverConnector, AuthentificationData authData)
	{
		this.serverConnector = serverConnector;
		this.authData = authData;
	}
	
	public ServerXmlRpcInterface getConnector()
	{
		return serverConnector;
	}
	
	public AuthentificationData getAuthentificationData()
	{
		return authData;
	}
	
	@Override
	public boolean updateData()
	{
		rows.clear();
		AbstractRemoteRow[] rowsArray = getRows();
		for (int i = 0; i < rowsArray.length; i++)
		{
			rowsArray[i].setParentTable(this);
			rows.add(rowsArray[i]);
		}
		return true;
	}
	
	@Override
	public boolean saveData()
	{
		for (int i = 0; i < rows.size(); i++)
			rows.get(i).save();
		return true;
	}
	
	@Override
	public boolean insertRow()
	{
    	AbstractRemoteRow newRow;
		try
		{
			newRow = (AbstractRemoteRow) getRowClass().newInstance();
			boolean res = newRow.create(this);
			updateData();
			return res;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
	}
	
	@Override
	public boolean deleteRow(int iRow)
	{
		AbstractRemoteRow row = rows.get(iRow);
		row.delete();
		updateData();
		return true;
	}
	
	public AbstractRemoteRow getRow(int index)
	{
		return rows.get(index);
	}
}
