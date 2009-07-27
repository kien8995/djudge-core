package djudge.acmcontester;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ContestTimeCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
		int data = (Integer) value;
		int minutes = data / 60 / 1000;
		setText(minutes / 60 + ":" + minutes % 60);
		return this;
	}
}


