package djudge.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import djudge.judge.validator.ValidatorDescription;
import djudge.judge.validator.ValidatorType;

public class JValidatorPanel extends JPanel implements ActionListener, DocumentListener
{

	private static final long serialVersionUID = 1L;

	ValidatorDescription val;
	
	JTextField txtParam;
	
	JTextField txtFile;
	
	JComboBox cbTypes;
	
	JButton jbChooseFile;
	
	boolean fChanged = false;
	
	private void setupComponent()
	{
		setupGUI();
		setBorder(BorderFactory.createTitledBorder("Validator"));
		setPreferredSize(new Dimension(250, 100));		
	}
	
	public JValidatorPanel(ValidatorDescription val)
	{
		setupComponent();
		setLimits(val);
		setVisible(true);
	}
	
	public JValidatorPanel()
	{
		setupComponent();
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
		txtParam = new JTextField();
		txtParam.setPreferredSize(sz);
		txtParam.setEnabled(false);
		txtParam.addActionListener(this);
		txtParam.getDocument().addDocumentListener(this);
		add(txtParam, c);
		
		c.gridy = 2;
		txtFile = new JTextField();
		txtFile.setPreferredSize(sz);
		txtFile.setEnabled(false);
		txtFile.addActionListener(this);
		txtFile.getDocument().addDocumentListener(this);
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
		txtFile.setText(limits.getExeName());
		txtParam.setText(limits.param);
	}
	
	public void updateType()
	{
		String strType = cbTypes.getSelectedItem().toString();
		ValidatorType type = ValidatorType.parse(strType);
		val.type = type;
		txtFile.setEnabled(false);
		txtParam.setEnabled(false);
		if (type.isParametrized())
		{
			txtParam.setEnabled(true);
		}
		if (type.isExternal())
		{
			txtFile.setEnabled(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		updateType();
	}
	
	private void updateParams()
	{
		fChanged = true;
		val.setExeName(txtFile.getText());
		val.param = txtParam.getText();
	}

	@Override
	public void changedUpdate(DocumentEvent arg0)
	{
		updateParams();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0)
	{
		updateParams();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0)
	{
		updateParams();
	}
}
