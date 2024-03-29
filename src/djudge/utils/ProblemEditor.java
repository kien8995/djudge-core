/* $Id$ */

package djudge.utils;

import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import djudge.judge.AbstractDescription;
import djudge.judge.GroupDescription;
import djudge.judge.ProblemDescription;
import djudge.swing.JLimitsPanel;
import djudge.swing.JBlockInfoPanel;
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
	
	JButton btnSaveDescription;
	
	JSplitPane spSplit;
	
	JBlockInfoPanel jtpTest;
	
	AbstractDescription currentBlock;
	
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
	
	JLimitsPanel jlpLimits;
	
	private void setupGUI()
	{
		GridBagConstraints c = new GridBagConstraints();
		
		treePane = new JScrollPane();
		treePane.setBorder(BorderFactory.createTitledBorder("Tests tree"));
		treePane.setMinimumSize(new Dimension(200, 200));
		treePane.setPreferredSize(new Dimension(250, 250));
		
		dataPane = new JScrollPane(jtpTest = new JBlockInfoPanel());
		dataPane.setMinimumSize(new Dimension(200, 200));
		
		spSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, dataPane);
		spSplit.setOneTouchExpandable(true);
		spSplit.setDividerLocation(200);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 10;
		c.gridheight = 9;
		c.weightx = c.weighty = 1;
		add(spSplit, c);

		//* Buttons 
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 3;
		c.gridheight = 1;
		
		btnViewTests = new JButton("View Tests");
		btnViewTests.addActionListener(this);
		add(btnViewTests, c);
		
		c.gridx = 3;
		btnTestSolution = new JButton("Test Solution");
		btnTestSolution.addActionListener(this);
		add(btnTestSolution, c);
		
		c.gridx = 6;
		btnSaveDescription = new JButton("Save");
		btnSaveDescription.addActionListener(this);
		add(btnSaveDescription, c);
	}
	
	void loadTree()
	{
		tree = new JTree(getTree());
		tree.addTreeSelectionListener(this);
		spSplit.setLeftComponent(treePane = new JScrollPane(tree));
	}
	
	public ProblemEditor(String contestId, String problemId)
	{
		this.problemId = problemId;
		this.contestId = contestId;
		try
		{
			pd = new ProblemDescription(contestId, problemId);
			pd.saveXML("d:\\1.xml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		setTitle(pd.getName());
		setSize(640, 480);
		setLayout(new GridBagLayout());
		setupGUI();
		loadTree();
		setVisible(true);
	}
	
	public void showData()
	{
		jtpTest.setData(currentBlock);
	}
	
	public void showProblemInfo()
	{
		currentBlock = pd;
		showData();
	}
	
	public void showGroupInfo()
	{
		String str = treePath[2].toString();
		int groupNumber = Integer.parseInt(str.substring(7)) - 1;
		currentBlock = pd.getGroup(groupNumber);		
		showData();
	}
	
	public void showTestInfo()
	{
		String str = treePath[3].toString();
		int groupNumber = Integer.parseInt(str.substring(6, str.lastIndexOf('.'))) - 1;
		int testNumber = Integer.parseInt(str.substring(str.lastIndexOf('.') + 1)) - 1;
		currentBlock = pd.getGroup(groupNumber).getTest(testNumber);
		showData();
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
			showGroupInfo();
		}
		else if (pathLength == 4)
		{
			showTestInfo();
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
	
	private void actionSaveDescription()
	{
		pd.save();
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
		if (source.equals(btnSaveDescription))
		{
			actionSaveDescription();
		}
	}
	
	public static void main(String[] args)
	{
		ProblemEditor pe = new ProblemEditor("NEERC-1998", "G");
		pe.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}	
}
