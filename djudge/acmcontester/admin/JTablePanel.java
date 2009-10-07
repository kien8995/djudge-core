package djudge.acmcontester.admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import djudge.acmcontester.AuthentificationData;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;
import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.acmcontester.structures.RemoteTableUsers;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.SubmissionData.SubmissionDataColumnsEnum;
import djudge.swing.JSubmissionInfoFrame;
import djudge.utils.xmlrpc.AbstractDataTable;

class JAdminSubmissionsPanel extends JTablePanel implements MouseListener
{
	private static final long serialVersionUID = 1L;

	private JPopupMenu popupMenu;
	
	private class PopupMenuHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			int iRow = jtTable.getSelectedRow();
			if (iRow < 0)
				return;
			
			String act = arg0.getActionCommand().toLowerCase();
			if ("activate".equals(act))
			{
    			int prev = Integer.parseInt(tableModel.getValueAt(iRow,
    					SubmissionDataColumnsEnum.Active.ordinal()).toString());
    			if (prev <= 0)
    				prev = 1;
    			else
    				prev = 0;
    			RemoteTableSubmissions rts = (RemoteTableSubmissions) tableModel;
				AuthentificationData ad = rts.getAuthentificationData();
				ServerXmlRpcInterface connector = rts.getConnector();
				connector.activateSubmission(ad.getUsername(),
						ad.getPassword(), tableModel.getValueAt(iRow, 0)
								.toString(), prev);
			}
			else if (act.startsWith("rejudge"))
			{
				RemoteTableSubmissions rts = (RemoteTableSubmissions) tableModel;
				SubmissionData sd = (SubmissionData) rts.getRow(iRow);
				Object value = null;
				String key = "";
				if (act.equals("rejudge_submission"))
				{
					value = sd.id;
					key = "id";
				}
				else if (act.equals("rejudge_user"))
				{
					value = sd.userID;
					key = "user_id";
				}
				else if (act.equals("rejudge_problem"))
				{
					value = tableModel.getValueAt(iRow, SubmissionDataColumnsEnum.ProblemID.ordinal());
					key = "problem_id";
				}
				else if (act.equals("rejudge_language"))
				{
					value = tableModel.getValueAt(iRow, SubmissionDataColumnsEnum.LanguageID.ordinal());
					key = "language_id";
				}
				else
				{
					return;
				}
				AuthentificationData ad = rts.getAuthentificationData();
				ServerXmlRpcInterface connector = rts.getConnector();
				connector.rejudgeSubmissions(ad.getUsername(), ad.getPassword(), key, value.toString());
				rts.updateData();
			}
		}
	}
	
	public JAdminSubmissionsPanel(RemoteTableSubmissions atdm)
	{
		super(atdm);
		
		/* Removing buttons */
		jpButtons.remove(jbtnAddRecord);
		jpButtons.remove(jbtnDeleteRecord);
		jpButtons.remove(jbtnSave);
		
		TableColumnModel tcm = jtTable.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++)
			tcm.getColumn(i).setCellRenderer(new DefaultSubmissionsModelCellRenderer(atdm));
		tcm.getColumn(SubmissionDataColumnsEnum.ContestTime.ordinal()).setCellRenderer(new ContestTimeCellRenderer(atdm));
		tcm.getColumn(SubmissionDataColumnsEnum.Judgement.ordinal()).setCellRenderer(new JudgementCellRenderer(atdm));
		tcm.getColumn(SubmissionDataColumnsEnum.MaxTime.ordinal()).setCellRenderer(new RuntimeCellRenderer(atdm));
		tcm.getColumn(SubmissionDataColumnsEnum.MaxMemory.ordinal()).setCellRenderer(new MemoryCellRenderer(atdm));
		tcm.getColumn(SubmissionDataColumnsEnum.FailedTest.ordinal()).setCellRenderer(new FailedTestCellRenderer(atdm));
		tcm.getColumn(SubmissionDataColumnsEnum.DJudgeFlag.ordinal()).setCellRenderer(new DJudgeStatusCellRenderer(atdm));
		jtTable.addMouseListener(this);
		// Popup menu
		PopupMenuHandler listener = new PopupMenuHandler();
		JMenuItem item;
		popupMenu = new JPopupMenu();
		
		item = new JMenuItem("Details");
		item.setActionCommand("details");
		item.addActionListener(listener);
		popupMenu.add(item);
		
		item = new JMenuItem("[de]activate");
		item.setActionCommand("activate");
		item.addActionListener(listener);
		popupMenu.add(item);
		
		/* 'rejudge' submenu */
		JMenu submenu = new JMenu("Rejudge...");
		
		item = new JMenuItem("submission");
		item.setActionCommand("rejudge_submission");
		item.addActionListener(listener);
		submenu.add(item);
		
		item = new JMenuItem("problem");
		item.setActionCommand("rejudge_problem");
		item.addActionListener(listener);
		submenu.add(item);
		
		item = new JMenuItem("language");
		item.setActionCommand("rejudge_language");
		item.addActionListener(listener);
		submenu.add(item);
		
		item = new JMenuItem("user");
		item.setActionCommand("rejudge_user");
		item.addActionListener(listener);
		submenu.add(item);
		
		popupMenu.add(submenu);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			JTable target = (JTable) e.getSource();
			int row = target.getSelectedRow();
			if  (row < 0)
				return;
			RemoteTableSubmissions sdm = (RemoteTableSubmissions) tableModel;
			SubmissionData sd = (SubmissionData) sdm.getRow(row);
			new JSubmissionInfoFrame(sd);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		if (arg0.getButton() == MouseEvent.BUTTON3)
		{
			int row = jtTable.getSelectedRow();
			if (row != -1)
			{
				popupMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}

class JUsersPanel extends JTablePanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JButton btnGenerate;
	
	public JUsersPanel(RemoteTableUsers udm)
	{
		super(udm);
		
		btnGenerate = new JButton("Generate logins");
		btnGenerate.addActionListener(this);
		jpButtons.add(btnGenerate);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		super.actionPerformed(arg0);
		Object src = arg0.getSource();
		if (src.equals(btnGenerate))
		{
			String str = JOptionPane.showInputDialog(null, "Number of team logins to generate:", "5");
			if (null != str)
			{
				int number = 0;
				try
				{
					number = Integer.parseInt(str);
				}
				catch (Exception e) {
				}
				if (number > 0)
				{
					RemoteTableUsers udm = (RemoteTableUsers) tableModel;
					AuthentificationData ad = udm.getAuthentificationData();
					udm.getConnector().generateLogins(ad.getUsername(), ad.getPassword(), number, "TEAM");
				}
			}
		}
	}
}

public class JTablePanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	protected JTable jtTable;
	
	protected AbstractDataTable tableModel;
	
	protected JPanel jpButtons;
	
	protected JButton jbtnAddRecord;
	
	protected JButton jbtnDeleteRecord;
	
	protected JButton jbtnRefresh;
	
	protected JButton jbtnSave;
	
	public JTablePanel(AbstractDataTable model)
	{
		tableModel = model;
		tableModel.updateData();
		
		jbtnAddRecord = new JButton("Add");
		jbtnAddRecord.addActionListener(this);

		jbtnDeleteRecord = new JButton("Delte");
		jbtnDeleteRecord.addActionListener(this);

		jbtnRefresh = new JButton("Update");
		jbtnRefresh.addActionListener(this);
		
		jbtnSave = new JButton("Save");
		jbtnSave.addActionListener(this);
		
		jtTable = new JTable(model);
		jtTable.setRowHeight(20);
		jtTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		if (tableModel instanceof RemoteTableUsers)
		{
			jtTable.getColumnModel().getColumn(2).setCellRenderer(new PasswordCellRenderer());
		}
		
		jpButtons = new JPanel();
		jpButtons.add(jbtnAddRecord);
		jpButtons.add(jbtnDeleteRecord);
		jpButtons.add(jbtnRefresh);
		jpButtons.add(jbtnSave);
		
		setLayout(new BorderLayout());
		add(jpButtons, BorderLayout.SOUTH);
		add(new JScrollPane(jtTable), BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object source = arg0.getSource();
		if (source.equals(jbtnAddRecord))
		{
			tableModel.insertRow();
			jtTable.updateUI();
		}
		else if (source.equals(jbtnSave))
		{
			tableModel.saveData();
			jtTable.updateUI();
		}
		else if (source.equals(jbtnRefresh))
		{
			tableModel.updateData();
			jtTable.updateUI();
		}
		else if (source.equals(jbtnDeleteRecord))
		{
			int iRow = jtTable.getSelectedRow();
			if (iRow >= 0)
			{
				tableModel.deleteRow(iRow);
				jtTable.updateUI();
			}
		}
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}
	
}
