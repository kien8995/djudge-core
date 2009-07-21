package djudge.acmcontester.structures;

import java.sql.ResultSet;

import djudge.acmcontester.Admin;
import djudge.acmcontester.models.AbstractDBModel;

public class UserDescription
{
	public final static String[] names = {
		"id",
		"surname",
		"name",
		"password",
		"role",
	};

	public String id;
	public String surname;
	public String name;
	public String password;
	public String username;
	public String role;
	
	public UserDescription()
	{
		
	}
	
	public UserDescription(ResultSet rs)
	{
		fill(rs);
	}
	
	private void fill(ResultSet rs)
	{
		try
		{
        	id = rs.getString("id");
        	username = rs.getString("username");
        	password = rs.getString("password");
        	name = rs.getString("name");
        	surname = rs.getString("surname");
        	role = rs.getString("role");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getUpdateStatement()
	{
		return "UPDATE `users` SET " + 
			"`username` = '" + AbstractDBModel.escape(username)  + "', " +
			"`password` = '" + AbstractDBModel.escape(password) + "', " + 
			"`name` = '" + AbstractDBModel.escape(name) + "', " +
			"`surname` = '" + AbstractDBModel.escape(surname) + "', " +
			"`role` = '" + AbstractDBModel.escape(role) + "' " +
			"WHERE `id` = '" + AbstractDBModel.escape(id) + "'";
			
	}
	
	public String getColumnValue(int columnIndex)
	{
		switch (columnIndex)
		{
		case 0: return id;
		case 1: return username;
		case 2: return name;
		case 3: return surname;
		case 4: return role;
		}
		return "";
	}
	
	public void setColumnValue(int columnIndex, String newValue)
	{
		switch (columnIndex)
		{
		case 1:
			username = newValue;
			break;
			
		case 2:
			name = newValue;
			break;
			
		case 3: 
			surname = newValue;
			break;
			
		case 4:
			role = newValue;
			break;
		}
	}

	public static void main(String[] args)
	{
		new Admin();
	}
	
}
