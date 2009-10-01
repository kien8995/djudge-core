package djudge.acmcontester.server;

import java.util.Date;

import org.apache.log4j.Logger;

import utils.FileWorks;
import djudge.acmcontester.structures.ContestStatusEnum;

public class ContestState
{
	private static final Logger log = Logger.getLogger(ContestState.class);

	private ContestStateInternals internals;
	
	class ContestStateInternals
	{
		private final Logger log = Logger.getLogger(ContestStateInternals.class);
		
		private long lastLeftTime = -1;
		private Date lastStartTime;
		private long contestTimeElapsed = 0;
		
		private ContestStatusEnum state = ContestStatusEnum.Running;
		
		private ContestSettings settings;
		
		public ContestStateInternals(ContestSettings settings)
		{
			this.settings = settings;
			loadSettings();
		}

		public long getTimeSinceLastStartAbsolute()
		{
			if (lastLeftTime > 0)
			{
				return new Date().getTime() - lastStartTime.getTime();
			}
			return 0;
		}
		
		public long getTimeSinceLastStartNormalized()
		{
			long absTime = getTimeSinceLastStartAbsolute();
			absTime = Math.min(absTime, lastLeftTime);
			absTime = Math.max(absTime, 0);
			return absTime;
		}
		
		public long getContestTimeElapsed()
		{
			return contestTimeElapsed + getTimeSinceLastStartNormalized();
		}
		
		public boolean saveState()
		{
			String res = "";
			res += getContestTimeElapsed() + " ";
			res += getContestTimeLeft() + " ";
			res += new Date() + " ";
			FileWorks.saveToFile(res, "./data/contest-settings.txt");
			log.info("Saving contest state");
			return true;
		}
		
		private void loadSettings()
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
		
		protected boolean isRunnning()
		{
			return state == ContestStatusEnum.Running &&  lastLeftTime > 0 && getTimeSinceLastStartAbsolute() == getTimeSinceLastStartNormalized();
		}
		
		protected long getContestTime()
		{
			saveState();
			return getContestTimeElapsed();
		}

		protected void stopContest()
		{
			lastLeftTime = -1;
			saveState();
		}

		protected void startContest(long timeLeft)
		{
			lastLeftTime = timeLeft;
			lastStartTime = new Date();
			saveState();
		}
		
		protected ContestStatusEnum getContestStatus()
		{
			if (isRunnning())
			{
				return ContestStatusEnum.Running;
			}
			return ContestStatusEnum.Stopped;
		}

		protected long getContestTimeLeft()
		{
			return lastLeftTime - getTimeSinceLastStartNormalized();
		}
		
		protected void setContestTimePassed(long newTimePassed)
		{
			contestTimeElapsed = newTimePassed;
			lastStartTime = new Date();
			saveState();
		}
		
		protected void setContestTimeLeft(long newTimeLeft)
		{
			lastLeftTime = newTimeLeft;
			saveState();
		}	
	}

	/*
	 * Default constructor
	 */
	
	public ContestState(ContestSettings settings)
	{
		internals = new ContestStateInternals(settings);
	}
	
	/* 
	 * Interface methods
	 */
	
	synchronized public boolean isRunnning()
	{
		return internals.isRunnning();
	}
	
	synchronized public long getContestTime()
	{
		return internals.getContestTime();
	}
	
	synchronized public long getContestTimeLeft()
	{
		return internals.getContestTimeLeft();
	}
	
	synchronized public ContestStatusEnum getContestState()
	{
		return internals.getContestStatus();
	}

	synchronized public boolean setContestTime(long contestTime)
	{
		internals.setContestTimePassed(contestTime);
		return true;
	}
	
	synchronized public boolean setContestTimeLeft(long contestTimeLeft)
	{
		internals.setContestTimeLeft(contestTimeLeft);
		return true;
	}	
}
