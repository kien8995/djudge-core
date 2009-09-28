package djudge.acmcontester.structures;

import org.apache.log4j.Logger;


public abstract class AbstractRemoteRow
{
	protected static final Logger log = Logger.getLogger(AbstractRemoteRow.class);
	
	protected boolean fDataChanged = false;
	
	protected AbstractRemoteTable table;
	
	abstract Class<?extends AbstractRemoteTable> getTableClass();
	
	abstract int getColumnCount();
	
	AbstractRemoteRow()
	{
		
	}
	
	AbstractRemoteRow(AbstractRemoteTable table)
	{
		this.table = table;
	}
	
	void setParentTable(AbstractRemoteTable table)
	{
		this.table = table;
	}
	
	abstract Object getValueAt(int column);
	abstract void setValueAt(int column, String value);
	
	abstract boolean save();
	abstract boolean create();
	abstract boolean delete();

	boolean create(AbstractRemoteTable table)
	{
		this.table = table;
		return this.create();
	}
	
	void setValueAt(Object value, int column)
	{
		if (getValueAt(column).equals(value))
			return;
		fDataChanged = true;
		setValueAt(column, value.toString());
	}
}
