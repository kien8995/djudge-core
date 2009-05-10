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

package com.alt.utils;


import java.io.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

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
	
	public static void saveXmlToFile(Document doc, String file)
	{
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
	        DOMSource source = new DOMSource(doc);
	        trans.transform(source, result);
	        String xmlString = sw.toString();
	        
	        FileWorks.saveToFile(xmlString, file);
	        System.out.println(xmlString);
		}
		catch (TransformerConfigurationException exc)
		{
			// FIXME			
			System.out.println("!!![XmlWorks.saveXmlToFile]: " + exc);
		}
		catch (TransformerException exc)
		{
			// FIXME
			System.out.println("!!![XmlWorks.saveXmlToFile]: " + exc);
		}
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
