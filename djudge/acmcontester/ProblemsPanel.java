package djudge.acmcontester;

import java.util.Vector;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import djudge.acmcontester.models.ProblemsModel;
import djudge.acmcontester.structures.ProblemDescription;
import djudge.acmcontester.structures.UserDescription;

public class ProblemsPanel extends JPanel implements TableModel
{

	private static final long serialVersionUID = 1L;
	
	private JTable jtProblems;
	
	private Vector<ProblemDescription> problems;
	
	public ProblemsPanel()
	{
		problems = new Vector<ProblemDescription>();
		setLayout(new BorderLayout());
		jtProblems = new JTable(this);
		jtProblems.setRowHeight(20);		
		add(new JScrollPane(jtProblems), BorderLayout.CENTER);
		setVisible(true);
	}
	
	public void setData(Vector<ProblemDescription> problems)
	{
		this.problems = problems;
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {}

	@Override
	public Class<?> getColumnClass(int arg0)
	{
		return String.class;
	}

	@Override
	public int getColumnCount()
	{
		return UserDescription.names.length;
	}

	@Override
	public String getColumnName(int arg0)
	{
		return UserDescription.names[arg0];
	}

	@Override
	public int getRowCount()
	{
		return problems.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1)
	{
		return problems.get(arg0).getColumnValue(arg1);
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1)
	{
		return arg1 > 0;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2)
	{
		ProblemDescription ud = problems.get(arg1);
		ud.setColumnValue(arg2, arg0.toString());
		ProblemsModel.updateData(ud);
	}
	
}
