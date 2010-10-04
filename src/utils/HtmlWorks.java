/* $Id$ */

package utils;

import org.apache.commons.lang.StringEscapeUtils;

import djudge.judge.GroupDescription;
import djudge.judge.GroupResult;
import djudge.judge.ProblemDescription;
import djudge.judge.ProblemResult;
import djudge.judge.SubmissionResult;
import djudge.judge.TestDescription;
import djudge.judge.TestResult;
import djudge.judge.TestResultEnum;
import djudge.judge.checker.ValidationResult;
import djudge.judge.dexecutor.ExecutionResult;

public class HtmlWorks 
{
	public static String getHeaderColor()
	{
		return "Gainsboro";
	}
	
	public static String formatMemorySize(long size)
	{
		if (size < 0)
			return "n/a";
		if (size < 1000)
			return "" + size + "";
		if (size < 1000 * 1000)
			return "" + size/1000 +  " " + size%1000 + "";
		return "" + size/1000000 +  " " + ((size/1000)%1000) + " " + size%1000 + "";
	}
	
	public static String getJudgementColor(TestResultEnum j)
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
	
	public static String formatRuntime(long time)
	{
		if (time < 0)
			return "n/a";
		if (time <= 1000)
			return "" + time + " ms";
		//if (time <= 1000 * 60)
		return "" + time/1000 + "s " +  (time % 1000) + " ms";		
	}
	
	public static String testToHtml(TestResult res, TestDescription desc)
	{
		TestResultEnum judgement = res.getResult();
		ExecutionResult RunInfo = res.getRuntimeInfo();
		int TestNum = res.getTestNumber();
		ValidationResult validationInfo = res.getValidationInfo(); 
		
		StringBuffer s = new StringBuffer();
		String color = getJudgementColor(judgement);
		s.append("<tr bgcolor=" + color + ">");
			s.append("<td>" + (TestNum + 1)  + "</td>");
			s.append("<td>" + res.getScore()  + "</td>");
			s.append("<td>" + formatRuntime(RunInfo.timeConsumed) + " of " + formatRuntime(desc.getWorkLimits().timeLimit) + "</td>");
			s.append("<td>" + formatMemorySize(RunInfo.memoryConsumed) + " of " + formatMemorySize(desc.getWorkLimits().memoryLimit) + "</td>");
			s.append("<td>" + formatMemorySize(RunInfo.outputGenerated) + "</td>");
			s.append("<td>" + judgement  + "</td>");
			try
			{
				s.append("<td>" + (validationInfo.getFail() + " - ") 
						+ StringEscapeUtils.escapeHtml(StringWorks.ArrayToString(validationInfo.getValidatorOutput()))  + "</td>");
			}
			catch (Exception e)
			{
				s.append("<td></td>");
			}
			s.append("<td>" + desc.getInputMask() + " - " + desc.getOutputMask() + " - " + res.getRuntimeInfo().tempDir + "</td>");
		s.append("</tr>\n");
		return s.toString();		
	}
	
	public static String testGroupToHtml(GroupResult res, GroupDescription desc)
	{
		StringBuffer s = new StringBuffer();
/*		s.append("<table border=1>");
		s.append("<tr><td>Group#</td><td>" + (res.getGroupNumber() + 1) + "</td></tr>");
		s.append("<tr><td>Status</td><td>" + res.getJudgement() + "</td></tr>");
		s.append("<tr><td>MaxTime</td><td>" + formatRuntime(res.getMaxTime()) + "</td></tr>");
		s.append("<tr><td>MaxMemory</td><td>" + formatMemorySize(res.getMaxMemory()) + " K</td></tr>");
		s.append("</table>");*/
		s.append("<table border=1>");
		s.append("<tr><th>Test</th><th>Score</th><th>Time</th><th>Memory</th><th>OutputSize</th><th>Judgement</th><th>Validator</th><th>IO</th></tr>");
		try
		{
    		for (int i = 0; i < res.getTestsCount(); i++)
    			s.append(testToHtml(res.getTestInfo(i), desc.getTest(i)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		s.append("</table>\n");
		return s.toString();
	}
	
	public static String problemToHtml(SubmissionResult res2, ProblemDescription desc)
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
				s.append(testGroupToHtml(res.getGroupResult(i), desc.getGroup(i)));
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
	
	public static String directoryResultToHtml(DirectoryResult res, ProblemDescription desc)
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
			s.append(problemToHtml(t, desc));
		}
		return s.toString();		
	}
	
	
}
