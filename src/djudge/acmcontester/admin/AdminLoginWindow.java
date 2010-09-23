package djudge.acmcontester.admin;


import javax.swing.JOptionPane;
import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.LoginWindow;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;

public class AdminLoginWindow extends LoginWindow
{
	private static final long serialVersionUID = 1L;

	private ServerXmlRpcInterface serverConnector;
	
	public AdminLoginWindow(ServerXmlRpcInterface connector, AuthentificationData authData)
	{
		super(authData, "Admin login");
		this.serverConnector = connector;
	}
	
	@Override
	protected boolean doLogin()
	{
		authData.isLoggedIn = false;
		authData.password = tfPassword.getText();
		authData.username = tfLogin.getText();
		try
		{
			// TODO: Fixme
			if (serverConnector.enterContestTeam(authData.username, authData.password))
				authData.isLoggedIn = true;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Failed connect to server", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return authData.isLoggedIn;
	}

	@Override
	protected boolean doConnect()
	{
		return echoString.equals(serverConnector.echo(echoString));
	}
}