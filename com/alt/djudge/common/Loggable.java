/* $Id: Loggable.java, v 1.0 2008/11/14 13:24:00 alt Exp $ */

package com.alt.djudge.common;

public class Loggable
{
	protected void log(String s)
	{
		System.out.println(s);
	}
	
	protected void logDebug(String s)
	{
		System.out.println("Debug: " + s);
	}
	
	protected void logException(String s)
	{
		System.out.println("Exception:" + s);
	}
	
	protected void logException(Exception ex)
	{
		logException(ex.toString());
	}
}