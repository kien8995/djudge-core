package djudge.swing;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import djudge.acmcontester.admin.AdminClient;
import djudge.judge.SubmissionResult;

public class JSubmissionResultFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SubmissionResult data;
	
	void init(SubmissionResult data)
	{
		this.data = data;
		setLayout(new BorderLayout());
		add(new JScrollPane(new JSubmissionResultPanel(data)), BorderLayout.CENTER);
		setSize(640, 480);
		setVisible(true);
	}
	
	public JSubmissionResultFrame(SubmissionResult data)
	{
		init(data);
	}
	
	public JSubmissionResultFrame(SubmissionResult data, String caption)
	{
		init(data);
		setTitle(caption);
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}
}
