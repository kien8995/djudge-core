package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GroupDescription extends AbstractDescription
{
	int groupNumber;
	
	public GroupDescription(int number, ProblemDescription problem)
	{
		groupNumber = number;
		problemInfo = problem.problemInfo.clone();
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
