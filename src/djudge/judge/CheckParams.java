/* $Id$ */

package djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlTools;

import djudge.acmcontester.structures.SubmissionData;
import djudge.judge.dexecutor.ExecutorLimits;

public class CheckParams// extends XMLSerializable
{
	public final static String XMLRootElement = "params";
	
	public boolean fFirstTestOnly;
	private static String fFirstTestOnlyAttibuteName = "first-test-only";
	
	public ExecutorLimits limits;
	
	public String inputFilename = "";
	private static String inputFilenameAttibuteName = "input-file";
	
	public String outputFilename = "";
	private static String outputFilenameAttibuteName = "output-file";

	public CheckParams()
	{
		fFirstTestOnly = false;
	}
	
	public CheckParams(Element documentFromString)
	{
		readXML(documentFromString);
	}

	public CheckParams(String str)
	{
		fFirstTestOnly = Boolean.parseBoolean(str);
	}

	public CheckParams(SubmissionData sd)
	{
		fFirstTestOnly = sd.fFirstTestOnly > 0;
	}

	//@Override
	public Document getXML()
	{
		Document doc = XmlTools.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(fFirstTestOnlyAttibuteName, "" + fFirstTestOnly);
		res.setAttribute(inputFilenameAttibuteName, inputFilename);
		res.setAttribute(outputFilenameAttibuteName, outputFilename);
		
		if (null != limits)
		{
			res.appendChild(doc.importNode(limits.getXML().getFirstChild(), true));
		}
		
		doc.appendChild(res);
		return doc;
	}

	//@Override
	public boolean readXML(Element elem)
	{
		fFirstTestOnly = Boolean.parseBoolean(elem.getAttribute(fFirstTestOnlyAttibuteName));
		inputFilename = elem.getAttribute(inputFilenameAttibuteName);
		outputFilename = elem.getAttribute(outputFilenameAttibuteName);
		
		NodeList list = elem.getElementsByTagName(ExecutorLimits.XMLRootElement);
        if (list.getLength() > 0)
        {
        	for (int i = 0; i < list.getLength(); i++)
        	{
            	if (list.item(i).getParentNode().equals(elem))
            	{
            		limits = new ExecutorLimits((Element) list.item(i));
            	}
        	}
        }
        
        
		return true;
	}
	
	public boolean isNormalSubmission()
	{
		return !fFirstTestOnly;
	}
	
	public void setLimits(ExecutorLimits limits)
	{
		this.limits = limits;
	}
	
	public void setFlagFirstTestOnly(boolean flag)
	{
		fFirstTestOnly = flag;
	}
}
