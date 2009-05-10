package com.alt.djudge.contestmanager;

import org.w3c.dom.Element;

public class GlobalUserInfo
{
	String sid;
	String password;
	
	public GlobalUserInfo(Element item)
	{
		sid = item.getAttribute("id");
		password = item.getAttribute("password");
	}

}
