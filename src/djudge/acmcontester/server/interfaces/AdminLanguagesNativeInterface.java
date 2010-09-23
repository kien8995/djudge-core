/* $Id$ */

package djudge.acmcontester.server.interfaces;

import djudge.acmcontester.structures.LanguageData;

public interface AdminLanguagesNativeInterface extends AdminLanguagesCommonInterface
{
	@SuppressWarnings("unchecked")
	public LanguageData[] getLanguages(String username, String password);
}
