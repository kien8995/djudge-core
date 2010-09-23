/* $Id$ */

package djudge.acmcontester;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import db.AbstractTableDataModel;

public class ProblemsPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	private JTable jtProblems;
	
	AbstractTableDataModel modelProblems;
	
	public ProblemsPanel(AbstractTableDataModel atdm)
	{
		modelProblems = atdm;
		setLayout(new BorderLayout());
		jtProblems = new JTable(atdm);
		jtProblems.setRowHeight(20);		
		add(new JScrollPane(jtProblems), BorderLayout.CENTER);
		setVisible(true);
	}
	
}
