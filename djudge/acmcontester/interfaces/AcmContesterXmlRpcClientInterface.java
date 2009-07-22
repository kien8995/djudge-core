package djudge.acmcontester.interfaces;


public interface AcmContesterXmlRpcClientInterface extends ProblemsInterface, UsersInterface, SubmissionsInterface, LanguagesInterface
{
	public String getVersion();
}
