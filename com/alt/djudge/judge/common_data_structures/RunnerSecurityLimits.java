package com.alt.djudge.judge.common_data_structures;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.common.XMLSerializable;
import com.alt.utils.XmlWorks;




public class RunnerSecurityLimits extends XMLSerializable
{
	public RunnerSecurityLimits(Element elem)
	{
		readXML(elem);
	}

	public RunnerSecurityLimits()
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
