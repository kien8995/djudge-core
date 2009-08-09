package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import djudge.acmcontester.structures.SubmissionData;
import djudge.common.HashMapSerializer;

public class JRunsPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	JTable jtRuns;
	
	JPanel jpButtons;
	
	JButton jbtnRefresh;

	Vector<SubmissionData> data = new Vector<SubmissionData>();

	class TableRunsDataModel extends AbstractTableModel
	{

		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount()
		{
			return SubmissionData.fieldNames.length;
		}

		@Override
		public int getRowCount()
		{
			return data.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1)
		{
			return data.get(arg0).getField(arg1);
		}

		@Override
		public String getColumnName(int arg0)
		{
			switch (arg0)
			{
			case 0:
				return "ID";

			case 1:
				return "Time";

			case 2:
				return "Problem";

			case 3:
				return "Language";

			case 4:
				return "Result";

			default:
				break;
			}
			return SubmissionData.fieldNames[arg0];
		}

		@Override
		public Class<?> getColumnClass(int arg0)
		{
			if (arg0 <= 1)
				return Integer.class;
			return String.class;
		}
	}
	
	public JRunsPanel()
	{
		setupGUI();
		setVisible(true);
		data = new Vector<SubmissionData>(
				HashMapSerializer.deserializeFromHashMapArray(Client.server
						.getAllSubmissions(Client.username, Client.password),
						SubmissionData.class));
	}

	private void setupGUI()
	{
		jtRuns = new JTable(new TableRunsDataModel());
		jtRuns.setAutoCreateRowSorter(true);
		jtRuns.setRowHeight(20);
		
		TableColumnModel tcm = jtRuns.getColumnModel();
		tcm.getColumn(1).setCellRenderer(new ContestTimeCellRenderer());
		tcm.getColumn(4).setCellRenderer(new JudgementCellRenderer());
		
		jbtnRefresh = new JButton("Update");
		jbtnRefresh.addActionListener(this);
		
		jpButtons = new JPanel();
		jpButtons.add(jbtnRefresh);
		
		setLayout(new BorderLayout());
		
		add(new JScrollPane(jtRuns), BorderLayout.CENTER);
		add(jpButtons, BorderLayout.SOUTH);
	}
	
	public void setData(Vector<SubmissionData> submissionsData)
	{
		data = submissionsData;
		jtRuns.updateUI();
	}

	public static void main(String[] args)
	{
		new Client();
	}
	
	private void doRefreshAction()
	{
		Vector<SubmissionData> data = HashMapSerializer.deserializeFromHashMapArray(
				Client.server.getAllSubmissions(Client.username,
						Client.password), SubmissionData.class);
		setData(data);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object src = arg0.getSource();
		if (src.equals(jbtnRefresh))
		{
			doRefreshAction();
		}
		
	}
}
