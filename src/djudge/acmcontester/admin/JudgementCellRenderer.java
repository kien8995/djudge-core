package djudge.acmcontester.admin;

import java.awt.Color;

import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.gui.Formatter;

public class JudgementCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	public JudgementCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}	
	
	public JudgementCellRenderer()
	{
		super(null);
	}

	@Override
	protected void setTextAndColor(Object value, int row, int column)
	{
		String data = (String) value;
		if ("AC".equalsIgnoreCase(data))
		{
			setBackground(Color.GREEN);
		}
		else if ("WA".equalsIgnoreCase(data) || "TLE".equalsIgnoreCase(data) || "RE".equalsIgnoreCase(data) || "MLE".equalsIgnoreCase(data))
		{
			setBackground(Color.RED);
		}
		else if ("N/A".equalsIgnoreCase(data))
		{
			setBackground(Color.YELLOW);
		}
		else if ("CE".equalsIgnoreCase(data))
		{
			setBackground(Color.LIGHT_GRAY);
		}
		else
		{
			setBackground(Color.WHITE);
		}
		setText(Formatter.formatJudgement(data));
	}	
}

