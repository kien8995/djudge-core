/* $Id: XMLSerializable.java, v 1.0 2008/11/14 13:24:00 alt Exp $ */

package common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlWorks;

public abstract class XMLSerializable extends Loggable
{
	public final static String XMLRootElement = "change-me-in-child-class";
	
	public abstract Document getXML();
	
	public abstract boolean readXML(Element elem);
	
	public boolean loadXML(String filename)
	{
		boolean result = false;
		try
		{
			result = readXML((Element)XmlWorks.getDocument(filename).getDocumentElement());
		}
		catch (Exception ex)
		{
			logException(ex.toString());
		}
		return result;
	}
	
	public boolean saveXML(String filename)
	{
		boolean result = false;
		try
		{
			XmlWorks.saveXmlToFile(getXML(), filename);
		}
		catch (Exception ex)
		{
			logException(ex.toString());
		}
		return result;
	}
}
