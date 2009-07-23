package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import djudge.acmcontester.client.JSubmitPanel;
import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;
import djudge.acmcontester.interfaces.AuthentificationDataProvider;

import utils.FileWorks;

public class Admin extends JFrame implements AuthentificationDataProvider
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	private JTablePanel usersPanel;

	private JTablePanel problemsPanel;
	
	private JTablePanel languagesPanel;
	
	private JTablePanel submissionsPanel;
	
	private JMonitorPanel monitorPanel;
	
	private JSubmitPanel submitPanel;
	
	private ContestCore core;
	
	private String username = "root";
	
	private String password = "root";
	
	private AcmContesterXmlRpcClientInterface serverInterface = new AcmContesterClientStub();
	
	private void setupGUI()
	{
		core = new ContestCore();
		
		setTitle("Contest Manager");
		setLayout(new BorderLayout());
		jtpTabs = new JTabbedPane();
		
		jtpTabs.add("Users", usersPanel = new JTablePanel(core.getUsersModel()));
		
		jtpTabs.add("Problems", problemsPanel = new JTablePanel(core.getProblemsModel()));
		
		jtpTabs.add("Languages", languagesPanel = new JTablePanel(core.getLanguagesModel()));
		
		jtpTabs.add("Runs", submissionsPanel = new JTablePanel(core.getSubmissionsDataModel()));
		
		jtpTabs.add("Monitor", monitorPanel = new JMonitorPanel(serverInterface, this));
		
		jtpTabs.add("Submit", submitPanel = new JSubmitPanel(serverInterface, this));
		
		add(jtpTabs, BorderLayout.CENTER);
	}
	
	public Admin()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(640, 480));
		setSize(640, 480);
		setLocation(250, 200);
		setupGUI();
		setData();
		setVisible(true);
	}

	private void setData()
	{
		//core.getAllSubmissions(new AuthentificationData());
		//core.submitSolution("alt", "p78", "2", "1", FileWorks.readFile("d:/A-alt.cpp"));
	}

	public static void log(Object o)
	{
		System.out.println(o);
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getUsername()
	{
		return username;
	}
}
