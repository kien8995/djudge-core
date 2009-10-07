package djudge.acmcontester.admin;

import java.awt.Component;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import djudge.acmcontester.structures.RemoteTableSubmissions;
import djudge.acmcontester.structures.SubmissionData.SubmissionDataColumnsEnum;
import djudge.gui.Formatter;

public class DefaultCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	protected JTable table;
	
	protected boolean isSelected;

	private void checkFocusAndSelected(boolean isSelected, boolean hasFocus)
	{
		
	}
	
	protected void setTextAndColor(Object value, int row, int column)
	{
		setText(value.toString());
	}
	
	protected void overrideActive(Object value, int row, int column)
	{
		// nothing to do here
	}
	
	protected void checkActive(int row, int column)
	{
		// nothing to do here
	}
	
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		this.table = table;
		this.isSelected = isSelected;
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setForeground(Color.DARK_GRAY);
		setTextAndColor(value, row, column);
		checkActive(row, column);
		overrideActive(value, row, column);
		checkFocusAndSelected(isSelected, hasFocus);
		return c;
	}
}

class DefaultSubmissionsModelCellRenderer extends DefaultCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	protected RemoteTableSubmissions sdm;
	
	@Override
	protected void checkActive(int row, int column)
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

class MemoryCellRenderer extends DefaultSubmissionsModelCellRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void setTextAndColor(Object value, int row,int column)
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
	protected void setTextAndColor(Object value, int row,int column)
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
	protected void setTextAndColor(Object value, int row,int column)
	{
		String text = Formatter.formatRealTime(value.toString());
		setText(text);
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
	protected void setTextAndColor(Object value, int row,int column)
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
	protected void setTextAndColor(Object value, int row,int column)
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
	protected void setTextAndColor(Object value, int row,int column)
	{
		int data = (Integer) value;
		setText(Formatter.formatScore(data));
	}
}

class PasswordCellRenderer extends DefaultCellRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void setTextAndColor(Object value, int row,int column)
	{
		String s = value.toString();
		String r = "";
		for (int i = 0; i < s.length(); i++)
			r += "*";
		setText(r);
	}
}	
