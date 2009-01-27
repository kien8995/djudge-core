package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlWorks;

public class ProblemResult extends AbstractResult
{
	public final static String XMLRootElement = "problem";

	GroupResult[] groupResults;
	int groupCount;

	public ProblemResult(ProblemDescription problem)
	{
		groupCount = problem.groupsCount;
		groupResults = new GroupResult[groupCount];
		for (int i = 0; i < groupCount; i++)
			groupResults[i] = new GroupResult(problem.groups[i]);
	}

	public void updateResult()
	{
		result = TestResultEnum.AC;
		score = 0;
		for (int i = 0; i < groupCount; i++)
		{
			score += groupResults[i].score;
			if (groupResults[i].result != TestResultEnum.AC && result == TestResultEnum.AC)
				result = groupResults[i].result;
		}
	}

	

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(scoreAttributeName, "" + score);
		res.setAttribute(resultAttributeName, "" + result);
		
		for (int i = 0; i < groupCount; i++)
			res.appendChild(doc.importNode(groupResults[i].getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		NodeList groups = elem.getElementsByTagName(GroupResult.XMLRootElement);
		groupCount = groups.getLength();
		groupResults = new GroupResult[groupCount];
		
		for (int i = 0; i < groupCount; i++)
		{
        	groupResults[i] = new GroupResult((Element)groups.item(i));
		}
		
		return true;
	}
}