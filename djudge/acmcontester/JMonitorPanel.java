package djudge.acmcontester;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;
import djudge.acmcontester.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.UserProblemStatus;
import djudge.common.HashMapSerializer;

public class JMonitorPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;

	JTable jtMonitor;
	
	JPanel jpButtons;
	
	JButton jbtnRefresh;

	MonitorData data = new MonitorData();
	
	Vector<ProblemData> problems;

	AcmContesterXmlRpcClientInterface serverInterface;
	
	AuthentificationDataProvider authProvider;
	
	class MonitorDataModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;

		@Override
		public String getColumnName(int colNum)
		{
			if (colNum == 0)
			{
				return "User";
			}
			else if (colNum == 1)
			{
				return "Solved";
			}
			else if (colNum == 2)
			{
				return "Time";
			}
			return problems.get(colNum-3).sid; 
		}
		
		@Override
		public int getColumnCount()
		{
			if (data.rows.length > 0)
			{
				return data.rows[0].problemData.length + 3;
			}
			return 0;
		}

		@Override
		public int getRowCount()
		{
			return data.rows.length;
		}

		@Override
		public Object getValueAt(int arg0, int arg1)
		{
			if (arg1 == 0)
			{
				return data.rows[arg0].username;
			}
			else if (arg1 == 1)
			{
				return data.rows[arg0].totalSolved;
			}
			else if (arg1 == 2)
			{
				return data.rows[arg0].totalTime;
			}
			else
			{
				return data.rows[arg0].problemData[arg1 - 3];
			}
		}
	}

	class ProblemCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			UserProblemStatus data = (UserProblemStatus) value;
			if (data.wasSolved)
			{
				setBackground(Color.GREEN);
			}
			else if (data.isPending)
			{
				setBackground(Color.YELLOW);
			}
			else if (data.wrongTryes > 0)
			{
				setBackground(Color.RED);
			}
			else
			{
				setBackground(Color.WHITE);
			}
			setText(value.toString());
			return this;
		}

	/*	public void setValue(Object value)
		{
			setText(value.toString());
		}*/
	}
	
	class InfoCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			if (table.getModel().getValueAt(row, 0).toString().equals(authProvider.getUsername()))
			{
				setBackground(Color.CYAN);
			}
			else if (getBackground() == Color.WHITE || getBackground() == Color.GRAY)
			{
				setBackground(Color.LIGHT_GRAY);
			}
			else
			{
				setBackground(Color.GRAY);
			}
			if (column == 2)
			{
				value = ((Long) value) / 1000 / 60;
			}
			setText(value.toString());
			return this;
		}

	/*	public void setValue(Object value)
		{
			setText(value.toString());
		}*/
	}
	
	public JMonitorPanel(AcmContesterXmlRpcClientInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		this.serverInterface = serverInterface;
		this.authProvider = authProvider;
		setupGUI();
		setVisible(true);
	}

	private void setupGUI()
	{
		problems = HashMapSerializer.deserializeFromHashMapArray(
				serverInterface.getProblems(authProvider.getUsername(),
						authProvider.getPassword()), ProblemData.class);
		data = new MonitorData(serverInterface.getMonitor(authProvider.getUsername(), authProvider.getPassword()));
		
		jtMonitor = new JTable(new MonitorDataModel());
		jtMonitor.setRowHeight(25);
		jtMonitor.setAutoCreateRowSorter(true);
		
		TableColumnModel cm = jtMonitor.getColumnModel();
		cm.getColumn(0).setCellRenderer(new InfoCellRenderer());
		cm.getColumn(1).setCellRenderer(new InfoCellRenderer());
		cm.getColumn(2).setCellRenderer(new InfoCellRenderer());
		for (int i = 3; i < jtMonitor.getColumnCount(); i++)
		{
			cm.getColumn(i).setCellRenderer(new ProblemCellRenderer());
		}
		
		jbtnRefresh = new JButton("Update");
		jbtnRefresh.addActionListener(this);
		
		jpButtons = new JPanel();
		jpButtons.add(jbtnRefresh);
		
		setLayout(new BorderLayout());
		
		add(new JScrollPane(jtMonitor), BorderLayout.CENTER);
		add(jpButtons, BorderLayout.SOUTH);
	}
	
	public void updateData()
	{
		doRefreshAction();
	}
	
	private void setData(MonitorData monitorData)
	{
		data = monitorData;
		jtMonitor.updateUI();
	}

	private void doRefreshAction()
	{
		MonitorData data = new MonitorData(serverInterface.getMonitor(
				authProvider.getUsername(), authProvider.getPassword()));
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
	
	public static void main(String[] args)
	{
		new Admin();
	}
}
