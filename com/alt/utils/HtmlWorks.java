/* $Id: HtmlWorks.java, v 0.1 2008/07/28 05:13:08 alt Exp $ */

/* Copyright (C) 2008 Olexiy Palinkash <olexiy.palinkash@gmail.com> */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */ 

package com.alt.utils;

import com.alt.djudge.judge.GroupResult;
import com.alt.djudge.judge.ProblemResult;
import com.alt.djudge.judge.SubmissionResult;
import com.alt.djudge.judge.TestResult;
import com.alt.djudge.judge.TestResultEnum;
import com.alt.djudge.judge.dexecutor.ExecutionResult;
import com.alt.djudge.judge.validator.ValidationResult;


public class HtmlWorks 
{
	private static String toSafeHtml(String s)
	{
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < s.length(); i++)
			switch (s.charAt(i))
			{
			case '&':
				res.append("&amp;");
				break;
			case '<':
				res.append("&lt;");
				break;
			case '>':
				res.append("&gt;");
				break;
			case '"':
				res.append("&quot;");
				break;
			case '\n':
				res.append("<br>");
				break;
			default:
				res.append(s.charAt(i));
			}
		return res.toString();
	}
	
	private static String getHeaderColor()
	{
		// Grey color
		return "Gainsboro";
	}
	
	private static String formatMemorySize(long size)
	{
		if (size < 0)
			return "n/a";
		if (size < 1000)
			return "" + size + "";
		if (size < 1000 * 1000)
			return "" + size/1000 +  " " + size%1000 + "";
		return "" + size/1000000 +  " " + ((size/1000)%1000) + " " + size%1000 + "";
	}
	
	private static String getJudgementColor(TestResultEnum j)
	{
		String res = "";
		switch (j)
		{
		case AC:
			res = "#aaffaa";
			break;

		default:
			res = "#ffaaaa";
			break;
		}
		return res;
	}
	
	private static String formatRuntime(long time)
	{
		if (time < 0)
			return "n/a";
		if (time <= 1000)
			return "" + time + " ms";
		//if (time <= 1000 * 60)
		return "" + time/1000 + "s " +  (time % 1000) + " ms";		
	}
	
	public static String testToHtml(TestResult res)
	{
		TestResultEnum judgement = res.getResult();
		ExecutionResult RunInfo = res.getRuntimeInfo();
		int TestNum = res.getTestNumber();
		ValidationResult ValidationInfo = res.getValidationInfo(); 
		
		StringBuffer s = new StringBuffer();
		String color = getJudgementColor(judgement);
		s.append("<tr bgcolor=" + color + ">");
			s.append("<td>" + (TestNum + 1)  + "</td>");
			s.append("<td>" + formatRuntime(RunInfo.timeConsumed)  + "</td>");
			s.append("<td>" + formatMemorySize(RunInfo.memoryConsumed)  + "</td>");
			s.append("<td>" + formatMemorySize(RunInfo.outputGenerated)  + "</td>");
			s.append("<td>" + judgement  + "</td>");
			try
			{
				s.append("<td>" + toSafeHtml(StringWorks.ArrayToString(ValidationInfo.ValidatorOutput))  + "</td>");
			}
			catch (Exception e)
			{
				s.append("<td></td>");
			}
		s.append("</tr>\n");
		return s.toString();		
	}
	
	public static String testGroupToHtml(GroupResult res)
	{
		StringBuffer s = new StringBuffer();
		s.append("<table border=1>");
		s.append("<tr><td>Group#</td><td>" + (res.getGroupNumber() + 1) + "</td></tr>");
		s.append("<tr><td>Status</td><td>" + res.getJudgement() + "</td></tr>");
		s.append("<tr><td>MaxTime</td><td>" + formatRuntime(res.getMaxTime()) + " ms</td></tr>");
		s.append("<tr><td>MaxMemory</td><td>" + formatMemorySize(res.getMaxMemory()) + " K</td></tr>");
		s.append("<table>");
		s.append("<table border=1>");
		s.append("<tr><th>Test</th><th>Time</th><th>Memory</th><th>OutputSize</th><th>Judgement</th><th>Validator</th></tr>");
		try
		{
    		for (int i = 0; i < res.getTestsCount(); i++)
    			s.append(testToHtml(res.getTestInfo(i)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		s.append("</table>\n");
		return s.toString();
	}
	
	public static String problemToHtml(SubmissionResult res2)
	{
		StringBuffer s = new StringBuffer();
		// ?compiled
		s.append("<table border=1>");
		s.append("<caption><h3>Results of testing '" + res2.comment + "'</h3></caption>");		
		ProblemResult res = res2.getProblemResult();
		if (res.getResult() != TestResultEnum.CE)
		{
			for (int i = 0; i < res.getGroupsCount(); i++)
			{
				s.append("<tr><td>");
				s.append(testGroupToHtml(res.getGroupResult(i)));
				s.append("</td></tr>");
			}
		}
		else
		{
			s.append("<tr><td>Compilation Error</td></tr>");
			s.append("<tr><td>");
			String str[] = res2.getCompilationInfo().getCompilerOutput();
			for (int i = 0; i < str.length; i++)
				s.append(str[i] + "<br>");
			s.append("</tr></td>");
		}
		s.append("</table><br>");
		return s.toString();
	}
	
	public static String directoryResultToHtml(DirectoryResult res)
	{
		StringBuffer s = new StringBuffer();
		// Global statistics
		s.append("<table border=1>\n");
		s.append("<tr bgcolor=" + getHeaderColor() + ">");
			s.append("<th>#</th>");
			s.append("<th>File</th>");
			s.append("<th>Judgement</th>");
			s.append("<th>Score</th>");
			s.append("<th>MaxTime</th>");
			s.append("<th>MaxMemory</th>");
			s.append("<th>TotalTime</th>");
		s.append("</tr>\n");		
		for (int i = 0; i < res.getFilesCount(); i++)
		{
			SubmissionResult t = res.getSubmissionResult(i);
			String color = getJudgementColor(t.getJudgement());
			s.append("<tr bgcolor=" + color +">");
				s.append("<td>" + (i+1) + "</td>");
				s.append("<td><a href=#" + (i+1) + ">" + t.comment + "</a></td>");
				s.append("<td>" + t.getJudgement() + "</td>");
				s.append("<td>" + t.getScore() + "</td>");
				s.append("<td>" + formatRuntime(t.getMaxTime()) + "</td>");
				s.append("<td>" + formatMemorySize(t.getMaxMemory()) + "</td>");
				//s.append("<td>" + formatRuntime(t.getTotalTime()) + "</td>");
			s.append("</tr>\n");
		}
		s.append("</table>\n");
		// By each solution
		for (int i = 0; i < res.getFilesCount(); i++)
		{
			s.append("<a name=" + (i+1) +"></a>");			
			SubmissionResult t = res.getSubmissionResult(i);
			s.append(problemToHtml(t));
		}
		return s.toString();		
	}
	
	
}
