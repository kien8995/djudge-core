package djudge.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import djudge.judge.dexecutor.ExecutorLimits;

public class JLimitsPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	ExecutorLimits limits;
	
	JTextArea txtMemory;
	
	JTextArea txtTime;
	
	JTextArea txtOutput;
	
	boolean fChanged = false;
	
	public JLimitsPanel(ExecutorLimits limits)
	{
		setupGUI();
		setLimits(limits);
		setBackground(Color.RED);
		setPreferredSize(new Dimension(200, 100));
		setVisible(true);
	}
	
	protected void setupGUI()
	{
		txtTime = new JTextArea();
		txtTime.setPreferredSize(new Dimension(180, 20));
		add(txtTime);
		
		txtMemory = new JTextArea();
		txtMemory.setPreferredSize(new Dimension(180, 20));
		add(txtMemory);
		
		txtOutput = new JTextArea();
		txtOutput.setPreferredSize(new Dimension(180, 20));
		add(txtOutput);
	}
	
	public boolean getLimitsChanged()
	{
		return fChanged;
	}
	
	public void setLimitsChanged(boolean fChanged)
	{
		this.fChanged = fChanged;
	}
	
	public ExecutorLimits getLimits()
	{
		return limits;
	}
	
	public void setLimits(ExecutorLimits limits)
	{
		this.limits = limits.clone();
		fChanged = false;
		txtTime.setText("" + limits.timeLimit);
		txtMemory.setText("" + limits.memoryLimit);
		txtOutput.setText("" + limits.outputLimit);
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLayout(new BorderLayout());
		
		ExecutorLimits limits = new ExecutorLimits(1000, 2000, 3000);
		frame.add(new JLimitsPanel(limits), BorderLayout.WEST);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
