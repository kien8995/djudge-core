package djudge.acmcontester.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.JContestSettingsPanel;
import djudge.acmcontester.JMonitorACMPanel;
import djudge.acmcontester.JStatusPanel;
import djudge.acmcontester.JSubmitPanel;
import djudge.acmcontester.JTestPanel;
import djudge.acmcontester.ServerXmlRpcConnector;
import djudge.acmcontester.structures.RemoteTableLanguages;
import djudge.acmcontester.structures.RemoteTableProblems;
import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.acmcontester.structures.RemoteTableUsers;

public class AdminClient extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	@SuppressWarnings("unused")
	private JTablePanel usersPanel;

	@SuppressWarnings("unused")
	private JTablePanel problemsPanel;
	
	@SuppressWarnings("unused")
	private JTablePanel languagesPanel;
	
	private JAdminSubmissionsPanel submissionsPanel;
	
	private JMonitorACMPanel monitorPanelACM;
	
	//private JMonitorIOIPanel monitorPanelIOI;
	
	private JSubmitPanel submitPanel;
	
	private JStatusPanel statusPanel;
	
	private AuthentificationData authData = new AuthentificationData("root", "root");
	
	private ServerXmlRpcConnector serverXmlRpcInterface = new ServerXmlRpcConnector();
	
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
					sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	private void setupGUI()
	{
		setTitle("Contest Manager");
		setLayout(new BorderLayout());
		jtpTabs = new JTabbedPane();
		
		jtpTabs.add("Manage Contest", new JContestSettingsPanel(serverXmlRpcInterface, authData));
		
		jtpTabs.add("Users", usersPanel = new JUsersPanel(new RemoteTableUsers(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Problems", problemsPanel = new JTablePanel(new RemoteTableProblems(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Languages", languagesPanel = new JTablePanel(new RemoteTableLanguages(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Runs", submissionsPanel = new JAdminSubmissionsPanel(new RemoteTableSubmissions(serverXmlRpcInterface, authData)));
		
		jtpTabs.add("Monitor-ACM", monitorPanelACM = new JMonitorACMPanel(serverXmlRpcInterface, authData));
		
		//jtpTabs.add("Monitor-IOI", monitorPanelIOI = new JMonitorIOIPanel(serverXmlRpcInterface, authData));
		
		jtpTabs.add("Submit", submitPanel = new JSubmitPanel(serverXmlRpcInterface, authData));
		
		jtpTabs.add("Test", new JTestPanel(serverXmlRpcInterface, authData));
		
		add(jtpTabs, BorderLayout.CENTER);
		
		add(statusPanel = new JStatusPanel(serverXmlRpcInterface, authData), BorderLayout.NORTH);
		serverXmlRpcInterface.setVizi(statusPanel);
		statusPanel.addUpdatetableObject(submissionsPanel);
		statusPanel.addUpdatetableObject(monitorPanelACM);
		//statusPanel.addUpdatetableObject(monitorPanelIOI);
		statusPanel.addUpdatetableObject(submitPanel);
	}
	
	public AdminClient()
	{
		this("root", "root");
	}
	
	public AdminClient(String username, String password)
	{
		authData.password = password;
		authData.username = username;
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
/*		AuthentificationData ad = new AuthentificationData();
		AdminLoginWindow wnd = new AdminLoginWindow(new ServerXmlRpcConnector(), ad);
		wnd.setModal(true);
		wnd.setVisible(true);
		if (ad.isLoggedIn)
		{
			new AdminClient(ad.getUsername(), ad.getPassword());
		}*/
		new AdminClient();
	}
}
