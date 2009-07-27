package djudge.acmcontester;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class JudgementCellRenderer extends DefaultTableCellRenderer
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
		else if ("WA".equalsIgnoreCase(data))
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


