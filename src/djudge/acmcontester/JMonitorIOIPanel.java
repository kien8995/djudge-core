/* $Id$ */

package djudge.acmcontester;

import java.awt.Color;

import javax.swing.table.TableColumnModel;

import djudge.acmcontester.admin.DefaultCellRenderer;
import djudge.acmcontester.server.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.server.interfaces.TeamXmlRpcInterface;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.UserProblemStatusIOI;

public class JMonitorIOIPanel extends JMonitorPanelAbstract implements Updateble
{
	private static final long serialVersionUID = 1L;

	class ProblemIOICellRenderer extends DefaultCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void setTextAndColor(Object value, int row, int column)
		{
			UserProblemStatusIOI data = (UserProblemStatusIOI) value;
			if (data.isFullScore)
			{
				setBackground(Color.GREEN);
			}
			else if (data.isPending)
			{
				setBackground(Color.YELLOW);
			}
			else if (data.submissionsTotal > 0)
			{
				setBackground(new Color(0xFF, 0x66, 0x00));
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
			setText(value.toString());
		}
	}
	
	
	public JMonitorIOIPanel(TeamXmlRpcInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		super(serverInterface, authProvider);
	}

	@Override
	protected void setTableRenderers()
	{
		TableColumnModel cm = jtMonitor.getColumnModel();
		cm.getColumn(0).setCellRenderer(new UserInfoCellRenderer());
		cm.getColumn(1).setCellRenderer(new UserInfoCellRenderer());
		cm.getColumn(2).setCellRenderer(new UserInfoCellRenderer());
		//for (int i = 3; i < jtMonitor.getColumnCount(); i++)
		//{
//			cm.getColumn(i).setCellRenderer(new ProblemIOICellRenderer());
	//	}
	}
	
	@Override
	protected void doRefreshAction()
	{
		MonitorData data = new MonitorData(serverInterface.getTeamMonitor(
				authProvider.getUsername(), authProvider.getPassword()));
		setData(data);
	}

	@Override
	public boolean updateState()
	{
		doRefreshAction();
		return true;
	}
}
