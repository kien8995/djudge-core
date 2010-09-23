/* $Id$ */

package djudge.acmcontester;

import javax.swing.table.TableColumnModel;

import djudge.acmcontester.server.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.server.interfaces.TeamXmlRpcInterface;
import djudge.acmcontester.structures.MonitorData;

public class JMonitorACMPanel extends JMonitorPanelAbstract implements Updateble
{
	private static final long serialVersionUID = 1L;

	public JMonitorACMPanel(TeamXmlRpcInterface serverInterface,
			AuthentificationDataProvider authProvider)
	{
		super(serverInterface, authProvider);
	}

	@Override
	protected void doRefreshAction()
	{
		MonitorData data = new MonitorData(serverInterface.getTeamMonitor(
				authProvider.getUsername(), authProvider.getPassword()));
		setData(data);
		setStateLabelText();
	}
	
	@Override
	protected void setTableRenderers()
	{
		TableColumnModel cm = jtMonitor.getColumnModel();
		cm.getColumn(0).setCellRenderer(new UserInfoCellRenderer());
		cm.getColumn(1).setCellRenderer(new UserInfoCellRenderer());
		cm.getColumn(2).setCellRenderer(new UserInfoCellRenderer());
		for (int i = 3; i < jtMonitor.getColumnCount(); i++)
		{
			cm.getColumn(i).setCellRenderer(new ProblemCellRenderer());
		}
	}

	@Override
	public boolean updateState()
	{
		doRefreshAction();
		return true;
	}
}
