/* $Id$ */

package djudge.acmcontester;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import djudge.acmcontester.admin.AdminClient;
import djudge.acmcontester.server.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.server.interfaces.TeamXmlRpcInterface;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.ProblemData;
import djudge.utils.xmlrpc.HashMapSerializer;

import utils.FileWorks;

public class JTestPanel extends JPanel implements ActionListener, Updateble
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
	
	JButton jbtnSubmit;
	
	TeamXmlRpcInterface serverInterface;
	
	AuthentificationDataProvider authProvider;
	
	public JTestPanel(TeamXmlRpcInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		this.serverInterface = serverInterface;
		this.authProvider = authProvider;
		setupGUI();
		setVisible(true);
	}
	
	private void setupGUI()
	{
		jcbLanguages = new JComboBox(HashMapSerializer
				.deserializeFromHashMapArray(serverInterface.getTeamLanguages(
						authProvider.getUsername(), authProvider.getPassword()), LanguageData.class));
		jcbLanguages.setPreferredSize(new Dimension(30, 30));
		glblLanguage = new JLabel("Language");
		
		jcbProblems = new JComboBox(HashMapSerializer
				.deserializeFromHashMapArray(serverInterface.getTeamProblems(
						authProvider.getUsername(), authProvider.getPassword()), ProblemData.class));
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
		
		jbtnSubmit = new JButton("Submit");
		jbtnSubmit.addActionListener(this);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 7;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(10, 10, 5, 5);
		
		add(jcbProblems, c);
		
		c.gridy = 1;
		add(jcbLanguages, c);

		c.gridy = 2;
		c.gridwidth = 3;
		add(jtfFile, c);
		
		c.gridx = 6;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		add(jbtnChooseFile, c);
		
		c.gridx = 8;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		add(jbtnSubmit, c);
		

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
		c.gridheight = 7;
		c.weightx = 2;
		c.weighty = 1;
		add(tScrollPane, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 9;
		c.gridy = 10;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		//add(jbtnSubmit, c);
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
	
	private void doSubmitSolution()
	{
		LanguageData ld = (LanguageData) jcbLanguages.getSelectedItem();
		ProblemData pd = (ProblemData) jcbProblems.getSelectedItem();
		if (JOptionPane.showConfirmDialog(this,
				"Do You Really Want to Test This Solution for\nProblem "
						+ jcbProblems.getSelectedItem() + "\nin Language"
						+ jcbLanguages.getSelectedItem() + "?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
		{
			if (serverInterface.testSolution(authProvider.getUsername(), authProvider.getPassword(), pd.id, ld.id, jtaSource.getText()))
			{
				JOptionPane.showMessageDialog(this, "Submitted Ok");
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Error occured");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object src = arg0.getSource(); 
		if (src.equals(jbtnChooseFile))
		{
			doChooseFile();
		}
		else if (src.equals(jbtnSubmit))
		{
			doSubmitSolution();
		}
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}

	@Override
	public boolean updateState()
	{
		// TODO implement me
		return false;
	}

}
