/* $Id: Scripts.java, v 0.1 2008/07/28 05:13:08 alt Exp $ */

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

import judge.ProblemDescription;

import common_data_structures.RunnerLimits;

public class Scripts 
{
	// TODO Hadrcode
	public static final String problemsRoot = ".\\problems\\";

	public static void generateProblemReport(String contestId, String problemId)
	{
		generateProblemReport(contestId, problemId, new RunnerLimits());
	}
	
	public static void generateProblemReport(String contestId, String problemId, RunnerLimits limits)
	{
		ProblemDescription desc = new ProblemDescription(contestId, problemId);
		
		JudgeDirectory j = new JudgeDirectory(desc);
		
		DirectoryResult res = j.judge(problemsRoot + contestId + "\\" + problemId +"\\solutions");
		String html = "<h1>Problem " + problemId + " (" + contestId + ") [ " + Calendar.getInstance().getTime() +  "]</h1>"
						+ HtmlWorks.directoryResultToHtml(res);
		FileWorks.saveToFile(html, problemsRoot + contestId + "\\" + problemId +"\\report.html");
	}
	
	public static void generateContestReport(String contestId)
	{
		generateContestReport(contestId, new RunnerLimits());
	}
	
	public static void generateContestReport(String contestId, RunnerLimits limits)
	{
		StringBuffer s = new StringBuffer();
		String contestPath = problemsRoot + contestId + "\\";
		s.append("<h1> Contest \"" + contestId + "\" report [ " + Calendar.getInstance().getTime() + "]</h1>");
		for (char c = 'A'; c <= 'Z'; c++)
		{
			if (!(new File(contestPath + c).exists())) continue;
			s.append("<h4><a href='./"+c+"/report.html'>Problem " + c + "<a></h4>");
			String str = "" + c;
			generateProblemReport(contestId, str, limits);
		}
		FileWorks.saveToFile(s.toString(), contestPath + "report.html");
	}
}
