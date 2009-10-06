package djudge.acmcontester.admin;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.acmcontester.structures.SubmissionData.SubmissionDataColumnsEnum;
import djudge.gui.Formatter;

class DefaultCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	private void checkFocusAndSelected(boolean isSelected, boolean hasFocus)
	{
		// TODO: implement this
	}
	
	void setTextAndColor(Object value, int row,int column)
	{
		setText(value.toString());
	}
	
	void overrideActive(Object value, int row,int column)
	{
		// nothing to do here
	}
	
	void checkActive(int row,int column)
	{
		// nothing to do here
	}
	
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		setForeground(Color.DARK_GRAY);
		setTextAndColor(value, row, column);
		checkActive(row, column);
		overrideActive(value, row, column);
		checkFocusAndSelected(isSelected, hasFocus);
		return this;
	}
}

class DefaultSubmissionsModelCellRenderer extends DefaultCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	protected RemoteTableSubmissions sdm;
	
	@Override
	void checkActive(int row, int column)
	{
		if (sdm == null)
			return;
		
		if (Integer.parseInt(sdm.getValueAt(row, SubmissionDataColumnsEnum.Active.ordinal()).toString()) < 1)
		{
			setForeground(Color.LIGHT_GRAY);
			setBackground(Color.WHITE);
		}
	}
	
	public DefaultSubmissionsModelCellRenderer(RemoteTableSubmissions sdm)
	{
		this.sdm = sdm;
	}
}

public class ContestTimeCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	void setTextAndColor(Object value, int row, int column)
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

	public static void main(String[] args)
	{
		new AdminClient();
	}	
}

class MemoryCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	void setTextAndColor(Object value, int row,int column)
	{
		String text = Formatter.formatMemory((Integer) value);
		setText(text);
		setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	public MemoryCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}	
}

class RuntimeCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	public RuntimeCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}	
	
	@Override
	void setTextAndColor(Object value, int row,int column)
	{
		String text = Formatter.formatRuntime((Integer) value);
		setText(text);
	}
}

class RealTimeCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	public RealTimeCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}	
	
	@Override
	void setTextAndColor(Object value, int row,int column)
	{
		String text = Formatter.formatRealTime(value.toString());
		setText(text);
	}	
}

class JudgementCellRenderer extends DefaultSubmissionsModelCellRenderer
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
	void setTextAndColor(Object value, int row, int column)
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

class DJudgeStatusCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	public DJudgeStatusCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}
		
	@Override
	void setTextAndColor(Object value, int row,int column)
	{
		int val = (Integer) value;
		String text = Formatter.formatDJudgeFlag(val);
		setText(text);
		if (val <= 0)
		{
			setForeground(Color.RED);
		}
		else
		{
			setForeground(Color.BLACK);
		}
	}	
}

class FailedTestCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	public FailedTestCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}
	
	@Override
	void setTextAndColor(Object value, int row,int column)
	{
		int data = (Integer) value;
		setText(Formatter.formatFailedTest(data));
	}
}

class ScoreCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	public ScoreCellRenderer(RemoteTableSubmissions sdm)
	{
		super(sdm);
	}
		
	@Override
	void setTextAndColor(Object value, int row,int column)
	{
		int data = (Integer) value;
		setText(Formatter.formatScore(data));
	}
}

class PasswordCellRenderer extends DefaultCellRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	void setTextAndColor(Object value, int row,int column)
	{
		String s = value.toString();
		String r = "";
		for (int i = 0; i < s.length(); i++)
			r += "*";
		setText(r);
	}
}
