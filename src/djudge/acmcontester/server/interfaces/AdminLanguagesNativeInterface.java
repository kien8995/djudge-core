/* $Id$ */

package djudge.acmcontester.server.interfaces;

import djudge.acmcontester.structures.LanguageData;

public interface AdminLanguagesNativeInterface extends AdminLanguagesCommonInterface
{
	public LanguageData[] getLanguages(String username, String password);
}
