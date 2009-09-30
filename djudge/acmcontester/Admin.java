package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import djudge.acmcontester.interfaces.ServerXmlRpcInterface;
import djudge.acmcontester.structures.RemoteTableLanguages;
import djudge.acmcontester.structures.RemoteTableProblems;
import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.acmcontester.structures.RemoteTableUsers;

public class Admin extends JFrame// implements AuthentificationDataProvider
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	private JTablePanel usersPanel;

	private JTablePanel problemsPanel;
	
	private JTablePanel languagesPanel;
	
	private JTablePanel submissionsPanel;
	
	private JMonitorPanel monitorPanel;
	
	private JSubmitPanel submitPanel;
	
	private JStatusPanel statusPanel;
	
	private AuthentificationData authData = new AuthentificationData("root", "root");
	
	private ServerXmlRpcInterface serverXmlRpcInterface = new ServerXmlRpcConnector();
	
	class WatchThread extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				statusPanel.updateData();
				try
				{
					sleep(10000);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	private void setupGUI()
	{
		setTitle("Contest Manager");
		setLayout(new BorderLayout());
		jtpTabs = new JTabbedPane();
		
		jtpTabs.add("Start/Stop", new JContestSettingsPanel());
		
		jtpTabs.add("Users", usersPanel = new JTablePanel(new RemoteTableUsers(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Problems", problemsPanel = new JTablePanel(new RemoteTableProblems(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Languages", languagesPanel = new JTablePanel(new RemoteTableLanguages(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Runs", submissionsPanel = new JAdminSubmissionsPanel(new RemoteTableSubmissions(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Monitor", monitorPanel = new JMonitorPanel(serverXmlRpcInterface, authData));
		
		jtpTabs.add("Submit", submitPanel = new JSubmitPanel(serverXmlRpcInterface, authData));
		
		add(jtpTabs, BorderLayout.CENTER);
		
		add(statusPanel = new JStatusPanel(serverXmlRpcInterface, authData), BorderLayout.NORTH);
	}
	
	public Admin()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(640, 480));
		setSize(640, 480);
		setLocation(250, 200);
		setupGUI();
		setVisible(true);
		new WatchThread().start();
	}

	public static void main(String[] args)
	{
		new Admin();
	}

/*	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getUsername()
	{
		return username;
	}*/
}
