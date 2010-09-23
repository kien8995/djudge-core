/* $Id$ */

package djudge.common;

import java.io.StringWriter;
import java.io.IOException;
import java.io.Writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import djudge.exceptions.DJudgeXmlException;

import utils.XmlWorks;


/**
 * @author alt
 */
public abstract class XMLSerializable extends Loggable
{
	public final static String XMLRootElement = "change-me-in-child-class";
	
	public abstract Document getXML();
	
	public abstract boolean readXML(Element elem) throws DJudgeXmlException;
	
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
	
	public String getXMLString()
	{
		String res = "";
        try {
        	Document doc = getXML();
            OutputFormat format = new OutputFormat(doc);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);            
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(doc);
            res = out.toString();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
		return res;
	}
}
