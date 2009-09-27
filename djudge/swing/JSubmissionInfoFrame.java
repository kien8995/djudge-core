package djudge.swing;


import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import utils.XmlWorks;

import djudge.acmcontester.Admin;
import djudge.acmcontester.structures.SubmissionData;
import djudge.judge.SubmissionResult;

public class JSubmissionInfoFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SubmissionData data;
	
	void init(SubmissionData data)
	{
		this.data = data;
		setLayout(new BorderLayout());
		JTabbedPane jtpTabs = new JTabbedPane();
		
		jtpTabs.add("Info", new JPanel());
		jtpTabs.add("Source code", new JSourceCodePanel(new String(Base64.decodeBase64(data.sourceCode.getBytes()))));
		jtpTabs.add("Test result", new JSubmissionResultPanel(new SubmissionResult(XmlWorks.getDocumentFromString(data.xml))));	
		
		add(jtpTabs, BorderLayout.CENTER);
		setSize(640, 480);
		setVisible(true);
	}
	
	public JSubmissionInfoFrame(SubmissionData data)
	{
		init(data);
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
}