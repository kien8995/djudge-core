package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import db.AbstractTableDataModel;

public class JTablePanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	protected JTable jtTable;
	
	protected AbstractTableDataModel jtttTableModel;
	
	protected JButton jbtnAddRecord;
	
	public JTablePanel(AbstractTableDataModel atdm)
	{
		jtttTableModel = atdm;
		setLayout(new BorderLayout());
		jbtnAddRecord = new JButton("Add");
		jbtnAddRecord.setPreferredSize(new Dimension(120, 25));
		jbtnAddRecord.addActionListener(this);
		add(jbtnAddRecord, BorderLayout.SOUTH);
		
		jtTable = new JTable(atdm);
		jtTable.setRowHeight(20);
		jtTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(jtTable), BorderLayout.CENTER);
		
		setVisible(true);
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
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
	
}
