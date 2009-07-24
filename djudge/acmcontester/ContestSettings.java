package djudge.acmcontester;

import java.util.Date;

import utils.FileWorks;

import djudge.acmcontester.structures.ContestStatusEnum;

public class ContestSettings
{
	private long lastLeftTime = -1;
	private Date lastStartTime;
	private long contestTimeElapsed = 0;
	
	{
		loadSettings();
	}
	
	private long getTimeSinceLastStartAbsolute()
	{
		if (lastLeftTime > 0)
		{
			return new Date().getTime() - lastStartTime.getTime();
		}
		return 0;
	}
	
	private long getTimeSinceLastStartNormalized()
	{
		long absTime = getTimeSinceLastStartAbsolute();
		absTime = Math.min(absTime, lastLeftTime);
		absTime = Math.max(absTime, 0);
		return absTime;
	}
	
	private long getContestTimeElapsed()
	{
		return contestTimeElapsed + getTimeSinceLastStartNormalized();
	}
	
	synchronized public boolean isRunnning()
	{
		return lastLeftTime > 0 && getTimeSinceLastStartAbsolute() == getTimeSinceLastStartNormalized();
	}

	synchronized public long getContestTime()
	{
		saveSettings();
		return getContestTimeElapsed();
	}

	synchronized public void stopContest()
	{
		saveSettings();
		lastLeftTime = -1;
	}

	synchronized public void startContest(long timeLeft)
	{
		saveSettings();
		lastLeftTime = timeLeft;
		lastStartTime = new Date();
	}
	
	synchronized public ContestStatusEnum getContestStatus()
	{
		if (isRunnning())
		{
			return ContestStatusEnum.Running;
		}
		return ContestStatusEnum.Stopped;
	}

	public long getContestTimeLeft()
	{
		return lastLeftTime - getTimeSinceLastStartNormalized();
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
	
	public void saveSettings()
	{
		long elapsedTotal = getContestTimeElapsed();
		long leftTotal = getContestTimeLeft();
		FileWorks.saveToFile(elapsedTotal + " " + leftTotal + " " + getContestStatus(), "./data/contest-settings.txt");
	}
	
	public void loadSettings()
	{
		try
		{
            String str[] = FileWorks.readFile("./data/contest-settings.txt").split(" ");
            long elapsedTotal = Long.parseLong(str[0]);
            long leftTotal = Long.parseLong(str[1]);
            lastLeftTime = leftTotal;
            lastStartTime = new Date();
            contestTimeElapsed = elapsedTotal;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
