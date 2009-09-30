package djudge.acmcontester.interfaces;

import djudge.acmcontester.structures.LanguageData;

public interface AdminLanguagesNativeInterface extends AdminLanguagesCommonInterface
{
	@SuppressWarnings("unchecked")
	public LanguageData[] getLanguages(String username, String password);
}
