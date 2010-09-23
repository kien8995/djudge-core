/* $Id$ */

package djudge.judge.dcompiler;

import java.io.File;
import java.util.HashMap;

public class DistributedFileset
{
	public HashMap<String, DistributedFile> map = new HashMap<String, DistributedFile>();
	
	public DistributedFileset()
	{
		
	}
	
	public boolean addFile(String filename)
	{
		DistributedFile f = new DistributedFile(filename);
		map.put(f.filename, f);
		return true;
	}
	
	public boolean addFile(String filename, String addAs)
	{
		DistributedFile f = new DistributedFile(filename, addAs);
		map.put(f.filename, f);
		return true;
	}
	
	public DistributedFileset(String filename)
	{
		addFile(filename);
	}
	
	public DistributedFileset(String[] files)
	{
		for (String file : files)
		{
			addFile(file);
		}
	}
	
	public void readDirectory(String directory)
	{
		File f = new File(directory);
		for (File file : f.listFiles())
		{
			if (file.isFile())
			{
				addFile(file.getPath());
			}
		}
	}
	
	public void unpack(String directory)
	{
		String[] files = map.keySet().toArray(new String[0]);
		for (String file : files)
		{
			map.get(file).saveFile(directory);
		}
	}
	
	public String getFile()
	{
		return map.keySet().toArray(new String[0])[0];
	}
}
