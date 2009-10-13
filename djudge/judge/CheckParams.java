package djudge.judge;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import utils.XmlWorks;

import djudge.acmcontester.structures.SubmissionData;

public class CheckParams// extends XMLSerializable
{
	public final static String XMLRootElement = "params";
	
	public boolean fFirstTestOnly;
	private static String fFirstTestOnlyAttibuteName = "first-test-only";

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
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(fFirstTestOnlyAttibuteName, "" + fFirstTestOnly);
		
		doc.appendChild(res);
		return doc;
	}

	//@Override
	public boolean readXML(Element elem)
	{
		fFirstTestOnly = Boolean.parseBoolean(elem.getAttribute(fFirstTestOnlyAttibuteName));
		return true;
	}
	
	public boolean isNormalSubmission()
	{
		return !fFirstTestOnly;
	}
}
