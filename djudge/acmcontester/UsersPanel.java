package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import djudge.acmcontester.models.UsersModel;
import djudge.acmcontester.structures.UserDescription;

public class UsersPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	private JTable jtUsers;
	
	private TableDataModel jtUsersModel;
	
	private Vector<UserDescription> users;
	
	private JButton jbtnAdd;
	
	public UsersPanel()
	{
		users = new Vector<UserDescription>();
		setLayout(new BorderLayout());

		jbtnAdd = new JButton("Add");
		jbtnAdd.setPreferredSize(new Dimension(120, 25));
		jbtnAdd.addActionListener(jtUsersModel);
		add(jbtnAdd, BorderLayout.SOUTH);
		
		jtUsers = new JTable(jtUsersModel = new TableDataModel());
		jtUsers.setRowHeight(20);
		add(new JScrollPane(jtUsers), BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	public void setData(Vector<UserDescription> users)
	{
		this.users = users;
	}
	
	private class TableDataModel extends AbstractTableModel implements ActionListener
	{
		private static final long serialVersionUID = 1L;

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
			return users.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1)
		{
			return users.get(arg0).getColumnValue(arg1);
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1)
		{
			return arg1 > 0;
		}

		@Override
		public void setValueAt(Object arg0, int arg1, int arg2)
		{
			UserDescription ud = users.get(arg1);
			ud.setColumnValue(arg2, arg0.toString());
			UsersModel.updateData(ud);
		}

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if (arg0.getSource().equals(jbtnAdd))
			{
				UserDescription newRow = UsersModel.insertRow();
				users.add(newRow);
				// TODO: is it needed?
				fireTableDataChanged();
				fireTableStructureChanged();
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
}
