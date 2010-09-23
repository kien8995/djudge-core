/* $Id$ */

package djudge.swing;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import djudge.acmcontester.admin.AdminClient;
import djudge.judge.dcompiler.CompilerResult;

public class JCompilationResultPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CompilerResult data;
	

	private void init()
	{
		add(new JLabel());
	}
	
	public JCompilationResultPanel(CompilerResult sr)
	{
		setBorder(BorderFactory.createTitledBorder("Compilation details"));
		data = sr;
		init();
	}
		
	public static void main(String[] args)
	{
		new AdminClient();
	}
}
