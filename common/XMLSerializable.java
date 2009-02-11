package common;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlWorks;

/**
 * @author alt
 */
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
	
	public String getXMLString()
	{
		String res = "";
		try
		{
	        //set up a transformer
	        TransformerFactory transfac = TransformerFactory.newInstance();
	        Transformer trans = transfac.newTransformer();
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	
	        //create string from xml tree
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(this.getXML());
	        trans.transform(source, result);
	        String xmlString = sw.toString();
	        res = xmlString;
		}
		catch (TransformerConfigurationException exc)
		{
			// FIXME
			System.out.println("!!![XMLSerializable.saveXmlToFile]: " + exc);
			exc.printStackTrace();
		}
		catch (TransformerException exc)
		{
			// FIXME
			System.out.println("!!![XMLSerializable.saveXmlToFile]: " + exc);
			exc.printStackTrace();
		}
		return res;
	}
}
