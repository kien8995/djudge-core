package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlWorks;

public class GroupResult extends AbstractResult
{
	public final static String XMLRootElement = "group-result";
	
	TestResult[] testResults;
	int groupNumber;
	
	int testsCount;
	final String groupNumberAttributeName = "group-num";

	public GroupResult(GroupDescription group)
	{
		groupNumber = group.groupNumber;
		testsCount = group.testsCount;
		testResults = new TestResult[testsCount];
		for (int i = 0; i < testsCount; i++)
			testResults[i] = new TestResult(group.tests[i]);
	}
	
	public void updateResult()
	{
		result = TestResultEnum.AC;
		score = 0;
		for (int i = 0; i < testsCount; i++)
		{
			score += testResults[i].score;
			if (testResults[i].result != TestResultEnum.AC && result == TestResultEnum.AC)
				result = testResults[i].result;
		}
	}

	public GroupResult(Element elem)
	{
		readXML(elem);
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(groupNumberAttributeName, "" + groupNumber);
		res.setAttribute(scoreAttributeName, "" + score);
		res.setAttribute(resultAttributeName, "" + result);
		
		for (int i = 0; i < testsCount; i++)
			res.appendChild(doc.importNode(testResults[i].getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		try
		{
			groupNumber = Integer.parseInt(elem.getAttribute(groupNumberAttributeName));
		}
		catch (Exception ex)
		{
			groupNumber = 0;
		}
		
		NodeList tests = elem.getElementsByTagName(TestResult.XMLRootElement);
		testsCount = tests.getLength();
		testResults = new TestResult[testsCount];
		
		for (int i = 0; i < testsCount; i++)
		{
        	testResults[i] = new TestResult((Element)tests.item(i));
		}
		
		return true;
	}
}
