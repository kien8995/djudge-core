package com.alt.djudge.judge.dexecutor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.common.XMLSerializable;
import com.alt.utils.XmlWorks;




public class ExecutorSecurityLimits extends XMLSerializable
{
	public ExecutorSecurityLimits(Element elem)
	{
		readXML(elem);
	}

	public ExecutorSecurityLimits()
	{
		
	}

	@Override
	public Document getXML()
	{
		// TODO Auto-generated method stub
		return XmlWorks.getDocument();
	}

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return true;
	}
}
