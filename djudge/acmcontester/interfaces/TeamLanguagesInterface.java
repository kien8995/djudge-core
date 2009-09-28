package djudge.acmcontester.interfaces;

import java.util.HashMap;

public interface TeamLanguagesInterface
{
	public HashMap<String, String>[] getLanguages(String username, String password);
	
}
