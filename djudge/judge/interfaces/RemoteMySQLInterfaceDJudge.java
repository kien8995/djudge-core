package djudge.judge.interfaces;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import djudge.judge.Judge;
import djudge.judge.JudgeTaskDescription;
import djudge.judge.JudgeTaskResult;



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
			try {TimeUnit.MILLISECONDS.sleep(100);}
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
			ResultSet rs;
			String req;

			java.sql.Connection con = DriverManager.getConnection(url, 
					RemoteMySQLInterfaceDJudge.connectionUsername, 
					RemoteMySQLInterfaceDJudge.connectionPassword);
			
			stmt = con.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = stmt.executeQuery("SELECT * FROM submissions " +
					"WHERE id = " + res.desc.tid);

			while(rs.next())
			{
				int xml_id = rs.getInt("id");

				String sql = " UPDATE submissions_data " +
						" SET xml = ? " +  
						" WHERE id = " + xml_id;

				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, res.res.getXMLString());
				ps.execute();
				System.out.println(res.res.getXMLString());
			}
			
			req = "UPDATE submissions " +
					" SET " + 
					" judgement =  '" + res.res.getResult() + "' " + 
					", status =  'CHECKED' " + 
					", exec_time = "+ res.res.getMaxTime() +" " + 
					", exec_memory = "+ res.res.getMaxMemory() +" " + 
					", exec_output = "+ res.res.getMaxOutput() + " " + 
					", djudge_status = 1 " + 
					" WHERE id = " + res.desc.tid;
			System.out.println(req);
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
			try {TimeUnit.MILLISECONDS.sleep(100);}
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

public class RemoteMySQLInterfaceDJudge
{
	public final static String connectionUrl = "jdbc:mysql://localhost/djudge";
	public final static String connectionUsername = "djudge";
	public final static String connectionPassword = "djudge";
	
	public static void main(String arg[])
	{
		// Submitter
		ThreadSubmitter subm = new ThreadSubmitter(connectionUrl);
		subm.start();
		
		// Tester
		ThreadTester tester = new ThreadTester();
		tester.submitter = subm;
		tester.start();
		
		while (true)
		{
			try {TimeUnit.MILLISECONDS.sleep(1000);}
			catch (Exception e){}
			
    		try
    		{	
    			Statement stmt;
    			ResultSet rs;
    
    			java.sql.Connection con = DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);
    
    			System.out.println("URL: " + connectionUrl);
    			System.out.println("Connection: " + con);
    			
    			// Get a Statement object
    			stmt = con.createStatement(
    				ResultSet.TYPE_SCROLL_INSENSITIVE,
    				ResultSet.CONCUR_READ_ONLY);
    
    			// Query the database, storing the result in an object of type ResultSet
    			rs = stmt.executeQuery("SELECT " +
    					"submissions.*, " +
    					"languages.djudge_name AS language_tsid, " +
    					"problems_int.djudge_contest AS problem_tcontest, " +
    					"problems_int.djudge_problem AS problem_tproblem, " +
    					"submissions_data.code AS source_code " +
    					"FROM submissions " +
    					"LEFT JOIN languages ON submissions.language_id = languages.id " +
    					"LEFT JOIN problems_int ON submissions.problem_id = problems_int.id " +
    					"LEFT JOIN submissions_data ON submissions.id = submissions_data.id " +
    					"WHERE djudge_status = 0");
    
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
    				desc.fTrial = 0;
    				
    				String req = "UPDATE submissions " +
    				" SET " + 
    				" djudge_status = -1 " + 
    				" WHERE id = " + desc.tid;
    				
    				Statement stmt2 = con.createStatement(
    	    				ResultSet.TYPE_SCROLL_INSENSITIVE,
    	    				ResultSet.CONCUR_READ_ONLY);
    				stmt2.executeUpdate(req);
    				
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
}
