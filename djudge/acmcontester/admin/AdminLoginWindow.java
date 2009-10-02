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

	public static void main(String[] args)
	{
		AuthentificationData ad = new AuthentificationData();
		AdminLoginWindow wnd = new AdminLoginWindow(new AdminXmlRpcConnector(), ad);
		wnd.setModal(true);
		wnd.setVisible(true);
		System.out.println(ad.username + " " + ad.password + " " + ad.isLoggedIn);
	}
}