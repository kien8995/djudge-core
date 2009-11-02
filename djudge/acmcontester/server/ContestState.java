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
			loadState();
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
			res += getContestTimeElapsed() + "\n";
			res += getContestTimeLeft() + "\n";
			res += state + "\n";
			FileWorks.saveToFile(res, "./data/contest-settings.txt");
			return true;
		}
		
		private void loadState()
		{
			try
			{
	            String str[] = FileWorks.readFile("./data/contest-settings.txt").split("\n");
	            contestTimeElapsed = Long.parseLong(str[0]);
	            lastLeftTime = Long.parseLong(str[1]);
	            state = ContestStatusEnum.valueOf(str[2]);
	            lastStartTime = new Date();
	            log.info("lastLeftTime = " + lastLeftTime);
	            log.info("lastStartTime = " + lastStartTime);
	            log.info("contestTimeElapsed = " + contestTimeElapsed);
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
		
		protected void setContestRunning(boolean isRunning)
		{
			state = isRunning ? ContestStatusEnum.Running : ContestStatusEnum.Stopped;
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
		
		protected void setContestTime(long newTimePassed)
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

		public boolean isFrozen()
		{
			//
			return false;
		}

		public void incrementContestTimeLeft(long timeLeftAdd)
		{
			lastLeftTime += timeLeftAdd;
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
	
	synchronized public boolean isFrozen()
	{
		return internals.isFrozen();
	}
	
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
		log.info("SetContestTime " + contestTime / 1000 / 60 + " min");
		internals.setContestTime(contestTime);
		return true;
	}
	
	synchronized public boolean setContestTimeLeft(long contestTimeLeft)
	{
		log.info("SetContestTimeLeft " + contestTimeLeft / 1000 / 60 + " min");
		internals.setContestTimeLeft(contestTimeLeft);
		return true;
	}

	synchronized public boolean setContestRunning(boolean isRunning)
	{
		log.info("SetRunning: " + isRunning);
		internals.setContestRunning(isRunning);
		return true;
	}

	public long getContestTimeFrozen()
	{
		// TODO Auto-generated method stub
		return getContestTime();
	}

	public void incrementContestTimeLeft(long timeLeftAdd)
	{
		internals.incrementContestTimeLeft(timeLeftAdd);
	}	
}
