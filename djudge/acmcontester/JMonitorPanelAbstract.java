package djudge.acmcontester;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import djudge.acmcontester.admin.DefaultCellRenderer;
import djudge.acmcontester.server.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.server.interfaces.TeamXmlRpcInterface;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.UserProblemStatusACM;
import djudge.utils.xmlrpc.HashMapSerializer;

public abstract class JMonitorPanelAbstract extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected JTable jtMonitor;
	
	protected JPanel jpButtons;
	
	protected JButton jbtnRefresh;

	protected MonitorData data = new MonitorData();
	
	protected Vector<ProblemData> problems;

	protected TeamXmlRpcInterface serverInterface;
	
	protected AuthentificationDataProvider authProvider;
	
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
			if (data.teams.length > 0)
			{
				return data.teams[0].acmData.length + 3;
			}
			return 0;
		}

		@Override
		public int getRowCount()
		{
			return data.teams.length;
		}

		@Override
		public Object getValueAt(int arg0, int arg1)
		{
			if (arg1 == 0)
			{	
				return data.teams[arg0].username;
			}
			else if (arg1 == 1)
			{
				return data.teams[arg0].totalSolved;
			}
			else if (arg1 == 2)
			{
				return data.teams[arg0].totalTime;
			}
			else
			{
				return data.teams[arg0].acmData[arg1 - 3];
			}
		}
	}

	class ProblemCellRenderer extends DefaultCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void setTextAndColor(Object value, int row, int column)
		{
			UserProblemStatusACM data = (UserProblemStatusACM) value;
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
			else if (isSelected)
			{
				setBackground(super.getBackground());
			}
			else
			{
				setBackground(Color.WHITE);
			}
			setText(value.toString());
		}
	}
	
	protected class UserInfoCellRenderer extends DefaultCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void setTextAndColor(Object value, int row, int column)
		{
			if (table.getModel().getValueAt(row, 0).toString().equals(authProvider.getUsername()))
			{
				setBackground(new Color(0x99, 0xFF, 0xFF));
			}
			else if (isSelected)
			{
				setBackground(super.getBackground());
			}
			else
			{
				setBackground(Color.WHITE);
			}
			setText(value.toString());
		}
	}
	
	public JMonitorPanelAbstract(TeamXmlRpcInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		this.serverInterface = serverInterface;
		this.authProvider = authProvider;
		setupGUI();
		setVisible(true);
	}

	protected void sortMonitor()
	{
		
	}
	
	protected void setTableRenderers()
	{
		
	}


	private void setupGUI()
	{
		problems = HashMapSerializer.deserializeFromHashMapArray(
				serverInterface.getTeamProblems(authProvider.getUsername(),
						authProvider.getPassword()), ProblemData.class);
		
		data = new MonitorData(serverInterface.getTeamMonitor(authProvider.getUsername(), authProvider.getPassword()));
		sortMonitor();
		
		jtMonitor = new JTable(new MonitorDataModel());
		jtMonitor.setRowHeight(25);
		
		setTableRenderers();
		
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
	
	protected void setData(MonitorData monitorData)
	{
		data = monitorData;
		jtMonitor.updateUI();
	}

	protected abstract void doRefreshAction();

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
