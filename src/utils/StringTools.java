/* $Id$ */

package utils;

public class StringTools 
{
	public static int parseInt(String str, int defaultValue)
	{
		int res;
		try
		{
			res = Integer.parseInt(str);
		}
		catch (Exception ex)
		{
			res = defaultValue;
		}
		return res;
	}
	
	public static String ArrayToString(String[] Array)
	{
		String res = new String();
		for (int i = 0; i < Array.length; i++)
			res += Array[i] + "\n";
		return res;
	}
	
	/**
	 * Converts string denoting size of memory of form "<IntegerNumber>[<Prefix>]" to it's integer representation
	 * Possible suffices (case-insensitive):
	 * K, KB - Kilobytes
	 * M, MB - Megabytes
	 * G, GB - Gigabytes
	 * B - bytes 
	 * @param s String 
	 * @return Integer number - amount of bytes
	 */
	public static int StrToMemoryLimit(String s)
	{
		s = s.toUpperCase();
		if (s == "") s = "-1";
		int Val = 0;
		if (s.endsWith("GB"))
			Val = Integer.parseInt(s.substring(0, s.length() - 2)) * 1024 * 1024 * 1024;
		else if (s.endsWith("G"))
			Val = Integer.parseInt(s.substring(0, s.length() - 1)) * 1024 * 1024 * 1024;
		else if (s.endsWith("MB"))
			Val = Integer.parseInt(s.substring(0, s.length() - 2)) * 1024 * 1024;
		else if (s.endsWith("M"))
			Val = Integer.parseInt(s.substring(0, s.length() - 1)) * 1024 * 1024;
		else if (s.endsWith("KB"))
			Val = Integer.parseInt(s.substring(0, s.length() - 2)) * 1024;
		else if (s.endsWith("K"))
			Val = Integer.parseInt(s.substring(0, s.length() - 1)) * 1024;
		else if (s.endsWith("B"))
			Val = Integer.parseInt(s.substring(0, s.length() - 1));
		else
			Val = Integer.parseInt(s);
		return Val;
	}
	
	/**
	 * Converts string denoting time interval of form "<IntegerNumber>[<Prefix>]" to it's integer representation in milliseconds
	 * Possible suffices (case-insensitive):
	 * H - Hours
	 * M - Minutes
	 * S - Seconds
	 * MS - Milliseconds 
	 * @param s String 
	 * @return Integer number - amount of milliseconds 
	 */	
	public static int strToTimeLimit(String s)
	{
		s = s.toUpperCase();
		if (s == "") s = "-1";
		int Val = 0;
		if (s.endsWith("H"))
			Val = Integer.parseInt(s.substring(0, s.length() - 1)) * 60 * 60 * 1000;
		else if (s.endsWith("M"))
			Val = Integer.parseInt(s.substring(0, s.length() - 1)) * 60 * 1000;
		else if (s.endsWith("MS"))
			Val = Integer.parseInt(s.substring(0, s.length() - 2));
		else if (s.endsWith("S"))
			Val = Integer.parseInt(s.substring(0, s.length() - 1)) * 1000;
		else
			Val = Integer.parseInt(s);
		return Val;
	}
	
	/**
	 * Truncates string after first 100 symbols
	 * @param s String to truncate
	 * @return Truncated string
	 */
	public static String truncate(String s)
	{
		if (s == null)
			return "<null>";
		if (s == "")
			return "<Empty token>";		
		if (s.length() <= 20)
			return s;
		int len = s.length();
		return s.substring(0, 10) + "..." + s.substring(len - 10, len);
	}	
}
