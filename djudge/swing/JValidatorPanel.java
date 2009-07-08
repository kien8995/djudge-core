package djudge.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.validator.ValidatorDescription;
import djudge.judge.validator.ValidatorType;

public class JValidatorPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;

	ValidatorDescription val;
	
	JTextArea txtParam;
	
	JTextArea txtFile;
	
	JComboBox cbTypes;
	
	boolean fChanged = false;
	
	public JValidatorPanel(ValidatorDescription val)
	{
		setupGUI();
		setLimits(val);
		setBorder(BorderFactory.createTitledBorder("Validator"));
		setPreferredSize(new Dimension(250, 100));
		setVisible(true);
	}
	
	protected void setupGUI()
	{
		final Dimension sz = new Dimension(150, 20);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 5, 2, 5);
		
		cbTypes = new JComboBox(ValidatorType.values());
		cbTypes.setPreferredSize(sz);
		cbTypes.addActionListener(this);
		add(cbTypes, c);
		
		c.gridy = 1;
		txtParam = new JTextArea();
		txtParam.setPreferredSize(sz);
		txtParam.setEnabled(false);
		add(txtParam, c);
		
		c.gridy = 2;
		txtFile = new JTextArea();
		txtFile.setPreferredSize(sz);
		txtFile.setEnabled(false);
		add(txtFile, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.EAST;
		add(new JLabel("Type"), c);
		
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		add(new JLabel("Param"), c);		

		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		add(new JLabel("File"), c);				
	}
	
	
	public boolean getLimitsChanged()
	{
		return fChanged;
	}
	
	public void setLimitsChanged(boolean fChanged)
	{
		this.fChanged = fChanged;
	}
	
	public ValidatorDescription getLimits()
	{
		return val;
	}
	
	public void setLimits(ValidatorDescription limits)
	{
		this.val = limits.clone();
		fChanged = false;
		cbTypes.setSelectedItem(limits.type);
	}
	
	public void updateType()
	{
		String strType = cbTypes.getSelectedItem().toString();
		ValidatorType type = ValidatorType.parse(strType);
		txtFile.setEnabled(false);
		txtParam.setEnabled(false);
		System.out.println(type);
		if (type.isParametrized())
		{
			txtParam.setEnabled(true);
		}
		if (type.isExternal())
		{
			txtFile.setEnabled(true);
		}
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLayout(new FlowLayout());
		
		ValidatorDescription val = new ValidatorDescription();
		val.type = ValidatorType.InternalExact;
		
		frame.add(new JValidatorPanel(val), BorderLayout.CENTER);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		updateType();
		System.out.println(cbTypes.getSelectedItem() + " selected");
	}
}
