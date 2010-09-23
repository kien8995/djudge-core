/* $Id$ */

package djudge.judge.common_data_structures;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import djudge.common.XMLSerializable;


import utils.XmlWorks;

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
