package djudge.acmcontester.server;

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