package djudge.acmcontester;

import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public abstract class LoginWindow extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	protected JButton btnLogon;
	
	protected JButton btnExit;
	
	protected JTextField tfLogin;
	
	protected JTextField tfPassword;
	
	private final Dimension tfSize = new Dimension(250, 25);
	
	private final Dimension btnSize = new Dimension(100, 25);
	
	private final String titleString = "<html><h2 align='center'>DJudge.Contester</h2>";
	
	protected final AuthentificationData authData;
	
	protected final String echoString = "Echo";
	
	protected abstract boolean doLogin();
	
	protected abstract boolean doConnect();
	
	private void setupUI()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 3, 3, 5);

		JLabel title = new JLabel(titleString);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 6;
		c.anchor = GridBagConstraints.CENTER;
		add(title, c);
		
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.WEST;
		add(new JLabel("Login"), c);
		
		tfLogin = new JTextField();
		tfLogin.setPreferredSize(tfSize);
		c.gridy = 6;
		c.gridwidth = 4;
		add(tfLogin, c);
		
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 2;
		add(new JLabel("Password"), c);
		
		tfPassword = new JTextField();
		tfPassword.setPreferredSize(tfSize);
		c.gridy = 10;
		c.gridwidth = 4;
		add(tfPassword, c);
		
		btnLogon = new JButton("Logon");
		btnLogon.setPreferredSize(btnSize);
		btnLogon.addActionListener(this);
		c.gridy = 12;
		c.gridwidth = 2;
		add(btnLogon, c);
		
		btnExit = new JButton("Exit");
		btnExit.setPreferredSize(btnSize);
		btnExit.addActionListener(this);
		c.gridy = 12;
		c.gridx = 3;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.EAST;
		add(btnExit, c);
		
		c.gridx = 0;
		c.gridy = 14;
		c.gridheight = 2;
		c.gridwidth = 6;
		c.anchor = GridBagConstraints.CENTER;
		add(new JLabel(), c);		
	}
	
	public LoginWindow(AuthentificationData ad, String title)
	{
		this.authData = ad;
		setTitle(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(380, 300);
		setupUI();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource().equals(btnExit))
		{
			System.exit(0);
		}
		else if (arg0.getSource().equals(btnLogon))
		{
			if (doLogin())
			{
				setVisible(false);
				dispose();
			}
		}
	}
}
