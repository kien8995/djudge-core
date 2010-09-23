/* $Id$ */

package djudge.utils.xmlrpc;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractDataTable extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	public abstract boolean updateData();
	
	public abstract boolean saveData();
	
	public abstract boolean insertRow();
	
	public abstract boolean deleteRow(int index);
	
}
