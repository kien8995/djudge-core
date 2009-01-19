package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GroupResult extends AbstractResult
{
	TestResult[] testResults;
	GroupDescription group;
	int groupNumber;
	int testsCount;

	public GroupResult(GroupDescription group)
	{
		this.group = group;
		groupNumber = group.groupNumber;
		testsCount = group.testsCount;
		testResults = new TestResult[testsCount];
		for (int i = 0; i < testsCount; i++)
			testResults[i] = new TestResult(group.tests[i]);
	}

	@Override
	public Document getXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
