/* $Id$ */

package djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlTools;

public class ProblemResult extends AbstractResult
{
	public final static String XMLRootElement = "problem";

	GroupResult[] groupResults;
	int groupCount;

	int problemScore = 0;
	
	public ProblemResult(ProblemDescription problem)
	{
		groupCount = problem.groupsCount;
		groupResults = new GroupResult[groupCount];
		problemScore = problem.getScore();
		for (int i = 0; i < groupCount; i++)
			groupResults[i] = new GroupResult(problem.groups[i]);
	}
	
	public ProblemResult()
	{
		
	}
	
	public ProblemResult(Element doc)
	{
		readXML(doc);
	}	

	public void updateResult()
	{
		result = TestResultEnum.AC;
		score = 0;
		for (int i = 0; i < groupCount; i++)
		{
			score += groupResults[i].score;
			if (groupResults[i].result != TestResultEnum.AC && result == TestResultEnum.AC)
			{
				result = groupResults[i].result;
				wrongTest = groupResults[i].wrongTest;
			}
			maxTime = Math.max(maxTime, groupResults[i].maxTime);
			maxMemory = Math.max(maxMemory, groupResults[i].maxMemory);			
		}
		if (TestResultEnum.AC.equals(result))
		{
			score += problemScore;
		}
	}

	

	@Override
	public Document getXML()
	{
		Document doc = XmlTools.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(scoreAttributeName, "" + score);
		res.setAttribute(resultAttributeName, "" + result);
		res.setAttribute(maxMemoryAttributeName, "" + maxMemory);
		res.setAttribute(maxTimeAttributeName, "" + maxTime);
		res.setAttribute(wrongTestAttributeName, "" + wrongTest);
		res.setAttribute(resultDetailsAttributeName, resultDetails);
		
		for (int i = 0; i < groupCount; i++)
		{
			res.appendChild(doc.importNode(groupResults[i].getXML().getFirstChild(), true));
		}
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		resultDetails = elem.getAttribute(resultDetailsAttributeName);
		
		NodeList groups = elem.getElementsByTagName(GroupResult.XMLRootElement);
		groupCount = groups.getLength();
		groupResults = new GroupResult[groupCount];
		
		for (int i = 0; i < groupCount; i++)
		{
        	groupResults[i] = new GroupResult((Element)groups.item(i));
		}
		
		return true;
	}

	public int getGroupsCount()
	{
		return groupCount;
	}

	public GroupResult getGroupResult(int i)
	{
		return groupResults[i];
	}

	public TestResultEnum getJudgement()
	{
		return result;
	}
	
	public TestResult getTestResult(int groupNumber, int testNumber)
	{
		return groupResults[groupNumber].testResults[testNumber];
	}

	public int getTestsCount()
	{
		int res = 0;
		for (int i = 0; i < getGroupsCount(); i++)
		{
			res += groupResults[i].getTestsCount();
		}
		return res;
	}
	
}
