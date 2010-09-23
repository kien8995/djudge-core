package djudge.gui;

import utils.PrintfFormat;

public class Formatter
{
	
	public static String formatJudgement(String j)
	{
		return j;
	}
	
	public static String formatRealTime(String time)
	{
		return time;
	}
	
	public static String formatContestTime(long miliseconds)
	{
		if (miliseconds < 0)
			return "-";
		long seconds = miliseconds / 1000;
		return new PrintfFormat("%02d:%02d:%02d").sprintf(new Object[] {seconds / 60 / 60, (seconds / 60) % 60, seconds % 60});
	}
	
	public static String formatDJudgeFlag(int flag)
	{
		if (flag < 0)
			return "At judge";
		else if (flag > 0)
			return "Judged";
		else
			return "NEW";
	}

	public static String formatFailedTest(int number)
	{
		if (number < 0)
			return "-";
		return "" + (number + 1);
	}
	
	public static String formatMemory(long bytes)
	{
		if (bytes <= 0)
			return "-";
		if (bytes < 1000)
			return "" + bytes + " B";
		long kb = bytes / 1000;
		if (kb < 1000)
			return "" + kb + " KB";
		return new PrintfFormat("%d %03d KB").sprintf(new Object[] {kb / 1000, kb % 1000});
	}
	
	public static String formatRuntime(long ms)
	{
		if (ms < 0)
			return "-";
		return "" + ms + " ms";
	}

	public static String formatScore(long score)
	{
		if (score < 0)
			return "-";
		return "" + score;
	}
	
}
