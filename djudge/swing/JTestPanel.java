package djudge.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.validator.ValidatorDescription;

public class JTestPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	ExecutorLimits limits;
	
	ValidatorDescription val;
	
	JValidatorPanel jvpValidator;
	
	JLimitsPanel jlpLimits;
	
	public void setData(ExecutorLimits newLimits, ValidatorDescription newValidator)
	{
		limits = newLimits;
		val = newValidator;
	}
	
	public JTestPanel()
	{
		setupGUI();
		setData(limits = new ExecutorLimits(), val = new ValidatorDescription());
		setBorder(BorderFactory.createTitledBorder("Test Info"));
		setPreferredSize(new Dimension(400, 400));
		setVisible(true);
	}
	
	protected void setupGUI()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 5, 2, 5);
		
		add(jlpLimits = new JLimitsPanel(), c);
		
		c.gridy = 2;
		add(jvpValidator = new JValidatorPanel(), c);
		
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLayout(new FlowLayout());
		
		frame.add(new JTestPanel(), BorderLayout.CENTER);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setValidator(ValidatorDescription val)
	{
		jvpValidator.setLimits(val);
	}
	
	public void setLimits(ExecutorLimits newLimits)
	{
		jlpLimits.setLimits(newLimits);
	}
}
