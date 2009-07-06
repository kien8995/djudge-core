package djudge.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import djudge.judge.GroupDescription;
import djudge.judge.ProblemDescription;
import djudge.judge.dexecutor.ExecutorFiles;
import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.validator.Validator;
import djudge.swing.JLimitsPanel;
import djudge.swing.JTestsFrame;

public class ProblemEditor extends JFrame implements TreeSelectionListener, ActionListener
{

	private static final long serialVersionUID = 1L;
	
	String problemId;
	
	String contestId;
	
	JTree tree;
	
	JScrollPane treePane;
	
	JScrollPane dataPane;
	
	djudge.judge.ProblemDescription pd;
	
	JButton btnViewTests;
	
	JButton btnTestSolution;
	
	Hashtable<String, Hashtable<String, String[]>> getTree()
	{
		Hashtable<String, Hashtable<String, String[]>> res = new Hashtable<String, Hashtable<String, String[]>>();
		Hashtable<String, String[]> groupIds = new Hashtable<String,String[]>();
		for (int i = 0; i < pd.getGroupsCount(); i++)
		{
			GroupDescription group = pd.getGroup(i);
			String[] testIds = new String[group.getTestCount()];
			for (int j = 0; j < group.getTestCount(); j++)
			{
				testIds[j] = "Test #" + (i + 1) + "." + (j + 1); 
			}
			groupIds.put("Group #" + (i + 1), testIds);
		}
		res.put("All tests", groupIds);
		return res;
	}
	
	void reloadData()
	{
		if (treePane != null)
		{
			treePane.removeAll();
		}
		tree = new JTree(getTree());
		tree.setPreferredSize(new Dimension(200, 200));
		tree.addTreeSelectionListener(this);
		add(treePane = new JScrollPane(tree), BorderLayout.WEST);		
		
		JPanel spSouth = new JPanel();
		spSouth.setVisible(true);
		
		btnViewTests = new JButton("View Tests");
		btnViewTests.addActionListener(this);
		//btnViewTests.setPreferredSize(new Dimension(100, 25));
		spSouth.add(btnViewTests);
		
		btnTestSolution = new JButton("Test Solution");
		btnTestSolution.addActionListener(this);
		//btnTestSolution.setPreferredSize(new Dimension(100, 25));
		spSouth.add(btnTestSolution);
		
		add(spSouth, BorderLayout.SOUTH);
		
		showProblemInfo();
	}
	
	public ProblemEditor(String contestId, String problemId)
	{
		this.problemId = problemId;
		this.contestId = contestId;
		try
		{
			pd = new ProblemDescription(contestId, problemId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		setSize(640, 480);
		setLayout(new BorderLayout());
		reloadData();
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		ProblemEditor pe = new ProblemEditor("univ-2009-stdio", "E");
		pe.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	ExecutorFiles files;
	ExecutorLimits limits;
	Validator val;
	
	public void showData()
	{
/*		if (dataPane != null)
		{
			dataPane.setVisible(false);
			dataPane.removeAll();
			remove(dataPane);
		}
		dataPane = new JScrollPane();
		dataPane.add(new JLimitsPanel(limits));
		dataPane.setPreferredSize(new Dimension(400, 400));
		add(dataPane, BorderLayout.CENTER);*/
		//dataPane.setVisible(true);
		add(new JLimitsPanel(limits), BorderLayout.CENTER);
		setVisible(true);
	}
	
	public void showProblemInfo()
	{
		files = pd.getFiles();
		limits = pd.getLimits();
		val = pd.getValidator();
		showData();
	}
	
	public void showGroupInfo()
	{
		
	}
	
	public void showTestInfo()
	{
		
	}

	Object[] treePath;
	
	@Override
	public void valueChanged(TreeSelectionEvent arg0)
	{
		TreePath path = arg0.getNewLeadSelectionPath();
		treePath = path.getPath();
		int pathLength = treePath.length;
		if (pathLength == 2)
		{
			showProblemInfo();
		}
		else if (pathLength == 3)
		{
			showProblemInfo();
		}
		else if (pathLength == 4)
		{
			showProblemInfo();
		}
	}

	private void actionShowTests()
	{
		if (treePath == null || treePath.length != 4) return;
		String str = treePath[3].toString();
		int groupNumber = Integer.parseInt(str.substring(6, str.lastIndexOf('.'))) - 1;
		int testNumber = Integer.parseInt(str.substring(str.lastIndexOf('.') + 1)) - 1;
		new JTestsFrame(pd, groupNumber, testNumber);
	}
	
	private void actionTestSolution()
	{
		new SolutionTester(pd);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object source = arg0.getSource();
		if (source.equals(btnViewTests))
		{
			actionShowTests();
		}
		if (source.equals(btnTestSolution))
		{
			actionTestSolution();
		}
	}
}
