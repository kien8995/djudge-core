package djudge.acmcontester.server;

import djudge.utils.XMLSettings;

public class ServerSettings
{
	private static final String configFile = "server.xml";

	private static XMLSettings settings;
	
	static
	{
		settings = new XMLSettings(configFile);
	}
	
	public static String getProperty(String propertyName)
	{
		return settings.getProperty(propertyName);
	}
}
