package djudge.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		setBorder(BorderFactory.createTitledBorder("Runtime Limits"));
		setPreferredSize(new Dimension(200, 100));
		setVisible(true);
	}
	
	protected void setupGUI()
	{
		final Dimension sz = new Dimension(50, 20);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 5, 2, 5);
		
		txtTime = new JTextArea();
		txtTime.setPreferredSize(sz);
		add(txtTime, c);
		
		c.gridy = 1;
		txtMemory = new JTextArea();
		txtMemory.setPreferredSize(sz);
		add(txtMemory, c);
		
		c.gridy = 2;
		txtOutput = new JTextArea();
		txtOutput.setPreferredSize(sz);
		add(txtOutput, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		add(new JLabel("Time Limit"), c);
		
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		add(new JLabel("Memory Limit"), c);		

		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		add(new JLabel("Output Limit"), c);				
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
		frame.setLayout(new FlowLayout());
		
		ExecutorLimits limits = new ExecutorLimits(1000, 2000, 3000);
		frame.add(new JLimitsPanel(limits), BorderLayout.CENTER);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
