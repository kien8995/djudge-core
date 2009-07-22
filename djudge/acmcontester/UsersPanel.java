package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import db.AbstractTableDataModel;

public class UsersPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private JTable jtUsers;
	
	private AbstractTableDataModel jtUsersModel;
	
	private JButton jbtnAdd;
	
	public UsersPanel(AbstractTableDataModel atdm)
	{
		jtUsersModel = atdm;
		
		setLayout(new BorderLayout());

		jbtnAdd = new JButton("Add");
		jbtnAdd.setPreferredSize(new Dimension(120, 25));
		jbtnAdd.addActionListener(this);
		add(jbtnAdd, BorderLayout.SOUTH);
		
		jtUsers = new JTable(atdm);
		jtUsers.setRowHeight(20);
		add(new JScrollPane(jtUsers), BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object source = arg0.getSource();
		if (source.equals(jbtnAdd))
		{
			jtUsersModel.insertRow();//appendRecord();
		}
	}
}
