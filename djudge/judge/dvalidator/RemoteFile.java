package djudge.judge.dvalidator;

import djudge.filesystem.RemoteFS;

public class RemoteFile
{
	public RemoteFile(String filename2)
	{
		fIsPresent = false;
		filename = filename2;
	}

	public RemoteFile()
	{
		// TODO Auto-generated constructor stub
	}

	public boolean fIsPresent;
	
	public String filename;
}
