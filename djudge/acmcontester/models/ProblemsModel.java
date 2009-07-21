package djudge.acmcontester.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import djudge.acmcontester.exceptions.NoDataException;
import djudge.acmcontester.structures.ProblemDescription;

public class ProblemsModel extends AbstractDBModel
{
	
	public static Vector<ProblemDescription> getAllProblems()
	{
		Vector <ProblemDescription> v = new Vector <ProblemDescription>();
		synchronized (AbstractDBModel.dbMutex)
		{
			Statement st = AbstractDBModel.getStatement();
			String sql = "SELECT * FROM problems";
			try
			{
				ResultSet rs = st.executeQuery(sql);
				while (rs.next())
				{
					v.add(new ProblemDescription(rs));
				}
				rs.close();
				st.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return v;
	}

	public static ProblemDescription getProblemByID(String problemID) throws NoDataException
	{
		ProblemDescription result = null;
		synchronized (AbstractDBModel.dbMutex)
		{
			Statement st = AbstractDBModel.getStatement();
			String sql = "SELECT * FROM problems where problems.id = " + problemID;
			try
			{
				ResultSet rs = st.executeQuery(sql);
				if (rs.next())
				{
					result = new ProblemDescription(rs);
				}
				else
				{
					throw new NoDataException();
				}
				rs.close();
				st.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void updateData(ProblemDescription pd)
	{
		AbstractDBModel.executeUpdate(pd.getUpdateStatement());
	}
	
}
