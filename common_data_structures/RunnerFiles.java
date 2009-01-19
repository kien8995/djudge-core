package common_data_structures;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlWorks;

import common.XMLSerializable;

public class RunnerFiles extends XMLSerializable
{
	public final static String XMLRootElement = "RunnerFiles";
	
	public String inputFilename;
	private final String inputFilenameAttributeName = "input-filename"; 

	public String outputFilename;
	private final String outputFilenameAttributeName = "output-filename"; 
	
	public String errorFilename;
	private final String errorFilenameAttributeName = "error-filename"; 
	
	{
		inputFilename = outputFilename = errorFilename = "";
	}
	
	public RunnerFiles(Element elem)
	{
		readXML(elem);
	}
	
	public RunnerFiles()
	{
		// nothing
	}
	
	public RunnerFiles(String in, String out, String err)
	{
		inputFilename = in;
		outputFilename = out;
		errorFilename = err;
	}

	public RunnerFiles(String in, String out)
	{
		inputFilename = in;
		outputFilename = out;
	}
	
	public RunnerFiles(String out)
	{
		outputFilename = out;
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
