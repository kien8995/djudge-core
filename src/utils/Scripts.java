/* $Id$ */

/* Copyright (C) 2008 Oleksiy Palinkash <oleksiy.palinkash@gmail.com> */

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

package utils;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;

import djudge.exceptions.DJudgeXmlCorruptedException;
import djudge.exceptions.DJudgeXmlNotFoundException;
import djudge.judge.ProblemDescription;
import djudge.judge.SubmissionResult;
import djudge.judge.common_data_structures.ExecutorLimits;


public class Scripts 
{
	private static final Logger log = Logger.getLogger(Scripts.class);
	
	// TODO Hadrcode
	public static final String problemsRoot = ".\\problems\\";

	public static void generateProblemReport(String contestId, String problemId)
	{
		generateProblemReport(contestId, problemId, new ExecutorLimits());
	}
	
	public static DirectoryResult generateProblemReport(String contestId, String problemId, ExecutorLimits limits)
	{
		try
		{
    		ProblemDescription desc = new ProblemDescription(contestId, problemId);
    		log.info("Generating report for problem " + contestId + "-" + problemId);
    		
    		JudgeDirectory j = new JudgeDirectory(desc);
    		
    		DirectoryResult res = j.judge(problemsRoot + contestId + "\\" + problemId +"\\solutions");
    		String html = "<h1>Problem " + problemId + " (" + contestId + ") [ " + Calendar.getInstance().getTime() +  "]</h1>"
    						+ HtmlWorks.directoryResultToHtml(res, desc);
    		FileWorks.saveToFile(html, problemsRoot + contestId + "\\" + problemId +"\\report.html");
    		return res;
		}
		catch (DJudgeXmlCorruptedException e)
		{
			DirectoryResult res = new DirectoryResult(problemsRoot + contestId + "\\" + problemId +"\\solutions");
			return res;
		}
		catch (DJudgeXmlNotFoundException e)
		{
			DirectoryResult res = new DirectoryResult(problemsRoot + contestId + "\\" + problemId +"\\solutions");
			return res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void generateContestReport(String contestId)
	{
		generateContestReport(contestId, new ExecutorLimits());
	}
	
	public static void generateContestReport(String contestId, ExecutorLimits limits)
	{
		StringBuffer s = new StringBuffer();
		String contestPath = problemsRoot + contestId + "\\";
		s.append("<h1> Contest \"" + contestId + "\" report [ " + Calendar.getInstance().getTime() + "]</h1>");
		String[] list = new File(contestPath).list();
		//for (char c = 'A'; c <= 'Z'; c++)
		for (int j = 0; j < list.length; j++)
		{
			if (list[j].startsWith("_") || !(new File(contestPath + list[j]).exists()) || !(new File(contestPath + list[j]).isDirectory())) continue;
			s.append("<h4><a href='./"+list[j]+"/report.html'>Problem " + list[j] + "<a></h4>");
			s.append("<table border=1>\n");
			s.append("<tr bgcolor=" + HtmlWorks.getHeaderColor() + ">");
				s.append("<th>#</th>");
				s.append("<th>File</th>");
				s.append("<th>Judgement</th>");
				s.append("<th>Score</th>");
				s.append("<th>MaxTime</th>");
				s.append("<th>MaxMemory</th>");
				s.append("<th>TotalTime</th>");
			s.append("</tr>\n");					
			String str = "" + list[j];
			DirectoryResult dr = generateProblemReport(contestId, str, limits);
			for (int i = 0; i < dr.res.size(); i++)
			{
				//String str = HtmlWorks.directoryResultToHtml(dr.res.get(i));
				SubmissionResult t = dr.res.get(i);
				String color = HtmlWorks.getJudgementColor(t.getJudgement());
				s.append("<tr bgcolor=" + color +">");
					s.append("<td>" + (i+1) + "</td>");
					s.append("<td><a href=#" + (i+1) + ">" + t.comment + "</a></td>");
					s.append("<td>" + t.getJudgement() + "</td>");
					s.append("<td>" + t.getScore() + "</td>");
					s.append("<td>" + HtmlWorks.formatRuntime(t.getMaxTime()) + "</td>");
					s.append("<td>" + HtmlWorks.formatMemorySize(t.getMaxMemory()) + "</td>");
					//s.append("<td>" + formatRuntime(t.getTotalTime()) + "</td>");
				s.append("</tr>\n");
			}
			s.append("</table>\n");
		}
		FileWorks.saveToFile(s.toString(), contestPath + "report.html");
	}
}