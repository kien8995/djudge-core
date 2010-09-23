/* $Id$ */

package djudge.judge.dcompiler;

import djudge.filesystem.RemoteFS;
import djudge.judge.dvalidator.RemoteFile;
import utils.FileWorks;

public class DistributedFile
{
	public String filename;

	public String fsName;
	
	public DistributedFile(String filename)
	{
		loadFile(filename);
	}
	
	public DistributedFile(String filename, String addAs)
	{
		loadFile(filename, addAs);
	}
	
	public boolean loadFile(String filename, String addAs)
	{
		String name = FileWorks.getFileName(addAs);
		this.filename = name;
		fsName = RemoteFS.saveToRemoteStorage(new RemoteFile(filename));
		return true;
	}
	
	public boolean loadFile(String filename)
	{
		return loadFile(filename, filename);
	}
	
	public boolean saveFile(String rootDirectory)
	{
		//FileWorks.writeFileContent(FileWorks.ConcatPaths(rootDirectory, filename), content);
		FileWorks.CopyFile(FileWorks.ConcatPaths(rootDirectory, filename), fsName);
		return true;
	}
}
