package djudge.acmcontester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;
import djudge.acmcontester.interfaces.AuthentificationDataProvider;

public class JStatusPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private JLabel jlTime;
	private JLabel jlStatus;
	private JButton jbExit;
	
	AcmContesterXmlRpcClientInterface serverInterface;
	AuthentificationDataProvider authProvider;
	
	public JStatusPanel(AcmContesterXmlRpcClientInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		this.serverInterface = serverInterface;
		this.authProvider = authProvider;
		
		jlTime = new JLabel("Time Left");
		jlStatus = new JLabel("Contest Status");
		jbExit = new JButton("Exit");
		jbExit.addActionListener(this);
		
		add(jlStatus);
		add(jlTime);
		add(jbExit);		
	}
	
	public void updateData()
	{
		String status = serverInterface.getContestStatus(authProvider.getUsername(), authProvider.getPassword());
		jlStatus.setText(status);
		long timeLeft = serverInterface.getContestTimeLeft(authProvider.getUsername(), authProvider.getPassword());
		jlTime.setText("" + (timeLeft / (60 * 1000)));
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
}