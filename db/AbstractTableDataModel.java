package db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import sun.awt.image.DataBufferNative;

public abstract class AbstractTableDataModel extends AbstractTableModel
{
	public final static String dbMutex = "Mutex";
	
	public DBField[] columns;
	
	public String tableName;
	
	{
		columns = getTableFields();
		tableName = getTableName();
		fillSqlQuery = "SELECT * FROM `" + tableName + "`";
	}
	
	public void setWhere(String where)
	{
		fillSqlQuery = "SELECT * FROM `" + tableName + "` WHERE "  + where;
	}
	
	protected Vector<DBRowAbstract> rows = new Vector<DBRowAbstract>();
	
	protected abstract String getTableName();
	
	protected abstract DBField[] getTableFields();
	
	protected abstract Class<?extends DBRowAbstract> getRowClass();
	
	protected String fillSqlQuery;
	
	public AbstractTableDataModel()
	{
		
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
	
	public boolean fill()
	{
		return fillSql(fillSqlQuery);
	}
	
	public final boolean fillSql(String query)
	{
		boolean res = true;
		try
		{
			Statement stmt = Settings.getConnection().createStatement();
			log(query);
			ResultSet rs = stmt.executeQuery(query);
			rows = getRows(rs);
			stmt.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	protected static void log(Object s)
	{
		System.out.println(s);
	}

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
	
	public int getColumnCount()
	{
		return columns.length;
	}

	public Class<?> getColumnClass(int arg0)
	{
		return columns[arg0].type;
	}

	public String getColumnName(int arg0)
	{
		return columns[arg0].caption;
	}

	public int getRowCount()
	{
		return rows.size();
	}

	public Object getValueAt(int arg0, int arg1)
	{
		return rows.get(arg0).data[arg1];
	}

	public boolean isCellEditable(int arg0, int arg1)
	{
		return arg1 > 0;
	}

	public void setValueAt(Object arg0, int arg1, int arg2)
	{
		DBRowAbstract row = rows.get(arg1);
		row.data[arg2] = arg0;
		row.save();
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {}

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
}
