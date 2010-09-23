package djudge.utils;

public class XMLConfigurable implements XMLConfigurableInterface
{

	@Override
	public String getConfigFilename()
	{
		return this.getClass().toString();
	}

}
