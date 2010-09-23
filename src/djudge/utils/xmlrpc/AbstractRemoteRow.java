/* $Id$ */

package djudge.utils.xmlrpc;

import org.apache.log4j.Logger;

public abstract class AbstractRemoteRow
{
	protected static final Logger log = Logger.getLogger(AbstractRemoteRow.class);
	
	protected boolean fDataChanged = false;
	
	protected AbstractRemoteTable table;
	
	protected abstract Class<?extends AbstractRemoteTable> getTableClass();
	
	protected abstract int getColumnCount();
	
	protected AbstractRemoteRow()
	{
		
	}
	
	protected AbstractRemoteRow(AbstractRemoteTable table)
	{
		this.table = table;
	}
	
	protected void setParentTable(AbstractRemoteTable table)
	{
		this.table = table;
	}
	
	protected abstract Object getValueAt(int column);
	protected abstract void setValueAt(int column, String value);
	
	protected abstract boolean save();
	protected abstract boolean create();
	protected abstract boolean delete();

	protected boolean create(AbstractRemoteTable table)
	{
		this.table = table;
		return this.create();
	}
	
	protected void setValueAt(Object value, int column)
	{
		if (getValueAt(column).equals(value))
			return;
		fDataChanged = true;
		setValueAt(column, value.toString());
	}
}
