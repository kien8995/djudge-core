package djudge.acmcontester.admin;

import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.JContestSettingsPanel;
import djudge.acmcontester.JMonitorPanel;
import djudge.acmcontester.JStatusPanel;
import djudge.acmcontester.JSubmitPanel;
import djudge.acmcontester.structures.RemoteTableLanguages;
import djudge.acmcontester.structures.RemoteTableProblems;
import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.acmcontester.structures.RemoteTableUsers;

public class AdminClient extends JFrame// implements AuthentificationDataProvider
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
	
	private AdminXmlRpcConnector serverXmlRpcInterface = new AdminXmlRpcConnector();
	
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
		
		jtpTabs.add("Start/Stop", new JContestSettingsPanel(serverXmlRpcInterface, authData));
		
		jtpTabs.add("Users", usersPanel = new JTablePanel(new RemoteTableUsers(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Problems", problemsPanel = new JTablePanel(new RemoteTableProblems(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Languages", languagesPanel = new JTablePanel(new RemoteTableLanguages(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Runs", submissionsPanel = new JAdminSubmissionsPanel(new RemoteTableSubmissions(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Monitor", monitorPanel = new JMonitorPanel(serverXmlRpcInterface, authData));
		
		jtpTabs.add("Submit", submitPanel = new JSubmitPanel(serverXmlRpcInterface, authData));
		
		add(jtpTabs, BorderLayout.CENTER);
		
		add(statusPanel = new JStatusPanel(serverXmlRpcInterface, authData), BorderLayout.NORTH);
		serverXmlRpcInterface.setVizi(statusPanel);
	}
	
	public AdminClient()
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
		new AdminClient();
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
