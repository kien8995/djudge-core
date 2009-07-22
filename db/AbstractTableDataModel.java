package db;

public abstract class AbstractTableDataModel extends DBTableAbstract
{
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return col > 0;
	}
}
