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

import javax.swing.JTextField;

import djudge.judge.dexecutor.ExecutorLimits;

public class JLimitsPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	ExecutorLimits limits;
	
	JTextField txtMemory;
	
	JTextField txtTime;
	
	JTextField txtOutput;
	
	JLabel jlblTime;
	
	JLabel jlblMemory;
	
	JLabel jlblOutput;
	
	boolean fChanged = false;

	private void setupComponent()
	{
		setupGUI();
		setBorder(BorderFactory.createTitledBorder("Runtime Limits"));
		setPreferredSize(new Dimension(250, 100));		
	}
	
	public JLimitsPanel()
	{
		setupComponent();
		setVisible(true);
	}
	
	public JLimitsPanel(ExecutorLimits limits)
	{
		setupComponent();
		setLimits(limits);
		setVisible(true);
	}
	
	protected void setupGUI()
	{
		final Dimension sz = new Dimension(120, 20);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 5, 2, 5);
		
		txtTime = new JTextField();
		txtTime.setPreferredSize(sz);
		add(txtTime, c);
		
		c.gridy = 1;
		txtMemory = new JTextField();
		txtMemory.setPreferredSize(sz);
		add(txtMemory, c);
		
		c.gridy = 2;
		txtOutput = new JTextField();
		txtOutput.setPreferredSize(sz);
		add(txtOutput, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		add(jlblTime = new JLabel("Time Limit"), c);
		jlblTime.setLabelFor(txtTime);
		jlblTime.setDisplayedMnemonic('t');
		
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		add(jlblMemory = new JLabel("Memory Limit"), c);
		jlblMemory.setLabelFor(txtMemory);
		jlblMemory.setDisplayedMnemonic('m');

		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		add(jlblOutput = new JLabel("Output Limit"), c);
		jlblOutput.setLabelFor(txtOutput);
		jlblOutput.setDisplayedMnemonic('o');
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
