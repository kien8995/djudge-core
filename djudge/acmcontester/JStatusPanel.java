package djudge.acmcontester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import djudge.acmcontester.server.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.server.interfaces.TeamXmlRpcInterface;
import djudge.utils.xmlrpc.XmlRpcStateVisualizer;

public class JStatusPanel extends JPanel implements ActionListener, XmlRpcStateVisualizer
{
	private static final long serialVersionUID = 1L;
	
	private JLabel jlTime;
	private JLabel jlStatus;
	private JLabel jlConnectionStatus;
	private JButton jbExit;
	
	private ConnectionState state = new ConnectionState();
	
	TeamXmlRpcInterface serverInterface;
	
	AuthentificationDataProvider authProvider;
	
	public JStatusPanel(TeamXmlRpcInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		this.serverInterface = serverInterface;
		this.authProvider = authProvider;
		
		jlTime = new JLabel("Time Left");
		jlStatus = new JLabel("Contest Status");
		jlConnectionStatus = new JLabel("Connected");
		jbExit = new JButton("Exit");
		jbExit.addActionListener(this);
		
		add(jlStatus);
		add(jlTime);
		add(jbExit);
		add(jlConnectionStatus);
	}
	
	boolean f = false;
	
	public void updateData()
	{
		f = true;
		try
		{
    		String status = serverInterface.getContestStatus(authProvider.getUsername(), authProvider.getPassword());
    		jlStatus.setText(status);
    		long timeLeft = serverInterface.getContestTimeLeft(authProvider.getUsername(), authProvider.getPassword());
    		jlTime.setText("" + (timeLeft / (60 * 1000)));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		f = false;
		jlConnectionStatus.setText(state.fConnected ? "Connected " + state.lastConnectionTime : "Disconnected. Last connected " + state.lastSuccessTime);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object src = arg0.getSource();
		if (src.equals(jbExit))
		{
			if (JOptionPane.showConfirmDialog(this,
					"Do you really want to exit?", "Confirm",
					JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
			{
				System.exit(0);
			}
		}
	}

	@Override
	public void beforeMethodCall()
	{
		state.lastConnectionTime = new Date();
	}

	@Override
	public void onFailure()
	{
		state.fConnected = false;
		if (!f)
			updateData();
	}

	@Override
	public void onSuccess()
	{
		state.fConnected = true;
		state.lastConnectionTime = new Date();
		state.lastSuccessTime = new Date();
		if (!f)
			updateData();
	}
}
