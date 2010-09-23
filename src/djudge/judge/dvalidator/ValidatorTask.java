/* $Id$ */

package djudge.judge.dvalidator;

public class ValidatorTask
{
	public String contestId;
	
	public String problemId;
	
	public int groupNumber;
	
	public int testNumber;
	
	public RemoteFile programOutput;
	
	public RemoteFile testInput;
	
	public RemoteFile testOutput;
	
	public ValidatorTask()
	{
		programOutput = new RemoteFile();
		testInput = new RemoteFile();
		testOutput = new RemoteFile();
	}
}
