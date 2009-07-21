package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import djudge.acmcontester.models.ProblemsModel;
import djudge.acmcontester.models.UsersModel;
import djudge.acmcontester.structures.ProblemDescription;
import djudge.acmcontester.structures.UserDescription;

public class Admin extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	private UsersPanel usersPanel;

	private ProblemsPanel problemsPanel;
	
	private Vector<UserDescription> users;
	
	private Vector<ProblemDescription> problems;
	
	private void setupGUI()
	{
		setTitle("Contest Manager");
		setLayout(new BorderLayout());
		jtpTabs = new JTabbedPane();
		jtpTabs.add("Users", usersPanel = new UsersPanel());
		jtpTabs.add("Problems", problemsPanel = new ProblemsPanel());
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
		users = UsersModel.getAllUsers();
		usersPanel.setData(users);
		
		problems = ProblemsModel.getAllProblems();
		problemsPanel.setData(problems);
	}

	public static void main(String[] args)
	{
		new Admin();
	}
}
