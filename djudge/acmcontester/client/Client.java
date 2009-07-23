package djudge.acmcontester.client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import djudge.acmcontester.JMonitorPanel;
import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;
import djudge.acmcontester.interfaces.AuthentificationDataProvider;

public class Client extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	public static AcmContesterXmlRpcClientInterface server;
	public static String username;
	public static String password;
	public static boolean fConnected;

	private JStatusPanel jspStatus;
	
	private static AuthentificationDataProvider authentificationDataProvider;
	
	class ClientAuthProvider implements AuthentificationDataProvider
	{

		@Override
		public String getPassword()
		{
			return Client.password;
		}

		@Override
		public String getUsername()
		{
			return Client.username;
		}
		
	}
	
	class WatchThread extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				String status = Client.server.getContestStatus(Client.username, Client.password);
				jspStatus.jlStatus.setText(status);
				long timeLeft = Client.server.getContestTimeLeft(Client.username, Client.password);
				jspStatus.jlTime.setText("" + (timeLeft / (60 * 1000)));
				try
				{
					sleep(5000);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	static
	{
		server = new AcmContesterClientXmlRpcConnector();
		username = "alt";
		password = "p78";
	}
	
	private void setupGUI()
	{
		setTitle("DJudge - AcmContester - Contestant's Client");
		
		jtpTabs = new JTabbedPane();
		
		jtpTabs.add("Submit", new JSubmitPanel(server, authentificationDataProvider));
		
		jtpTabs.add("Runs", new JRunsPanel());
		
		jtpTabs.add("Monitor", new JMonitorPanel(server, authentificationDataProvider));
		
		jtpTabs.add("Events", new JNotImplementedPanel());
		
		jtpTabs.add("Clars", new JNotImplementedPanel());
		
		jtpTabs.add("Settings", new JNotImplementedPanel());

		jspStatus = new JStatusPanel();
		
		setLayout(new BorderLayout());
		add(jtpTabs, BorderLayout.CENTER);
		add(jspStatus, BorderLayout.NORTH);
	}
	
	public Client()
	{
		authentificationDataProvider = new ClientAuthProvider();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(640, 480));
		setSize(640, 480);
		setLocation(250, 200);
		setupGUI();
		setVisible(true);

		new WatchThread().start();
	}

	public static void log(Object o)
	{
		System.out.println(o);
	}
	
	public static void main(String[] args)
	{
		new Client();
	}
}
