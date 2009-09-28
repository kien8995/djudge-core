package djudge.acmcontester.server;

import java.util.Date;

import org.apache.log4j.Logger;

import utils.FileWorks;

import djudge.acmcontester.Admin;
import djudge.acmcontester.structures.ContestStatusEnum;

public class ContestState
{
	private static final Logger log = Logger.getLogger(ContestState.class);
	
	private long lastLeftTime = -1;
	private Date lastStartTime;
	private long contestTimeElapsed = 0;
	
	ContestSettings settings;
	
	public ContestState(ContestSettings settings)
	{
		this.settings = settings;
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
            log.info("lastLeftTime = " + lastLeftTime);
            log.info("lastStartTime = " + lastStartTime);
            log.info("contestTimeElapsed = " + elapsedTotal);
		}
		catch (Exception e)
		{
			log.error("loadSettings", e);
		}
		
	}
}
