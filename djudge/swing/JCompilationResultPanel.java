package djudge.swing;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import djudge.acmcontester.Admin;
import djudge.judge.dcompiler.CompilationInfo;
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
		
	}
	
	public JCompilationResultPanel(CompilerResult sr)
	{
		setBorder(BorderFactory.createTitledBorder("Compilation details"));
		data = sr;
		init();
	}
		
	public static void main(String[] args)
	{
		new Admin();
	}
}
