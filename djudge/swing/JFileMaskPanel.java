package djudge.swing;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;

public class JFileMaskPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	String inputMask;
	
	String outputMask;
	
	JTextField txtInput;
	
	JTextField txtOutput;
	
	JLabel jlblInput;
	
	JLabel jlblOutput;
	
	boolean fChanged = false;

	private void setupComponent()
	{
		setupGUI();
		setBorder(BorderFactory.createTitledBorder("Files"));
		setPreferredSize(new Dimension(250, 100));		
	}
	
	public JFileMaskPanel()
	{
		setupComponent();
		setVisible(true);
	}
	
	public JFileMaskPanel(String inputMask, String outputMask)
	{
		setupComponent();
		setData(inputMask, outputMask);
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
		
		txtInput = new JTextField();
		txtInput.setPreferredSize(sz);
		add(txtInput, c);
		
		c.gridy = 1;
		txtOutput = new JTextField();
		txtOutput.setPreferredSize(sz);
		add(txtOutput, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		add(jlblInput = new JLabel("Input File(s)"), c);
		jlblInput.setLabelFor(txtInput);
		jlblInput.setDisplayedMnemonic('t');
		
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		add(jlblOutput = new JLabel("Output File(s)"), c);
		jlblOutput.setLabelFor(txtOutput);
		jlblOutput.setDisplayedMnemonic('m');
	}
	
	
	public void setData(String inputMask, String outputMask)
	{
		this.inputMask = inputMask;
		this.outputMask = outputMask;
		fChanged = false;
		txtInput.setText(inputMask);
		txtOutput.setText(outputMask);
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLayout(new FlowLayout());
		
		frame.add(new JFileMaskPanel("input", "output"), BorderLayout.CENTER);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
