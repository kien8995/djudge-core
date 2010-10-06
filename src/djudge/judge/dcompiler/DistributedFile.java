/* $Id$ */

package djudge.judge.dcompiler;

import djudge.judge.dchecker.RemoteFile;
import djudge.remotefs.RemoteFS;
import utils.FileTools;

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
		String name = FileTools.getFileName(addAs);
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
		// TODO test this
		return FileTools.createLink(FileTools.concatPaths(rootDirectory, filename), fsName);
		//FileWorks.copyFile(FileWorks.concatPaths(rootDirectory, filename), fsName);
		//return true;
	}
}
