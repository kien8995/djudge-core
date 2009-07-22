package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Admin extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	private JTablePanel usersPanel;

	private JTablePanel problemsPanel;
	
	private JTablePanel languagesPanel;
	
	private JTablePanel submissionsPanel;
	
	private ContestCore core;
	
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
		//core.submitSolution("alt", "p78", "1", "1", FileWorks.readFile("d:/A-alt.cpp"));
	}

	public static void log(Object o)
	{
		System.out.println(o);
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
}
