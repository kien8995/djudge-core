package db;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public class CellDefault extends JComboBox
{
	private static final long serialVersionUID = 1L;

	class MyCellRenderer extends JLabel implements ListCellRenderer
	{	
		private static final long serialVersionUID = 1L;
		
		private CellDefault cell;
		
		public MyCellRenderer(CellDefault cell)
		{
			this.cell = cell;
		}
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{
			setText(value != null ? cell.map(value.toString()).toString() : "null");
			Color background;
			Color foreground;
			JList.DropLocation dropLocation = list.getDropLocation();
			if (dropLocation != null && !dropLocation.isInsert()
					&& dropLocation.getIndex() == index)
			{
				background = Color.BLUE;
				foreground = Color.WHITE;
			}
			else if (isSelected)
			{
				background = Color.RED;
				foreground = Color.RED;
			}
			else
			{
				background = Color.WHITE;
				foreground = Color.black;
			}
			setBackground(background);
			setForeground(foreground);
			return this;
		}
		
	}
	
	class DBRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		CellDefault cell;		
		
		public DBRenderer(CellDefault cell)
		{
			super();
			this.cell = cell;
		}

		public void setValue(Object value)
	    {
			setText(cell.map(value != null ? value.toString() : "").toString());
	    }
	}

	public String getLabel()
	{
		return "Label";
	}
	
	public Object map(Object key)
	{
		return key != null ? key : "";
	}

	public Object unmap(Object value)
	{
		return value != null ? value : "";
	}
	
	public TableCellEditor getCustomEditor()
	{
		return new DefaultCellEditor(new JTextField());
	}
	
	public TableCellRenderer getCustomRenderer()
	{
		return new DBRenderer(this);
	}
	
	public CellDefault()
	{
		setRenderer(new MyCellRenderer(this));
	}
	
}
