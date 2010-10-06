/* $Id$ */

package djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlTools;

import djudge.judge.checker.CheckerResult;
import djudge.judge.checker.CheckerResultEnum;
import djudge.judge.executor.ExecutionResult;
import djudge.judge.executor.ExecutionResultEnum;
import djudge.judge.executor.RunnerResult;

public class TestResult extends AbstractResult
{
	public final static String XMLRootElement = "test";
	
	private ExecutionResult runResult;
	
	private CheckerResult validationResult;
	
	private int testNumber;
	final String testNumberAttributeName = "num";
	
	private int testScore;
	
	//String systemMessage;
	
	{
		testNumber = 0;
	}

	public TestResult(TestDescription testDescription)
	{
		testNumber = testDescription.testNumber;
		testScore = testDescription.getScore();
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
			resultDetails = "Run: " + runResult.getResultDetails();
		}
		else if (runResult == null)
		{
			result = TestResultEnum.Undefined;
			resultDetails = "Run: null";
		}
		else if (validationResult != null && validationResult.getResult() != CheckerResultEnum.OK)
		{
			result = TestResultEnumFactory.getResult(validationResult.getResult()); 
			resultDetails = "Validation: " + validationResult.getResultDetails();
		}
		else if (validationResult == null)
		{
			result = TestResultEnum.Undefined;
			resultDetails = "Validation: null";
		}
		else
		{
			result = TestResultEnum.AC;
			resultDetails = "OK: ok";
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

	public void setValidationInfo(CheckerResult validationResult)
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
	
	public CheckerResult getCheckInfo()
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
		Document doc = XmlTools.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(testNumberAttributeName, "" + testNumber);
		res.setAttribute(maxMemoryAttributeName, "" + maxMemory);
		res.setAttribute(maxTimeAttributeName, "" + maxTime);
		res.setAttribute(resultAttributeName, "" + result);
		res.setAttribute(scoreAttributeName, "" + score);
		res.setAttribute(resultDetailsAttributeName, resultDetails);
		
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
		resultDetails = elem.getAttribute(resultDetailsAttributeName);
		
		NodeList runs = elem.getElementsByTagName(RunnerResult.XMLRootElement);
        if (runs.getLength() > 0)
        {
        	runResult = new ExecutionResult((Element)runs.item(0));
        }
        else
        {
        	runResult = new ExecutionResult();
        }
		
		NodeList vals = elem.getElementsByTagName(CheckerResult.XMLRootElement);
        if (vals.getLength() > 0)
        {
        	validationResult = new CheckerResult((Element)vals.item(0));
        }
        else
        {
        	validationResult = new CheckerResult("@STR");
        }
		
		return true;
	}
}
