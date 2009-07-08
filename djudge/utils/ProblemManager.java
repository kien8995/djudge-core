package djudge.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import djudge.exceptions.DJudgeXmlCorruptedException;
import djudge.exceptions.DJudgeXmlNotFoundException;

class ProblemDescription
{
	String contestId = "";
	String problemId = "";
	String name = "";
	ProblemStatus status = ProblemStatus.OK;
	int testsCount;
	int groupsCount;
	
	public ProblemDescription()
	{
		
	}
	
	public ProblemDescription(String contestId, String problemId)
	{
		this.contestId = contestId;
		this.problemId = problemId;
	}
	
	public Object getFieldByNumber(int num)
	{
		switch (num)
		{
		case 0: return contestId;
		case 1: return problemId;
		case 2: return name;
		case 3: return groupsCount;
		case 4: return testsCount;
		case 5: return status;
		}
		return "";
	}
}

enum ProblemStatus
{
	OK,
	CORRUPTED_XML,
	CORRUPTED_TESTS,
	NOT_A_PROBLEM,
	INTERNAL_ERROR,
}

class ProblemScanner
{
	public static Set<ProblemDescription> pr;
	
	public static ProblemDescription[] scanProblems()
	{
		pr = new HashSet<ProblemDescription>();
		File[] dirs = new File("./problems/").listFiles();
		for (File dir : dirs)
		{
			if (dir.isDirectory())
			{
				scanDirectory(dir, dir.getName());
			}
		}
		
		return pr.toArray(new ProblemDescription[0]);
	}
	
	public static void scanDirectory(File contestDir, String contestId)
	{
		File[] dirs = contestDir.listFiles();
		for (File dir : dirs)
		{
			if (dir.isDirectory())
			{
				scanProblem(dir, contestId, dir.getName());
			}
		}
	}
	
	public static void scanProblem(File problemDir, String contestId, String problemId)
	{
		ProblemDescription desc = new ProblemDescription(contestId, problemId);
		try
		{
			djudge.judge.ProblemDescription pd = new djudge.judge.ProblemDescription(contestId, problemId);
			desc.groupsCount = pd.getGroupsCount();
			desc.testsCount = pd.getTestsCount();
			desc.name = pd.problemInfo.getName();
		} catch (DJudgeXmlNotFoundException e)
		{
			desc.status = ProblemStatus.NOT_A_PROBLEM;
			//System.out.println("Not a problem's directory: " + problemDir.toString());
		} catch (DJudgeXmlCorruptedException e)
		{
			desc.status = ProblemStatus.CORRUPTED_XML;
			//System.out.println("Corrupted XML: " + problemDir);
		}
		catch (Exception e)
		{
			desc.status = ProblemStatus.INTERNAL_ERROR;
			e.printStackTrace();
		}
		
		if (desc.status != ProblemStatus.NOT_A_PROBLEM)
		{
			pr.add(desc);
			//System.out.println("Added: " + desc.contestId + "." + desc.problemId + ": '" + desc.name + "'. Status: " + desc.status);
		}
	}
}

public class ProblemManager extends JFrame implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;
	
	private ProblemDescription[] problems;
	
	class ColorRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			setText(value.toString());
			ProblemStatus status = (ProblemStatus) value;
			switch (status)
			{
			case OK:
				setBackground(Color.GREEN);
				break;

			case CORRUPTED_TESTS:
				setBackground(Color.YELLOW);
				break;
				
			case CORRUPTED_XML:
				setBackground(Color.YELLOW);
				break;
				
			default:
				setBackground(Color.RED);
				break;
			}
			
			return this;
		}

		public void setValue(Object value)
		{
			setText(value.toString());
		}
	}	
	
	class TableModel implements javax.swing.table.TableModel
	{
		final String[] columnNames = {
				"Contest",
				"Problem",
				"Name",
				"Groups",
				"Tests",
				"Status"
			};

		@Override
		public Class<?> getColumnClass(int arg0)
		{
			return getValueAt(0, arg0).getClass();
		}

		@Override
		public int getColumnCount()
		{
			return columnNames.length;
		}

		@Override
		public String getColumnName(int arg0)
		{
			return columnNames[arg0];
		}

		@Override
		public int getRowCount()
		{
			return problems.length;
		}

		@Override
		public Object getValueAt(int arg0, int arg1)
		{
			return problems[arg0].getFieldByNumber(arg1);
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1)
		{
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {}

		@Override
		public void setValueAt(Object arg0, int arg1, int arg2) {}
		
		@Override
		public void addTableModelListener(TableModelListener arg0) {}
	}
	
	public ProblemManager()
	{
		problems = ProblemScanner.scanProblems();
		setSize(640, 480);
		setLayout(new BorderLayout());
		
		table = new JTable(new TableModel());
		table.setAutoCreateRowSorter(true);
		table.setRowHeight(20);
		table.addMouseListener(this);
		TableColumnModel cm = table.getColumnModel();
		cm.getColumn(table.getColumnCount()-1).setCellRenderer(new ColorRenderer());
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	public static void main(String[] args)
	{
		ProblemManager wnd = new ProblemManager();
		wnd.setVisible(true);
		wnd.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			JTable target = (JTable) e.getSource();
			int row = target.getSelectedRow();
			String contestId = table.getValueAt(row, 0).toString();
			String problemId = table.getValueAt(row, 1).toString();
			System.out.println(contestId + " - " + problemId);
			new ProblemEditor(contestId, problemId);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
