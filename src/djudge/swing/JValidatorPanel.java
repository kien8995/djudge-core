/* $Id$ */

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

import djudge.judge.AbstractDescription;
import djudge.judge.checker.CheckerDescription;
import djudge.judge.checker.CheckerTypeEnum;

public class JValidatorPanel extends JPanel implements ActionListener, DocumentListener
{

	private static final long serialVersionUID = 1L;

	AbstractDescription desc;
	
	JTextField txtParam;
	
	JTextField txtFile;
	
	JComboBox cbTypes;
	
	JButton jbChooseFile;
	
	JButton jbSave;
	
	private void setupComponent()
	{
		setupGUI();
		setBorder(BorderFactory.createTitledBorder("Validator"));
		setPreferredSize(new Dimension(250, 140));		
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
		
		cbTypes = new JComboBox(CheckerTypeEnum.values());
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
		
		c.gridy = 3;
		jbSave = new JButton("Save");
		jbSave.addActionListener(this);
		add(jbSave, c);
		
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
		
	public void setData(AbstractDescription desc)
	{
		this.desc = desc;
		CheckerDescription limits = desc.getWorkValidator();
		cbTypes.setSelectedItem(limits.type);
		txtFile.setText(limits.getExeName());
		txtParam.setText(limits.param);
	}
	
	public void updateType()
	{
		String strType = cbTypes.getSelectedItem().toString();
		CheckerTypeEnum type = CheckerTypeEnum.parse(strType);
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
	
	private void saveData()
	{
		String strType = cbTypes.getSelectedItem().toString();
		CheckerTypeEnum type = CheckerTypeEnum.parse(strType);
		CheckerDescription vd = new CheckerDescription(desc.getContestID(),
				desc.getProblemID(), type, txtParam.getText(), txtFile.getText());
		if (!vd.equals(desc.getWorkValidator()))
		{
			desc.setValidator(vd);
		}
	}
	
	private void updateParams()
	{
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource().equals(cbTypes))
		{
			updateType();
		}
		else if (arg0.getSource().equals(jbSave))
		{
			saveData();
		}
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
