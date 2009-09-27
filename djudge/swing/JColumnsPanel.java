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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import djudge.judge.AbstractDescription;
import djudge.judge.validator.ValidatorDescription;
import djudge.judge.validator.ValidatorType;

public class JColumnsPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	Object[][] data;
	
	public JColumnsPanel(Object[][] data)
	{
		this.data = data;
		setupGUI();
		setVisible(true);
	}

	protected void setupGUI()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(2, 5, 2, 5);
		
		int rows = data.length;
		int columns = data != null && data[0] != null ? data[0].length : 0;
		
		for (int i = 0; i < rows; i++)
		{
			c.gridy = i;
			for (int j = 0; j < columns; j++)
			{
				c.gridx = j;
				add(new JLabel(data[i][j].toString()), c);
			}
		}
		setPreferredSize(new Dimension(500, 500));
	}
	
	public static void main(String[] args)
	{
		Object[][] data = {
				{"0444444444440", "01"},
				{"10", "11"},
				{"20", "21"},
		};
		
		JFrame frame = new JFrame();
		frame.add(new JColumnsPanel(data));
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
