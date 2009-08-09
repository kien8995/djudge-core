package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import db.AbstractTableDataModel;

class JAdminSubmissionsPanel extends JTablePanel
{
	private static final long serialVersionUID = 1L;

	public JAdminSubmissionsPanel(AbstractTableDataModel atdm)
	{
		super(atdm);
		TableColumnModel tcm = jtTable.getColumnModel();
		tcm.getColumn(4).setCellRenderer(new ContestTimeCellRenderer());
		tcm.getColumn(6).setCellRenderer(new JudgementCellRenderer());
		tcm.getColumn(7).setCellRenderer(new RuntimeCellRenderer());
		tcm.getColumn(8).setCellRenderer(new MemoryCellRenderer());
		tcm.getColumn(9).setCellRenderer(new MemoryCellRenderer());
		tcm.getColumn(11).setCellRenderer(new FailedTestCellRenderer());
		tcm.getColumn(15).setCellRenderer(new DJudgeStatusCellRenderer());
	}
}

public class JTablePanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	protected JTable jtTable;
	
	protected AbstractTableDataModel jtttTableModel;
	
	protected JPanel jpButtons;
	
	protected JButton jbtnAddRecord;
	
	protected JButton jbtnDeleteRecord;
	
	protected JButton jbtnRefresh;
	
	public JTablePanel(AbstractTableDataModel atdm)
	{
		jtttTableModel = atdm;
		
		jbtnAddRecord = new JButton("Add");
		jbtnAddRecord.addActionListener(this);

		jbtnDeleteRecord = new JButton("Delte");
		jbtnDeleteRecord.addActionListener(this);

		jbtnRefresh = new JButton("Update");
		jbtnRefresh.addActionListener(this);
		
		jtTable = new JTable(atdm);
		jtTable.setRowHeight(20);
		jtTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jpButtons = new JPanel();
		jpButtons.add(jbtnAddRecord);
		jpButtons.add(jbtnDeleteRecord);
		jpButtons.add(jbtnRefresh);
		
		setLayout(new BorderLayout());
		add(jpButtons, BorderLayout.SOUTH);
		add(new JScrollPane(jtTable), BorderLayout.CENTER);
		
		//setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object source = arg0.getSource();
		if (source.equals(jbtnAddRecord))
		{
			jtttTableModel.insertRow();
			jtTable.updateUI();
		}
		else if (source.equals(jbtnRefresh))
		{
			jtttTableModel.fill();
			jtTable.updateUI();
		}
		else if (source.equals(jbtnDeleteRecord))
		{
			int iRow = jtTable.getSelectedRow();
			if (iRow >= 0)
			{
				jtttTableModel.deleteRow(iRow);
				jtTable.updateUI();
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
	
}

