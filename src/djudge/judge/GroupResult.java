/* $Id$ */

package djudge.judge;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import utils.StringWorks;
import utils.XmlWorks;

public class GroupResult extends AbstractResult
{
	public final static String XMLRootElement = "group";
	
	TestResult[] testResults;
	int groupNumber;
	int groupScore = 0;
	
	int testsCount;
	final String groupNumberAttributeName = "num";

	public GroupResult(GroupDescription group)
	{
		groupNumber = group.groupNumber;
		testsCount = group.getTestCount();
		testResults = new TestResult[testsCount];
		groupScore = group.getScore();
		for (int i = 0; i < testsCount; i++)
			testResults[i] = new TestResult(group.tests.get(i));
	}
	
	public void updateResult()
	{
		result = TestResultEnum.AC;
		score = 0;
		maxMemory = maxTime = wrongTest = -1;
		for (int i = 0; i < testsCount; i++)
		{
			score += testResults[i].score;
			if (testResults[i].result != TestResultEnum.AC && result == TestResultEnum.AC)
			{
				result = testResults[i].result;
				wrongTest = i;
			}
			maxTime = Math.max(maxTime, testResults[i].maxTime);
			maxMemory = Math.max(maxMemory, testResults[i].maxMemory);
		}
		
		if (result == TestResultEnum.AC)
			score += groupScore;
			
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
		res.setAttribute(maxMemoryAttributeName, "" + maxMemory);
		res.setAttribute(maxTimeAttributeName, "" + maxTime);
		res.setAttribute(scoreAttributeName, "" + score);
		res.setAttribute(resultAttributeName, "" + result);
		res.setAttribute(wrongTestAttributeName, "" + wrongTest);
		
		for (int i = 0; i < testsCount; i++)
			res.appendChild(doc.importNode(testResults[i].getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		groupNumber = StringWorks.parseInt(elem.getAttribute(groupNumberAttributeName), 0);
		score = StringWorks.parseInt(elem.getAttribute(scoreAttributeName), 0);

		NodeList tests = elem.getElementsByTagName(TestResult.XMLRootElement);
		testsCount = tests.getLength();
		testResults = new TestResult[testsCount];
		
		for (int i = 0; i < testsCount; i++)
		{
        	testResults[i] = new TestResult((Element)tests.item(i));
		}
		
		return true;
	}

	public int getGroupNumber()
	{
		return groupNumber;
	}

	public int getTestsCount()
	{
		return testsCount;
	}

	public TestResult getTestInfo(int i)
	{
		return testResults[i];
	}
}
