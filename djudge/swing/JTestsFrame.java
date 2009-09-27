package djudge.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utils.FileWorks;
import djudge.judge.ProblemDescription;
import djudge.judge.dvalidator.LocalValidator;
import djudge.judge.dvalidator.ValidatorTask;
import djudge.judge.validator.ValidationResult;
import djudge.judge.validator.ValidationResultEnum;

public class JTestsFrame extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	ProblemDescription pd;
	
	int groupNumber;
	
	int testNumber;
	
	JTextArea txtInput;
	
	JTextArea txtOutput;
	
	JTextArea txtAnswer;
	
	protected JMenu getInputMenu()
	{
		JMenu menu;
		JMenuItem menuItem;
		
		menu = new JMenu("Input file");
		
		menuItem = new JMenuItem("");
		menuItem.setActionCommand("close_window");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		return menu;
	}
	
	protected JMenu getOutputMenu()
	{
		JMenu menu;
		JMenuItem menuItem;
		
		menu = new JMenu("Output file");
		
		menuItem = new JMenuItem("");
		menuItem.setActionCommand("RTF");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		return menu;
	}
	
	protected JMenu getAnswerMenu()
	{
		JMenu menu;
		JMenuItem menuItem;
		
		menu = new JMenu("Answer file");
		
		menuItem = new JMenuItem("Test answer");
		menuItem.setActionCommand("TEST_ANSWER");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		return menu;
	}
	
	protected JMenu getActionMenu()
	{
		JMenu menu;
		JMenuItem menuItem;
		
		menu = new JMenu("Actions");
		
		menuItem = new JMenuItem("");
		menuItem.setActionCommand("RTF");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		return menu;
	}
	
	protected JMenuBar getMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.add(getInputMenu());
		menuBar.add(getOutputMenu());
		menuBar.add(getAnswerMenu());
		menuBar.add(getActionMenu());
		
		return menuBar;
	}	
	
	protected void loadData()
	{
		String input = FileWorks.readFile(pd.getJudgeInputFilepath(groupNumber, testNumber));
		String output = FileWorks.readFile(pd.getJudgeOutputFilepath(groupNumber, testNumber));
		txtInput.setText(input);
		txtOutput.setText(output);
	}
	
	public JTestsFrame(ProblemDescription pd, int groupNumber, int testNumber)
	{
		this.pd = pd;
		this.groupNumber = groupNumber;
		this.testNumber = testNumber;
		setSize(640, 480);
		setJMenuBar(getMenu());
		
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 2, 2);

		JScrollPane pane;
		
		txtInput = new JTextArea();
		pane = new JScrollPane(txtInput);
		pane.setBorder(BorderFactory.createTitledBorder("Input File"));
		add(pane, c);
		
		c.gridx = 1;
		txtOutput = new JTextArea();
		pane = new JScrollPane(txtOutput);
		pane.setBorder(BorderFactory.createTitledBorder("Output File"));
		add(pane, c);
		
		c.gridx = 2;
		txtAnswer = new JTextArea();
		pane = new JScrollPane(txtAnswer);
		pane.setBorder(BorderFactory.createTitledBorder("Answer File"));
		add(pane, c);
		
		loadData();
		
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		try
		{
			JTestsFrame fr = new JTestsFrame(new ProblemDescription("NEERC", "A"), 0, 0);
			fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void doTestAnswer()
	{
		ValidatorTask vTask = new ValidatorTask();
		vTask.contestId = pd.getContestID();
		vTask.problemId = pd.getProblemID();
		vTask.groupNumber = groupNumber;
		vTask.testNumber = testNumber;
		//TODO: rewrite
		/*vTask.programOutput = txtAnswer.getText();
		vTask.testInput = pd.getJudgeInputFilepath(groupNumber, testNumber);
		vTask.testOutput = pd.getJudgeOutputFilepath(groupNumber, testNumber);*/
		//throw new Exception();
		ValidationResult vRes = LocalValidator.validate(vTask);
		String msg = "<html><h2 align='center'>" + vRes.Result + "</h2>\n";
		msg += "Validator: " + vRes.ValidatorName + "\n";
		msg += "Validator's output: \n";
		for (int i = 0; i < vRes.ValidatorOutput.length; i++)
		{
			msg += vRes.ValidatorOutput[i] + "\n";
		}
		JOptionPane.showMessageDialog(this, msg, "Result",
				vRes.Result == ValidationResultEnum.OK ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String ac = arg0.getActionCommand();
		if ("TEST_ANSWER".equalsIgnoreCase(ac))
		{
			doTestAnswer();
		}
		
	}
}
