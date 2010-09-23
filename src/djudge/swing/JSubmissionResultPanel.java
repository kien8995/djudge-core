/* $Id$ */

package djudge.swing;


import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import djudge.acmcontester.admin.AdminClient;
import djudge.judge.SubmissionResult;

public class JSubmissionResultPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SubmissionResult data;
	
	void init()
	{
		setLayout(new BorderLayout());
		add(new JScrollPane(new JCompilationResultPanel(data.getCompilationInfo())), BorderLayout.NORTH);
		add(new JScrollPane(new JTestingResultPanel(data.getProblemResult())), BorderLayout.CENTER);
	}
	
	public JSubmissionResultPanel(SubmissionResult sr)
	{
		data = sr;
		init();
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}
}
