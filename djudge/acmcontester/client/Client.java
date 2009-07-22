package djudge.acmcontester.client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import djudge.acmcontester.ProblemsPanel;
import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;

public class Client extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	private JPanel jpTop;
	
	public static AcmContesterXmlRpcClientInterface server;
	
	static
	{
		server = new AcmContesterClientXmlRpcConnector();
	}
	
	private void setupGUI()
	{
		setTitle("DJudge - AcmContester - Contestant's Client");
		setLayout(new BorderLayout());
		jtpTabs = new JTabbedPane();
		
		jtpTabs.add("Submit", new JSubmitPanel());
		jtpTabs.add("Runs", new JRunsPanel());
		jtpTabs.add("Monitor", new JPanel());
		jtpTabs.add("Events", new JPanel());
		jtpTabs.add("Clars", new JPanel());
		jtpTabs.add("Settings", new JPanel());
		
		add(jtpTabs, BorderLayout.CENTER);
		
		jpTop = new JPanel();
		jpTop.setBorder(BorderFactory.createTitledBorder("Info"));
		
		jpTop.setPreferredSize(new Dimension(40, 40));
		
		add(jpTop, BorderLayout.NORTH);
	}
	
	public Client()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(640, 480));
		setSize(640, 480);
		setLocation(250, 200);
		setupGUI();
		setVisible(true);
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
