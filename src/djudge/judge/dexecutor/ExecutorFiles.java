/* $Id$ */

package djudge.judge.dexecutor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import djudge.common.XMLSerializable;


import utils.XmlWorks;

public class ExecutorFiles extends XMLSerializable
{
	public final static String XMLRootElement = "ExecutorFiles";
	
	public String inputFilename;
	private final String inputFilenameAttributeName = "input-filename"; 

	public String outputFilename;
	private final String outputFilenameAttributeName = "output-filename"; 
	
	public String errorFilename;
	private final String errorFilenameAttributeName = "error-filename"; 
	
	{
		inputFilename = outputFilename = errorFilename = "";
	}
	
	public ExecutorFiles(Element elem)
	{
		readXML(elem);
	}
	
	public ExecutorFiles()
	{
		// nothing
	}
	
	public ExecutorFiles(String in, String out, String err)
	{
		inputFilename = in;
		outputFilename = out;
		errorFilename = err;
	}

	public ExecutorFiles(String in, String out)
	{
		inputFilename = in;
		outputFilename = out;
	}
	
	public ExecutorFiles(String out)
	{
		outputFilename = out;
	}
	
	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(inputFilenameAttributeName, inputFilename);
		res.setAttribute(outputFilenameAttributeName, outputFilename);
		res.setAttribute(errorFilenameAttributeName, errorFilename);
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		inputFilename = elem.getAttribute(inputFilenameAttributeName);
		outputFilename = elem.getAttribute(outputFilenameAttributeName);
		errorFilename = elem.getAttribute(errorFilenameAttributeName);
		
		return true;
	}
}
