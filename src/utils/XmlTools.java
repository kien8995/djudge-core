/* $Id$ */

package utils;

import java.io.*;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import javax.xml.parsers.*;

public class XmlTools
{
	private static final Logger log = Logger.getLogger(XmlTools.class);

	public static Document getDocument(String filename)
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.parse(filename);
		}
		catch (Exception exc)
		{
			log.error("XmlWorks::getDocument", exc);
		}
		return doc;

	}

	public static Document getDocumentFromString(String xmlString)
	{
		Document doc = null;
		try
		{
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new java.io.StringReader(xmlString)));
			doc = parser.getDocument();
		}
		catch (Exception exc)
		{
			log.error("XmlWorks::getDocument(String)", exc);
		}
		return doc;
	}

	public static Document getDocumentE(String filename) throws Exception
	{
		File f = new File(filename);
		if (!f.exists())
		{
			throw new FileNotFoundException(filename);
		}
		Document doc = null;
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		doc = docBuilder.parse(filename);
		return doc;
	}

	public static Document getDocument()
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
		}
		catch (Exception exc)
		{
			log.error("XmlWorks::getDocument()", exc);
		}
		return doc;
	}

	public static String formatDoc(Document document)
	{
		try
		{
			OutputFormat format = new OutputFormat(document);
			format.setLineWidth(65);
			format.setIndenting(true);
			format.setIndent(2);
			Writer out = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(document);
			System.out.println(out.toString());
			return out.toString();
		}
		catch (IOException e)
		{
			log.error("XmlWorks::formatDoc()", e);
			throw new RuntimeException(e);
		}
	}

	public static void saveXmlToFile(Document doc, String file)
	{
		FileTools.saveToFile(formatDoc(doc), file);
	}
}
