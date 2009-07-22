package djudge.acmcontester.client;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.FileWorks;

public class JSubmitPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	JComboBox jcbLanguages;
	
	JComboBox jcbProblems;

	JLabel glblLanguage;
	
	JLabel glblProblem;
	
	JTextField jtfFile;
	
	JLabel jlblFile;
	
	JButton jbtnChooseFile;
	
	JTextArea jtaSource;
	
	public JSubmitPanel()
	{
		setupGUI();
		setVisible(true);
	}
	
	private void setupGUI()
	{
		jcbLanguages = new JComboBox(/*new Object[] {"<No languages defined>"}*/Client.server.getLanguages("", ""));
		jcbLanguages.setPreferredSize(new Dimension(30, 30));
		glblLanguage = new JLabel("Language");
		
		jcbProblems = new JComboBox(/*new Object[] {"<No problems defined>"}*/Client.server.getProblems("", ""));
		jcbProblems.setPreferredSize(new Dimension(30, 30));
		glblProblem = new JLabel("Problem");
		
		jtfFile = new JTextField();
		jtfFile.setEnabled(false);
		jtfFile.setPreferredSize(new Dimension(25, 25));
		jlblFile = new JLabel("File");
		
		jbtnChooseFile = new JButton("Choose File");
		jbtnChooseFile.addActionListener(this);
		
		jtaSource = new JTextArea();
		jtaSource.setFont(new Font("Courier New", Font.PLAIN, 14));
		
		JScrollPane tScrollPane = new JScrollPane(jtaSource);
		tScrollPane.setBorder(BorderFactory.createTitledBorder("Load or Paste Your Source"));
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 7;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(10, 10, 10, 10);
		
		add(jcbProblems, c);
		
		c.gridy = 1;
		add(jcbLanguages, c);

		c.gridy = 2;
		c.gridwidth = 5;
		add(jtfFile, c);
		
		c.gridx = 8;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		add(jbtnChooseFile, c);
		

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0;
		
		add(glblProblem, c);

		c.gridy = 1;
		add(glblLanguage, c);
		
		c.gridy = 2;
		add(jlblFile, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 10;
		c.gridheight = 5;
		c.weightx = 2;
		c.weighty = 1;
		add(tScrollPane, c);
		
	}
	
	private void doChooseFile()
	{
		FileDialog fd = new FileDialog((JFrame) null, "Open source", FileDialog.LOAD);
		fd.setVisible(true);
		if (fd.getFile() != null)
		{
			String filename = fd.getDirectory() + fd.getFile();
			jtfFile.setText(filename);
			String content = FileWorks.readFile(filename);
			jtaSource.setText(content);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource().equals(jbtnChooseFile))
		{
			doChooseFile();
		}
		
	}
	
	public static void main(String[] args)
	{
		new Client();
	}

}
