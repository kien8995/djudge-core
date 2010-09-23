package djudge.judge.dcompiler;

public class CompilerTask
{
	/*
	 * Files to compile
	 */
	DistributedFileset files;
	
	/*
	 * Language ID
	 */
	String languageId;
	
	//String mainFile;
	
	public CompilerTask()
	{
		// TODO Auto-generated constructor stub
	}
	
	public CompilerTask(String filename, String languageId)
	{
		files = new DistributedFileset(filename);
		this.languageId = languageId;
	}
		
}
