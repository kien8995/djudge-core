package djudge.acmcontester.server;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.createtank.util.configuration.XMLConfiguration;

public class XMLSettings
{
	private static final Logger log = Logger.getLogger(XMLSettings.class);
	
	private Properties properties;
	
	public XMLSettings(Class className)
	{
		this(className.getName() + ".xml");
	}
	
	public XMLSettings(String configFile)
	{
		try
		{
			XMLConfiguration conf = new XMLConfiguration(configFile);
			properties = conf.getProperties("settings");
		} catch (Exception e)
		{
			log.error("Wrong config file: " + configFile, e);
		}
	}
	
	public Properties getProperties()
	{
		return properties;
	}
	
	public String getProperty(String propertyName)
	{
		return properties.getProperty(propertyName);
	}
	
	public int getInt(String propertyName, int defaultValue)
	{
		String value = properties.getProperty(propertyName);
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception e) {
		}
		return defaultValue;
	}
	
	public boolean getBoolean(String propertyName, boolean defaultValue)
	{
		String value = properties.getProperty(propertyName);
		try
		{
			return Boolean.parseBoolean(value);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return defaultValue;
	}
}
