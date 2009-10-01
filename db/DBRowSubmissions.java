package db;

public class DBRowSubmissions extends DBRowAbstract
{
	@Override
	public Class<? extends AbstractTableDataModel> getTableClass()
	{
		return SubmissionsDataModel.class;
	}
	
	@Override
	protected String getTableNameForEditing()
	{
		return "submissions";
	}
}

