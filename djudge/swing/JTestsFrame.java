package djudge.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		setLayout(new BorderLayout());

		txtInput = new JTextArea();
		txtInput.setPreferredSize(new Dimension(200, 200));
		txtInput.setAutoscrolls(true);
		add(new JScrollPane(txtInput), BorderLayout.WEST);
		
		txtOutput = new JTextArea();
		txtOutput.setAutoscrolls(true);
		add(new JScrollPane(txtOutput), BorderLayout.CENTER);
		
		txtAnswer = new JTextArea();
		txtAnswer.setAutoscrolls(true);
		txtAnswer.setPreferredSize(new Dimension(200, 200));
		add(new JScrollPane(txtAnswer), BorderLayout.EAST);
		
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
		vTask.programOutput = txtAnswer.getText();
		vTask.testInput = pd.getJudgeInputFilepath(groupNumber, testNumber);
		vTask.testOutput = pd.getJudgeOutputFilepath(groupNumber, testNumber);
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
