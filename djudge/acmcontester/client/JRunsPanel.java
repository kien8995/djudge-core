package djudge.acmcontester.client;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import djudge.acmcontester.structures.SubmissionData;
import djudge.common.HashMapSerializer;

public class JRunsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	JTable jtRuns;
	
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
			return SubmissionData.fieldNames[arg0];
		}
		
		@Override
		public Class<?> getColumnClass(int arg0)
		{
			if (arg0 <= 1) return Integer.class;
			return String.class;
		}
	}
	
	public JRunsPanel()
	{
		setupGUI();
		setVisible(true);
		data = new Vector<SubmissionData>(HashMapSerializer.deserializeFromHashMapArray(Client.server.getAllSubmissions("", ""), SubmissionData.class));
	}
	
	private void setupGUI()
	{
		setLayout(new BorderLayout());
		jtRuns = new JTable(new TableRunsDataModel());
		jtRuns.setAutoCreateRowSorter(true);
		add(new JScrollPane(jtRuns), BorderLayout.CENTER);
		
	}
	
	public static void main(String[] args)
	{
		new Client();
	}
}
