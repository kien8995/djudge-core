package djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.StringWorks;
import utils.XmlWorks;


import djudge.judge.dexecutor.ExecutionResult;
import djudge.judge.dexecutor.ExecutionResultEnum;
import djudge.judge.executor.RunnerResult;
import djudge.judge.validator.ValidationResult;
import djudge.judge.validator.ValidationResultEnum;

public class TestResult extends AbstractResult
{
	public final static String XMLRootElement = "test";
	
	private ExecutionResult runResult;
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
		
		if (runResult != null && runResult.result != ExecutionResultEnum.OK)
		{
			result = TestResultEnumFactory.getResult(runResult.result);
		}
		else if (runResult == null)
		{
			result = TestResultEnum.Undefined;
		}
		else if (validationResult != null && validationResult.result != ValidationResultEnum.OK)
		{
			result = TestResultEnumFactory.getResult(validationResult.result); 
		}
		else if (validationResult == null)
		{
			result = TestResultEnum.Undefined;
		}
		else
		{
			result = TestResultEnum.AC;
		}
		
		maxMemory = runResult.memoryConsumed;
		maxTime = runResult.timeConsumed;
		if (result == TestResultEnum.AC)
		{
			score = testScore;
		}
	}

	public void setRuntimeInfo(ExecutionResult runResult)
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
	
	public ExecutionResult getRuntimeInfo()
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
		testNumber = Integer.parseInt(elem.getAttribute(testNumberAttributeName));
		maxMemory = Long.parseLong(elem.getAttribute(maxMemoryAttributeName));
		maxTime = Long.parseLong(elem.getAttribute(maxTimeAttributeName));
		result = TestResultEnum.valueOf(elem.getAttribute(resultAttributeName));
		score = Integer.parseInt(elem.getAttribute(scoreAttributeName));
		
		NodeList runs = elem.getElementsByTagName(RunnerResult.XMLRootElement);
        if (runs.getLength() > 0)
        {
        	runResult = new ExecutionResult((Element)runs.item(0));
        }
        else
        {
        	runResult = new ExecutionResult();
        }
		
		NodeList vals = elem.getElementsByTagName(ValidationResult.XMLRootElement);
        if (vals.getLength() > 0)
        {
        	validationResult = new ValidationResult((Element)vals.item(0));
        }
        else
        {
        	validationResult = new ValidationResult("@STR");
        }
		
		return true;
	}
}
