package djudge.acmcontester.admin;

import javax.swing.SwingConstants;

import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.gui.Formatter;

public class ContestTimeCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void setTextAndColor(Object value, int row, int column)
	{
		long data = (Integer) value;
		setText(Formatter.formatContestTime(data));
		setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	public ContestTimeCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}
		
	public ContestTimeCellRenderer()
	{
		super(null);
	}
}

