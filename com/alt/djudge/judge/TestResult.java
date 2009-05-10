package com.alt.djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alt.djudge.judge.runner.RunnerResult;
import com.alt.djudge.judge.runner.RunnerResultEnum;
import com.alt.djudge.judge.validator.ValidationResult;
import com.alt.djudge.judge.validator.ValidationResultEnum;
import com.alt.utils.StringWorks;
import com.alt.utils.XmlWorks;




public class TestResult extends AbstractResult
{
	public final static String XMLRootElement = "test";
	
	private RunnerResult runResult;
	private ValidationResult validationResult;
	
	int testNumber;
	final String testNumberAttributeName = "num";
	
	int testScore;
	
	String systemMessage;
	
	{
		testNumber = 0;
	}

	public TestResult(TestDescription testDescription)
	{
		testNumber = testDescription.testNumber;
		testScore = testDescription.score;
	}

	public TestResult(Element elem)
	{
		readXML(elem);
	}
	
	private void updateResult()
	{
		score = 0;
		
		if (runResult != null && runResult.state != RunnerResultEnum.OK)
		{
			result = TestResultEnumFactory.getResult(runResult.state);
		}
		else if (runResult == null)
		{
			result = TestResultEnum.Undefined;
		}
		else if (validationResult != null && validationResult.Result != ValidationResultEnum.OK)
		{
			result = TestResultEnumFactory.getResult(validationResult.Result); 
		}
		else if (validationResult == null)
		{
			result = TestResultEnum.Undefined;
		}
		else
		{
			result = TestResultEnum.AC;
		}
		
		maxMemory = runResult.memory;
		maxTime = runResult.time;
		if (result == TestResultEnum.AC)
		{
			score = testScore;
		}
	}

	public void setRuntimeInfo(RunnerResult runResult)
	{
		this.runResult = runResult;
		updateResult();
	}

	public void setValidationInfo(ValidationResult validationResult)
	{
		this.validationResult = validationResult;
		updateResult();
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
		res.setAttribute(maxMemoryAttributeName, "" + maxMemory);
		res.setAttribute(maxTimeAttributeName, "" + maxTime);
		res.setAttribute(resultAttributeName, "" + result);
		res.setAttribute(scoreAttributeName, "" + score);
		
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
		testNumber = StringWorks.parseInt(elem.getAttribute(testNumberAttributeName), -1);
		
		NodeList runs = elem.getElementsByTagName(RunnerResult.XMLRootElement);
        if (runs.getLength() > 0)
        	runResult = new RunnerResult((Element)runs.item(0));
		
		NodeList vals = elem.getElementsByTagName(ValidationResult.XMLRootElement);
        if (vals.getLength() > 0)
        	validationResult = new ValidationResult((Element)vals.item(0));		
		
		return true;
	}
}