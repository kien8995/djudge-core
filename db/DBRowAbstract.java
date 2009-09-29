package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import djudge.acmcontester.server.ContestServer;


public abstract class DBRowAbstract extends SQLAbstract
{
	private static final Logger log = Logger.getLogger(DBRowAbstract.class);
	
	public Object[] data;
	
	private AbstractTableDataModel table;
	
	{
		try
		{
			table = (AbstractTableDataModel) getTableClass().newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public DBRowAbstract()
	{
		data = new Object[getColumnCount()];
	}
	
	protected String getFillStatement(int index)
	{
		return "SELECT * FROM " + getTableName() + " WHERE id = " + index;
	}
	
	public DBRowAbstract(AbstractTableDataModel table, ResultSet rs)
	{
		fill(rs, table);
	}
	
	public String[] getColumnKeys()
	{
		return table.getColumnKeys();
	}
	
	public Object[] getColumnDefaultValues()
	{
		return table.getColumnDefaultValues();
	}
	
	public String getTableName()
	{
		return table.getTableName();
	}
	
	public abstract Class<?extends AbstractTableDataModel> getTableClass();
	
	private void fillRow(ResultSet rs) throws SQLException
	{
		try
		{
			data = new Object[getColumnCount()];
			String[] columnKeys = getColumnKeys();
			for (int i = 0; i < getColumnCount(); i++)
			{
				data[i] = rs.getObject(columnKeys[i]);
				if (data[i] == null)
				{
					data[i] = getColumnDefaultValues()[i];
				}
			}
		}
		catch (SQLException e)
		{
			log.error("fillRow", e);
		}
	}
	
	public int getColumnCount()
	{
		return table.getColumnCount();
	}

	private String getCreateStatement()
	{
		String[] columns = new String[getColumnCount() - 1];
		Object[] values = new Object[getColumnCount() - 1];
		String[] columnKeys = getColumnKeys();
		Object[] columnValues = getColumnDefaultValues();
		for (int i = 0; i < getColumnCount() - 1; i++)
		{
			columns[i] = columnKeys[i+1];
			values[i] = columnValues[i+1];
		}
		
		StringBuffer s = new StringBuffer();
		setInsert(s, getTableName());
		setValues2(s, columns, values);
		return s.toString();
	}
	
	private String getCreateStatement2()
	{
		String[] columns = new String[getColumnCount() - 1];
		Object[] values = new Object[getColumnCount() - 1];
		String[] columnKeys = getColumnKeys();
		Object[] columnValues = new Object[getColumnCount()];
		for (int i = 0; i < getColumnCount(); i++)
			columnValues[i] = data[i];
		for (int i = 0; i < getColumnCount() - 1; i++)
		{
			columns[i] = columnKeys[i+1];
			values[i] = columnValues[i+1];
		}
		
		StringBuffer s = new StringBuffer();
		setInsert(s, getTableName());
		setValues2(s, columns, values);
		return s.toString();
	}
	
	public boolean create(AbstractTableDataModel table)
	{
		this.table = table;
		boolean f = false;
		try
		{
			synchronized (AbstractTableDataModel.dbMutex)
			{
				Connection con = Settings.getConnection();
				Statement stmt = con.createStatement();
				String query = getCreateStatement();
				log.debug(query);
				stmt.executeUpdate(query);
				stmt.close();
				query = "SELECT * FROM " + getTableName() + " ORDER BY id desc LIMIT 0,1";
				log.debug(query);
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next())
				{
					DBRowAbstract newRow = table.getRowClass().newInstance();
					newRow.fillRow(rs);
					table.rows.add(newRow);
					f = true;
				}
				stmt.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			f = false;
		}
		return f;
	}
	
	public boolean delete(AbstractTableDataModel table)
	{
		this.table = table;
		boolean f = false;
		try
		{
			synchronized (AbstractTableDataModel.dbMutex)
			{
				Connection con = Settings.getConnection();
				Statement stmt = con.createStatement();
				String query = "DELETE FROM `" + getTableName() + "` WHERE id = " + this.data[0];
				log.debug(query);
				stmt.executeUpdate(query);
			}
		}
		catch (Exception e)
		{
			log.error("delete", e);
			f = false;
		}
		return f;
	}
	
	public boolean fill(ResultSet rs, AbstractTableDataModel table)
	{
		this.table = table;
		boolean res = true;
		try
		{
			fillRow(rs);
		}
		catch (SQLException e)
		{
			log.error("fill", e);
			res = false;
		}
		return res;
	}
	
	private String getUpdateStatement()
	{
		StringBuffer s = new StringBuffer();
		try
		{
    		List<String> columns = new LinkedList<String>();
    		List<Object> values = new LinkedList<Object>();
    		String[] columnKeys = getColumnKeys();
    		DBField[] fields = getTableClass().newInstance().columns;
    		for (int i = 0; i < getColumnCount(); i++)
    		{
    			if (fields[i].flagUpdate)
    			{
    				columns.add(columnKeys[i]);
    				values.add(data[i]);
    			}
    		}
    		
    		String[] where = {"id = "};
    		String[] whereVal = {data[0].toString()};
    		
    		setUpdate(s, getTableName());
    		setValues(s, columns.toArray(new String[0]), values.toArray());
    		setWhere(s, where, whereVal);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
				
		return s.toString();
	}
	
	public final boolean save()
	{
		boolean f = true;
		try
		{
			synchronized (AbstractTableDataModel.dbMutex)
			{
				Connection con = Settings.getConnection();
				Statement stmt = con.createStatement();
				String query = this.getUpdateStatement();
				log.debug(query.substring(0, Math.min(query.length(), 80)));
				stmt.executeUpdate(query);
				stmt.close();
			}
		}
		catch (Exception e)
		{
			log.error("save", e);
			f = false;
		}
		return f;
	}

	public boolean appendTo(AbstractTableDataModel abstractTableDataModel)
	{
		this.table = abstractTableDataModel;
		boolean f = false;
		try
		{
			synchronized (AbstractTableDataModel.dbMutex)
			{
				Connection con = Settings.getConnection();
				Statement stmt = con.createStatement();
				String query = getCreateStatement2();
				log.debug(query);
				stmt.executeUpdate(query);
				stmt.close();
				query = "SELECT * FROM " + getTableName() + " ORDER BY id desc LIMIT 0,1";
				log.debug(query);
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next())
				{
					DBRowAbstract newRow = table.getRowClass().newInstance();
					newRow.fillRow(rs);
					table.rows.add(newRow);
					f = true;
				}
				stmt.close();
			}
		}
		catch (Exception e)
		{
			log.error("appendTo", e);
			e.printStackTrace();
			f = false;
		}
		return f;		
	}
	
	@Override
	public String toString()
	{
		String res = "[";
		for (int i = 0; i < data.length; i++)
		{
			res += data[i].toString() + (i == data.length - 1 ? ']' : ", ");
		}
		return res;
	}
}
