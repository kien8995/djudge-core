package judge;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("unused")
public class Temp
{
	public static void main(String arg[])
	{
		Judge d = new Judge();
		
		String cmd = "D:\\a.exe";
		ProblemDescription desc = new ProblemDescription("NEERC-1998", "A");
		
		desc.print();
		
		ProblemResult res = Judge.judgeProblem(cmd, desc, true);
		
		System.out.println(desc);
		System.out.println("Fin");
	}
}
