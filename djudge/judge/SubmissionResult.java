package djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlWorks;

import djudge.acmcontester.Admin;
import djudge.judge.dcompiler.CompilationInfo;
import djudge.judge.dcompiler.CompilerResult;

public class SubmissionResult extends AbstractResult
{
	public final static String XMLRootElement = "submission";
	
	private ProblemResult problemResult;
	
	private CompilerResult compilationInfo;
	
	public String comment;
	
	public ProblemResult getProblemResult()
	{
		return problemResult;
	}
	
	private void updateResult()
	{
		if (!compilationInfo.isSuccessfull())
		{
			result = TestResultEnum.CE;
			score = 0;
			wrongTest = -1;
		}
		else
		{
			result = problemResult.result;
			score = problemResult.score;
			maxMemory = problemResult.maxMemory;
			maxTime = problemResult.maxTime;
			wrongTest = problemResult.wrongTest;
		}
	}
	
	public void setProblemResult(ProblemResult pr)
	{
		problemResult = pr;
		updateResult();
	}
	
	public void setCompilationInfo(CompilerResult ci)
	{
		compilationInfo = ci;
		updateResult();
	}
	
	public SubmissionResult(ProblemDescription desc)
	{
		compilationInfo = new CompilerResult();
		problemResult = new ProblemResult(desc);
	}
	
	public SubmissionResult(Document doc)
	{
		//System.out.println(doc.getAttributes());
		this.readXML((Element) doc.getFirstChild());
	}
	
	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(scoreAttributeName, "" + score);
		res.setAttribute(resultAttributeName, "" + result);
		res.setAttribute(maxMemoryAttributeName, "" + maxMemory);
		res.setAttribute(maxTimeAttributeName, "" + maxTime);
		res.setAttribute(wrongTestAttributeName, "" + wrongTest);
		
		res.appendChild(doc.importNode(compilationInfo.getXML().getFirstChild(), true));
		res.appendChild(doc.importNode(problemResult.getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		//System.out.println(elem.getAttributes().toString());
		score = Integer.parseInt(elem.getAttribute(scoreAttributeName));
		maxMemory = Long.parseLong(elem.getAttribute(maxMemoryAttributeName));
		maxTime = Long.parseLong(elem.getAttribute(maxTimeAttributeName));
		wrongTest = Integer.parseInt(elem.getAttribute(wrongTestAttributeName));
		result = TestResultEnum.valueOf(elem.getAttribute(resultAttributeName));
		
		NodeList groups = elem.getElementsByTagName(CompilationInfo.XMLRootElement);
		if (groups.getLength() > 0)
		{
			compilationInfo = new CompilerResult((Element) groups.item(0));
		}
		else
		{
			compilationInfo = new CompilerResult();
		}
		
		groups = elem.getElementsByTagName(ProblemResult.XMLRootElement);
		if (groups.getLength() > 0)
		{
			problemResult = new ProblemResult((Element) groups.item(0));
		}
		else
		{
			problemResult = new ProblemResult();
		}		
		return false;
	}
	
	public CompilerResult getCompilationInfo()
	{
		return compilationInfo;
	}
	
	public TestResult getTestResult(int groupNumber, int testNumber)
	{
		return problemResult.groupResults[groupNumber].testResults[testNumber];
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
}
