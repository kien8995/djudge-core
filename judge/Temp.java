package judge;

public class Temp
{
	public static void main(String arg[])
	{
		Judge d = new Judge();
		
		String cmd = "D:\\Temp\\Work\\eJudge\\DJudge\\tests\\runner\\Test_Runner_Crash_AccessViolation.exe";
		//String cmd = "java.exe";
		
		try
		{
			d.judgeTest("new", cmd, "d:/in", "d:/et", new TestDescription());
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		@SuppressWarnings("unused")
		ProblemDescription desc = new ProblemDescription("NEERC-2007", "A");
	}
}
