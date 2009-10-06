package db;

public class SQLAbstract
{
	protected final static void setInsert(StringBuffer s, String table)
	{
		s.append("INSERT INTO `" + table + "`");
	}
	
	protected final static void setUpdate(StringBuffer s, String table)
	{
		s.append("UPDATE `" + table + "`");
	}

	protected final static void setValues(StringBuffer s, String[] columns, Object[] values)
	{
		s.append("SET ");
		boolean f = false;
		for (int i = 0; i < columns.length; i++)
		{
			String val = null;
			if (values[i] == null) continue;
			if (values[i] instanceof Integer)
			{
				val = values[i].toString();
			}
			else
			{
				try
				{
					val = "'" + values[i].toString() + "'";
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			if (f) s.append(",");
			f = true;
			s.append(" "  + columns[i] + " = " + val);
		}
	}
	
	protected final static void setValues2(StringBuffer s, String[] columns, Object[] values)
	{
		s.append("(");
		boolean f = false;
		for (int i = 0; i < columns.length; i++)
		{
			if (values[i] == null) continue;
			if (f) s.append(",");
			f = true;
			s.append("`" + columns[i] + "`");
		}
		s.append(") VALUES (");
		f = false;
		for (int i = 0; i < columns.length; i++)
		{
			String val = null;
			if (values[i] == null) continue;
			if (values[i] instanceof Integer)
			{
				val = values[i].toString();
			}
			else
			{
				try
				{
					val = "'" + escape(values[i].toString()) + "'";
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			if (f) s.append(",");
			f = true;
			s.append(val);
		}
		s.append(")");
	}
	
	protected final static void setWhere(StringBuffer s, String[] columns, String[] values)
	{
		s.append(" WHERE ");
		for (int i = 0; i < columns.length; i++)
		{
			if (i > 0) s.append(" AND ");
			s.append(" "  + columns[i] + " " + values[i]);
		}
	}
	
	public static String escape(String str)
	{
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (c == '\'')
			{
				res.append("\\'");
			}
			else if (c == '\\')
			{
				res.append("\\\\");
			}
			else
			{
				res.append(c);
			}
		}
		return res.toString();
	}
}
