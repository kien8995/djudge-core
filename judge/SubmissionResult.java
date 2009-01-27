package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlWorks;

import compiler.CompilationInfo;

public class SubmissionResult extends AbstractResult
{
	public final static String XMLRootElement = "submission";
	
	private ProblemResult problemResult;
	
	private CompilationInfo compilationInfo;
	
	private void updateResult()
	{
		if (!compilationInfo.isSuccessfull())
		{
			result = TestResultEnum.CE;
			score = 0;
		}
		else
		{
			result = problemResult.result;
			score = problemResult.score;
		}
	}
	
	public void setProblemResult(ProblemResult pr)
	{
		problemResult = pr;
		updateResult();
	}
	
	public void setCompilationInfo(CompilationInfo ci)
	{
		compilationInfo = ci;
		updateResult();
	}
	
	public SubmissionResult(ProblemDescription desc)
	{
		compilationInfo = new CompilationInfo();
		problemResult = new ProblemResult(desc);
	}
	
	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(scoreAttributeName, "" + score);
		res.setAttribute(resultAttributeName, "" + result);
		
		res.appendChild(doc.importNode(compilationInfo.getXML().getFirstChild(), true));
		res.appendChild(doc.importNode(problemResult.getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return false;
	}

}