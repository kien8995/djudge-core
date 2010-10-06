/* $Id$ */

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

import djudge.judge.executor.ExecutorLimits;

public class JScoringPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	ExecutorLimits limits;
	
	JTextField txtProblemScore;
	
	JLabel jlblProblemScore;
	
	JTextField txtGroupScore;
	
	JLabel jlblGroupScore;
	
	JTextField txtTestScore;
	
	JLabel jlblTestScore;
	
	boolean fChanged = false;

	private void setupComponent()
	{
		setupGUI();
		setBorder(BorderFactory.createTitledBorder("Scoring"));
		setPreferredSize(new Dimension(250, 100));		
	}
	
	public JScoringPanel()
	{
		setupComponent();
		setVisible(true);
	}
	
	public JScoringPanel(ExecutorLimits limits)
	{
		setupComponent();
		setLimits(limits);
		setVisible(true);
	}
	
	protected void setupGUI()
	{
		final Dimension sz = new Dimension(50, 20);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 5, 2, 5);
		
		txtProblemScore = new JTextField();
		txtProblemScore.setPreferredSize(sz);
		add(txtProblemScore, c);
		
		c.gridy = 1;
		txtGroupScore = new JTextField();
		txtGroupScore.setPreferredSize(sz);
		add(txtGroupScore, c);
		
		c.gridy = 2;
		txtTestScore = new JTextField();
		txtTestScore.setPreferredSize(sz);
		add(txtTestScore, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		add(jlblProblemScore = new JLabel("Problem score"), c);
		jlblProblemScore.setLabelFor(txtProblemScore);
		jlblProblemScore.setDisplayedMnemonic('t');
		
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		add(jlblGroupScore = new JLabel("Group score"), c);
		jlblGroupScore.setLabelFor(txtGroupScore);
		jlblGroupScore.setDisplayedMnemonic('m');

		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		add(jlblTestScore = new JLabel("Test score"), c);
		jlblTestScore.setLabelFor(txtTestScore);
		jlblTestScore.setDisplayedMnemonic('o');
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
		txtProblemScore.setText("" + limits.timeLimit);
		txtGroupScore.setText("" + limits.memoryLimit);
		txtTestScore.setText("" + limits.outputLimit);
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLayout(new FlowLayout());
		
		ExecutorLimits limits = new ExecutorLimits(1000, 2000, 3000);
		frame.add(new JScoringPanel(limits), BorderLayout.CENTER);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
