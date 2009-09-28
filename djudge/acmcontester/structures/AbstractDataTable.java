package djudge.acmcontester.structures;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractDataTable extends AbstractTableModel
{
	public abstract boolean updateData();
	
	public abstract boolean saveData();
	
	public abstract boolean insertRow();
	
	public abstract boolean deleteRow(int index);
	
}
