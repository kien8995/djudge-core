package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import db.AbstractTableDataModel;
import db.ProblemsDataModel;
import db.UsersDataModel;

public class Admin extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JTabbedPane jtpTabs;
	
	private UsersPanel usersPanel;

	private ProblemsPanel problemsPanel;
	
	private AbstractTableDataModel usersModel;
	
	private AbstractTableDataModel problemsModel;
	
	private void setupGUI()
	{
		usersModel = new UsersDataModel();
		usersModel.fill();
		
		problemsModel = new ProblemsDataModel();
		problemsModel.fill();
		
		setTitle("Contest Manager");
		setLayout(new BorderLayout());
		jtpTabs = new JTabbedPane();
		
		jtpTabs.add("Users", usersPanel = new UsersPanel(usersModel));
		
		jtpTabs.add("Problems", problemsPanel = new ProblemsPanel(problemsModel));
		
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
