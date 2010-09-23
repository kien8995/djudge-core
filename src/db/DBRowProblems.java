/* $Id$ */

package db;

public class DBRowProblems extends DBRowAbstract
{
	@Override
	public Class<? extends AbstractTableDataModel> getTableClass()
	{
		return ProblemsDataModel.class;
	}
}
