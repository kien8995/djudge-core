package db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import org.apache.log4j.Logger;

import djudge.utils.xmlrpc.AbstractDataTable;

public abstract class AbstractTableDataModel extends AbstractDataTable
{
	protected static final Logger log = Logger.getLogger(AbstractTableDataModel.class);
	
	public final static String dbMutex = "Mutex";
	
	public DBField[] columns;
	
	public String tableName;
	
	{
		columns = getTableFields();
		tableName = getTableName();
		fillSqlQuery = getFillStatement();
	}
	
	protected String getFillStatement()
	{
		return "SELECT * FROM `" + tableName + "`";
	}
	
	public void setWhere(String where)
	{
		fillSqlQuery = getFillStatement() + " WHERE "  + where;
	}
	
	protected Vector<DBRowAbstract> rows = new Vector<DBRowAbstract>();
	
	protected abstract String getTableName();
	
	protected abstract DBField[] getTableFields();
	
	protected abstract Class<?extends DBRowAbstract> getRowClass();
	
	protected String fillSqlQuery;
	
	public AbstractTableDataModel()
	{
		//!!!
		fillSqlQuery = getFillStatement();
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends DBRowAbstract> Vector<T> getRows(ResultSet rs)
	{
		Vector<T> res = new Vector<T>();
		try
		{
    		while(rs.next())
    		{
    			T row = (T) getRowClass().newInstance();
    			row.fill(rs, this);
    			res.add(row);
    		}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}
	
	@Override
	public boolean updateData()
	{
		return fillSql(fillSqlQuery);
	}
	
	public final boolean fillSql(String query)
	{
		boolean res = true;
		try
		{
			synchronized (dbMutex)
			{
				Statement stmt = Settings.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(query);
				rows = getRows(rs);
				if (log.isDebugEnabled() && false)
				{
					for (int i = 0; i < rows.size(); i++)
					{
						log.debug(rows.get(i).toString());
					}
				}
				stmt.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	@Override
	public boolean insertRow()
	{
		try
		{
    		DBRowAbstract newRow = (DBRowAbstract) getRowClass().newInstance();
    		newRow.create(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;		
	}
	
	public boolean insertRow(DBRowAbstract row)
	{
		try
		{
			row.appendTo(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;		
	}
	
	protected String[] getColumnNames()
	{
		String[] res = new String[columns.length];
		for (int i = 0; i < columns.length; i++)
			res[i] = columns[i].caption;
		return res;
	}
	
	public Object[] getColumnDefaultValues()
	{
		Object[] res = new Object[columns.length];
		for (int i = 0; i < columns.length; i++)
			res[i] = columns[i].defaultValue;
		return res;
	}

	protected String[] getColumnKeys()
	{
		String[] res = new String[columns.length];
		for (int i = 0; i < columns.length; i++)
			res[i] = columns[i].key;
		return res;
	}
	
	@Override
	public int getColumnCount()
	{
		return columns.length;
	}

	@Override
	public Class<?> getColumnClass(int arg0)
	{
		return columns[arg0].type;
	}

	@Override
	public String getColumnName(int arg0)
	{
		return columns[arg0].caption;
	}

	@Override
	public int getRowCount()
	{
		return rows.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1)
	{
		return rows.get(arg0).data[arg1];
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1)
	{
		return arg1 > 0;
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2)
	{
		DBRowAbstract row = rows.get(arg1);
		row.data[arg2] = arg0;
		row.save();
	}
	
	public boolean isValidID(String id)
	{
		for (int i = 0; i < rows.size(); i++)
		{
			if (rows.get(i).data[0].toString().equals(id))
			{
				return true;
			}
		}
		return false;
	}
	
	public void setRowData(int rowIndex, Object[] data)
	{
		DBRowAbstract row = rows.get(rowIndex);
		row.data = data;
		row.save();
	}

	@Override
	public boolean deleteRow(int iRow)
	{
		DBRowAbstract row = rows.get(iRow);
		row.delete(this);
		updateData();
		return true;
	}
	
	public DBRowAbstract getRowByID(String id)
	{
		for (int i = 0; i < rows.size(); i++)
		{
			if (rows.get(i).data[0].toString().equals(id))
				return rows.get(i);
		}
		return null;
	}
	
	@Override
	public boolean saveData()
	{
		return true;
	}
	
	public DBRowAbstract getRow(int index)
	{
		return rows.get(index);
	}
}
