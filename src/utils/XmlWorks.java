/* $Id: XmlWorks.java, v 0.1 2008/07/23 05:13:08 alt Exp $ */

/* Copyright (C) 2008 Olexiy Palinkash <olexiy.palinkash@gmail.com> */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */ 

package utils;

import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import javax.xml.parsers.*;

public class XmlWorks 
{

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
			System.out.println("Exception in XmlWorks::getDocument: " + exc);
		}
        return doc;
        
	}

	public static Document getDocumentFromString(String xmlString)
	{
		Document doc = null;
		//System.out.println(xmlString);
		try
		{
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new java.io.StringReader(xmlString)));
			doc = parser.getDocument();
		}
		catch (Exception exc)
		{
			System.out.println("Exception in XmlWorks::getDocument: " + exc);
		}
		//System.out.println(doc);
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
			DocumentBuilder docBuilder= dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
		}
		catch (Exception exc)
		{
			System.out.println("!!![XmlWorks.Init]: " + exc);
		}
		return doc;
	}
	
	public static String formatDoc(Document document)
	{
	        try {
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
	            throw new RuntimeException(e);
	        }
	}
	
	public static void saveXmlToFile(Document doc, String file)
	{
        FileWorks.saveToFile(formatDoc(doc), file);
	}
	
//	public static void saveXmlElementToFile(Element elem, String file)
	//{
/*		try
		{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
	        doc.appendChild(elem.cloneNode(true));
	        saveXmlToFile((Document)elem, file);
		}
		catch (Exception exc)
		{
			// FIXME
			System.out.println("!!![XmlWorks.saveXmlElementToFile]: " + exc);
		}
		*/
	//}
}
