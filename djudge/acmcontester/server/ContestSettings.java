package djudge.acmcontester.server;

import djudge.utils.XMLSettings;

public class ContestSettings
{
	private final static int INFINITY = Integer.MAX_VALUE;
	
	private XMLSettings settings;
	
	public ContestSettings()
	{
		this("contest.xml");
	}
	
	public ContestSettings(String configFile)
	{
		settings = new XMLSettings(configFile);
	}
	
	public int getMaxSubmitsPerUserCount()
	{
		return settings.getInt("max-submits-per-user", INFINITY);
	}
	
	public String getContestName()
	{
		return settings.getProperty("name");
	}
	
	public int getMaxSubmitsPerProblemCount()
	{
		return settings.getInt("max-submits-per-task", INFINITY);
	}
	
	public boolean showAlienMonitor()
	{
		return settings.getBoolean("show-alien-monitor", true);
	}
	
	public boolean showAlienSubmissionDetails()
	{
		return settings.getBoolean("show-alien-details", false);
	}
	
	public boolean showAlienSubmissions()
	{
		return settings.getBoolean("show-alien-submissions", true);
	}

	public boolean showScore()
	{
		return settings.getBoolean("show-score", true);
	}

	public boolean showWrongTestNumber()
	{
		return settings.getBoolean("show-wrong-test-number", true);
	}

	public boolean showTimeConsumed()
	{
		return settings.getBoolean("show-time-consumed", true);
	}

	public boolean showMemoryConsumed()
	{
		return settings.getBoolean("show-memory-consumed", true);
	}

	public boolean showOutputSize()
	{
		return settings.getBoolean("show-output-size", true);
	}

	public boolean hideCompilationErrors()
	{
		return settings.getBoolean("hide-compilation-error-submissions", true);
	}

	public boolean allowFirstTestSubmissions()
	{
		return settings.getBoolean("allow-first-test-only-submissions", true);
	}
	
	public boolean allowNewUserRegistration()
	{
		return settings.getBoolean("allow-new-user-registration", true);
	}
}
