package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProblemResult extends AbstractResult
{
	GroupResult[] groupResults;
	ProblemDescription problem;
	int groupCount;

	public ProblemResult(ProblemDescription problem)
	{
		this.problem = problem;
		groupCount = problem.groupsCount;
		groupResults = new GroupResult[groupCount];
		for (int i = 0; i < groupCount; i++)
			groupResults[i] = new GroupResult(problem.groups[i]);
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
