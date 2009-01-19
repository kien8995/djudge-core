package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import common.XMLSerializable;

import runner.RunnerResult;
import utils.XmlWorks;
import validator.ValidationResult;


public class TestResult extends XMLSerializable
{
	RunnerResult runResult;
	ValidationResult validationResult;
	int testNumber;
	final String testNumberAttributeName = "test-num";
	
	{
		testNumber = 0;
	}
	
	public TestResult()
	{
		// nothing
	}

	public TestResult(int testNum)
	{
		testNumber = testNum;
	}

	public void setRuntimeInfo(RunnerResult runResult)
	{
		this.runResult = runResult;
	}

	public void setValidationInfo(ValidationResult validationResult)
	{
		this.validationResult = validationResult;
	}
	
	public void setTestNumber(int testNum)
	{
		this.testNumber = testNum;
	}
	
	public int getTestNumber()
	{
		return testNumber;
	}
	
	public ValidationResult getValidationInfo()
	{
		return validationResult;
	}
	
	public RunnerResult getRuntimeInfo()
	{
		return runResult;
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(testNumberAttributeName, "" + testNumber);
		
		if (runResult != null)
			res.appendChild(doc.importNode(runResult.getXML().getFirstChild(), true));
		
		if (validationResult != null)
			res.appendChild(doc.importNode(validationResult.getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		try
		{
			testNumber = Integer.parseInt(elem.getAttribute(testNumberAttributeName));
		}
		catch (Exception ex)
		{
			testNumber = 0;
		}
		
		NodeList runs = elem.getElementsByTagName(RunnerResult.XMLRootElement);
        if (runs.getLength() > 0)
        	runResult = new RunnerResult((Element)runs.item(0));
		
		NodeList vals = elem.getElementsByTagName(ValidationResult.XMLRootElement);
        if (vals.getLength() > 0)
        	validationResult = new ValidationResult((Element)vals.item(0));		
		
		return true;
	}
}
