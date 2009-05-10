package com.alt.djudge.contestmanager;

import org.w3c.dom.Element;

public class LocalProblemDescription
{
	public String sid;
	public String name;
	public String judgeContest;
	public String judgeProblem;
	
	public LocalProblemDescription(Element item)
	{
		sid = item.getAttribute("id");
		name = item.getAttribute("name");
		judgeContest = item.getAttribute("judge-contest");
		judgeProblem = item.getAttribute("judge-problem");
	}
	
	//public LocalProblemDescription()
	{
		// TODO Auto-generated constructor stub
	}
}
