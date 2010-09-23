/* $Id$ */

package djudge.acmcontester.server.interfaces;

import djudge.acmcontester.structures.LanguageData;

public interface TeamLanguagesNativeInterface
{
	public LanguageData[] getTeamLanguages(String username, String password);
	
}
