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

import djudge.acmcontester.interfaces.AcmContesterXmlRpcClientInterface;
import djudge.acmcontester.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.structures.MonitorData;

public class JMonitorPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;

	JTable jtMonitor;
	
	JPanel jpButtons;
	
	JButton jbtnRefresh;

	MonitorData data = new MonitorData();

	AcmContesterXmlRpcClientInterface serverInterface;
	
	AuthentificationDataProvider authProvider;	
	
	class MonitorDataModel extends AbstractTableModel
	{

		private static final long serialVersionUID = 1L;

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

	public JMonitorPanel(AcmContesterXmlRpcClientInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		this.serverInterface = serverInterface;
		this.authProvider = authProvider;
		setupGUI();
		setVisible(true);
	}

	private void setupGUI()
	{
		data = ContestCore.getMonitor("", "");
		jtMonitor = new JTable(new MonitorDataModel());
		jtMonitor.setRowHeight(25);
		jtMonitor.setAutoCreateRowSorter(true);
		
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
		MonitorData data = serverInterface.getMonitor(authProvider.getUsername(), authProvider.getPassword());
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
