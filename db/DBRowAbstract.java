package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public abstract class DBRowAbstract extends SQLAbstract
{
	public Object[] data;
	
	private DBTableAbstract table;
	
	{
		try
		{
			table = (DBTableAbstract) getTableClass().newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public DBRowAbstract()
	{
		
	}
	
	protected String getFillStatement(int index)
	{
		return "SELECT * FROM " + getTableName() + " WHERE id = " + index;
	}
	
	public DBRowAbstract(DBTableAbstract table, ResultSet rs)
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
	
	public abstract Class<?extends DBTableAbstract> getTableClass();
	
	private void fillRow(ResultSet rs) throws SQLException
	{
		try
		{
			data = new Object[getColumnCount()];
			String[] columnKeys = getColumnKeys();
			for (int i = 0; i < getColumnCount(); i++)
				data[i] = rs.getObject(columnKeys[i]);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
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
	
	public boolean create(DBTableAbstract table)
	{
		this.table = table;
		boolean f = false;
		try
		{
			Connection con = Settings.getConnection();
			Statement stmt = con.createStatement();
			String query = getCreateStatement();
			log(query);
			stmt.executeUpdate(query);
			stmt.close();
			query = "SELECT * FROM " + getTableName() + " ORDER BY id desc LIMIT 0,1";
			log(query);
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
		catch (Exception e)
		{
			e.printStackTrace();
			f = false;
		}
		return f;
	}
	
	public boolean delete(DBTableAbstract table)
	{
		this.table = table;
		boolean f = false;
		try
		{
			Connection con = Settings.getConnection();
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "DELETE FROM `" + getTableName() + "` WHERE id = " + this.data[0];
			log(query);
			stmt.executeUpdate(query);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			f = false;
		}
		return f;
	}
	
	public boolean fill(ResultSet rs, DBTableAbstract table)
	{
		this.table = table;
		boolean res = true;
		try
		{
			fillRow(rs);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
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
			Connection con = Settings.getConnection();
			Statement stmt = con.createStatement();
			String query = this.getUpdateStatement();
			log(query);
			stmt.executeUpdate(query);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			f = false;
		}
		return f;
	}
	
}
