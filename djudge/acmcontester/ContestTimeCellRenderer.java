package djudge.acmcontester;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import utils.PrintfFormat;

public class ContestTimeCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
		int data = (Integer) value;
		int seconds = data / 1000;
		setText(new PrintfFormat("%02d:%02d:%02d").sprintf(new Object[] {seconds / 60 / 60, (seconds / 60) % 60, seconds % 60}));
		setHorizontalAlignment(SwingConstants.RIGHT);
		return this;
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}	
}

class MemoryCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
		int data = (Integer) value;
		if (data < 0)
		{
			setText("-");
		}
		else
		{
			setText("" + (data / 1024) + " KB");
		}
		return this;
	}
}

class RuntimeCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
		int data = (Integer) value;
		if (data < 0)
		{
			setText("-");
		}
		else
		{
			setText("" + (data) + " ms");
		}
		return this;
	}
}

class JudgementCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
		String data = (String) value;
		if ("AC".equalsIgnoreCase(data))
		{
			setBackground(Color.GREEN);
		}
		else if ("WA".equalsIgnoreCase(data) || "TLE".equalsIgnoreCase(data) || "RE".equalsIgnoreCase(data))
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
		setText(value.toString());
		return this;
	}
}

class DJudgeStatusCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
		int data = (Integer) value;
		if (data < 0)
		{
			setForeground(Color.RED);
			setText("Judging");
		}
		else if (data == 0)
		{
			setForeground(Color.GREEN);
			setText("Waiting");
		}
		else
		{
			setForeground(Color.black);
			setText("OK");
		}
		return this;
	}
}

class FailedTestCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
		int data = (Integer) value;
		if (data < 0)
		{
			setText("-");
		}
		else
		{
			setText("" + (data + 1));
		}
		return this;
	}
}

