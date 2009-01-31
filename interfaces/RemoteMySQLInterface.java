package interfaces;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import judge.Judge;
import judge.JudgeTaskDescription;
import judge.JudgeTaskResult;

class ThreadSubmitter extends Thread implements Submitter
{
	Queue <JudgeTaskResult> tasks = new LinkedList<JudgeTaskResult>();

	String url;
	
	public ThreadSubmitter(String url)
	{
		super();
		this.url = url;
	}
	
	@Override
	public void submitResult(JudgeTaskResult result)
	{
		synchronized (tasks)
		{
			System.out.println("Submitter.queued: " + result.desc.tid);
			tasks.add(result);
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			try {wait(100);}
			catch (Exception e){}
			
			JudgeTaskResult res = null;
			
    		synchronized (tasks)
    		{
    			res = tasks.poll();
    		}
    		
    		if (res == null) continue;
    		
    		System.out.println("Submitter.submittting: " + res.desc.tid);
    		processSubmit(res);
    		
		}	
	}
	
	private void processSubmit(JudgeTaskResult res)
	{
		try
		{
			Statement stmt;

			java.sql.Connection con = DriverManager.getConnection(url, "school2009", "school2009");
			
			stmt = con.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			String req;
			
			req = "UPDATE submissions " +
					"SET score = -1 " +
					"WHERE id = " + res.desc.tid;
			
			stmt.executeUpdate(req);
			
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}

class ThreadTester extends Thread
{
	Queue<JudgeTaskDescription> tasks = new LinkedList<JudgeTaskDescription>();
	Submitter submitter;
	
	public void run()
	{
		while (true)
		{
			try {wait(100);}
			catch (Exception e){}
			
			JudgeTaskDescription desc = null;
			
    		synchronized (tasks)
    		{
    			desc = tasks.poll();
    		}
    		
    		if (desc == null) continue;
    		
    		System.out.println("Tester.judging: " + desc.tid);
    		JudgeTaskResult res = Judge.judgeTask(desc);
    		System.out.println("Tester.judged: " + desc.tid);
    		submitter.submitResult(res);
		}
	}
	
	public void addTasks(List<JudgeTaskDescription> newTasks)
	{
		synchronized (tasks)
		{
			for (JudgeTaskDescription judgeTaskDescription : newTasks)
			{
				System.out.println("Tester.queue: " + judgeTaskDescription.tid);
				tasks.add(judgeTaskDescription);
			}
		}
	}
	
}

public class RemoteMySQLInterface
{
	
	public static void main(String arg[])
	{
		try
		{
			String url = "jdbc:mysql://localhost/school2009";
			
			// Submitter
			ThreadSubmitter subm = new ThreadSubmitter(url);
			subm.start();
			
			// Tester
			ThreadTester tester = new ThreadTester();
			tester.submitter = subm;
			tester.start();
			
			Statement stmt;
			ResultSet rs;

			java.sql.Connection con = DriverManager.getConnection(url, "school2009", "school2009");

			System.out.println("URL: " + url);
			System.out.println("Connection: " + con);
			
			// Get a Statement object
			stmt = con.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);

			// Query the database, storing the result in an object of type ResultSet
			rs = stmt.executeQuery("SELECT " +
					"submissions.*, " +
					"languages.t_sid AS language_tsid, " +
					"problems.t_contest AS problem_tcontest, " +
					"problems.t_problem AS problem_tproblem " +
					"FROM submissions " +
					"LEFT JOIN languages ON submissions.language_id = languages.id " +
					"LEFT JOIN problems ON submissions.problem_id = problems.id " +
					"WHERE djudge_flag = 1");

			List<JudgeTaskDescription> list = new LinkedList<JudgeTaskDescription>();
			
			// Reading submissions
			while(rs.next())
			{
				JudgeTaskDescription desc = new JudgeTaskDescription();
				desc.tid = rs.getInt("id");
				desc.tcontest = rs.getString("problem_tcontest"); 
				desc.tproblem = rs.getString("problem_tproblem");
				desc.tlanguage = rs.getString("language_tsid");
				desc.tsourcecode = rs.getString("source_code");
				desc.fTrial = rs.getInt("f_final") > 0 ? 0 : 1;
				list.add(desc);
			}
			
			tester.addTasks(list);
			System.out.println(list.size());
			
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
