package djudge.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import djudge.judge.AbstractDescription;

public class JBlockInfoPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	JValidatorPanel jvpValidator;
	
	JLimitsPanel jlpLimits;
	
	JFileMaskPanel jfmpMasks;
	
	AbstractDescription desc;
	
	public void setData(AbstractDescription desc)
	{
		this.desc = desc;
		jfmpMasks.setData(desc);
	}
	
	public JBlockInfoPanel()
	{
		setupGUI();
		setBorder(BorderFactory.createTitledBorder("Block Info"));
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
		
		c.gridy = 4;
		add(jfmpMasks = new JFileMaskPanel(), c);
		
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLayout(new FlowLayout());
		
		frame.add(new JBlockInfoPanel(), BorderLayout.CENTER);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
