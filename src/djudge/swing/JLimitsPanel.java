/* $Id$ */

package djudge.swing;

import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;

import djudge.judge.AbstractDescription;
import djudge.judge.executor.ExecutorLimits;

public class JLimitsPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;

	AbstractDescription desc;
	
	JTextField txtMemory;
	
	JTextField txtTime;
	
	JTextField txtOutput;
	
	JLabel jlblTime;
	
	JLabel jlblMemory;
	
	JLabel jlblOutput;
	
	JButton jbtnSave;
	
	private void setupComponent()
	{
		setupGUI();
		setBorder(BorderFactory.createTitledBorder("Runtime Limits"));
		setPreferredSize(new Dimension(250, 140));		
	}
	
	public JLimitsPanel()
	{
		setupComponent();
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
		
		c.gridy = 3;
		jbtnSave = new JButton("Save");
		jbtnSave.addActionListener(this);
		add(jbtnSave, c);
		
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
	
	
	public void setData(AbstractDescription desc)
	{
		this.desc = desc;
		ExecutorLimits limits = desc.getWorkLimits();
		txtTime.setText("" + limits.timeLimit);
		txtMemory.setText("" + limits.memoryLimit);
		txtOutput.setText("" + limits.outputLimit);
	}
	
	private void saveData()
	{
		ExecutorLimits newLimits = new ExecutorLimits(
				Integer.parseInt(txtTime.getText()),
				Integer.parseInt(txtMemory.getText()),
				Integer.parseInt(txtOutput.getText())
				);
		if (!newLimits.equals(desc.getWorkLimits()))
		{
			desc.setLimits(newLimits);
		}
	}
	
	

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource().equals(jbtnSave))
		{
			saveData();
		}
	}
	
}
