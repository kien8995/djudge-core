/* $Id$ */

package djudge.acmcontester;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashSet;

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
	
	HashSet<Updateble> objectsToUpdate = new HashSet<Updateble>();
	
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
	
	public void updateView()
	{
		f = true;
		try
		{
    		long timeLeft = serverInterface.getContestTimeLeft(authProvider.getUsername(), authProvider.getPassword());
    		jlTime.setText("" + (timeLeft / (60 * 1000)));
    		if (timeLeft / (60 * 1000) > 5)
    			jlTime.setForeground(Color.BLUE);
    		else
    			jlTime.setForeground(Color.RED);
    		String status = serverInterface.getContestStatus(authProvider.getUsername(), authProvider.getPassword());
    		jlStatus.setText(status);
    		if (status.equalsIgnoreCase("running"))
    			jlStatus.setForeground(Color.green);
    		else
    			jlStatus.setForeground(Color.RED);
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		f = false;
		jlConnectionStatus.setText(state.fConnected ? "Connected " + state.lastConnectionTime : "Disconnected. Last connected " + state.lastSuccessTime);
	}
	
	public void updateData()
	{
		for (Updateble object : objectsToUpdate)
		{
			try
			{
				object.updateState();
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
		}
		updateView();
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
			updateView();
	}

	@Override
	public void onSuccess()
	{
		state.fConnected = true;
		state.lastConnectionTime = new Date();
		state.lastSuccessTime = new Date();
		if (!f)
			updateView();
	}
	
	public void addUpdatetableObject(Updateble object)
	{
		objectsToUpdate.add(object);
	}
}
