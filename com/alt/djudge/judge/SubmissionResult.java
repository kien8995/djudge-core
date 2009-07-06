package com.alt.djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.judge.dcompiler.CompilerResult;
import com.alt.utils.XmlWorks;

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
		// TODO Auto-generated method stub
		return false;
	}
	
	public CompilerResult getCompilationInfo()
	{
		return compilationInfo;
	}
}
