/* $Id$ */

package djudge.judge.dcompiler;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DistributedFileset
{
	private static Logger log = Logger.getLogger(DistributedFileset.class);
	
	public HashMap<String, DistributedFile> map = new HashMap<String, DistributedFile>();
	
	public DistributedFileset()
	{
		
	}
	
	public boolean addFile(String filename)
	{
		log.trace("Adding file `" + filename + "' to fileset");
		DistributedFile f = new DistributedFile(filename);
		map.put(f.filename, f);
		return true;
	}
	
	public boolean addFile(String filename, String addAs)
	{
		log.trace("Adding file `" + filename + "' to fileset under name `" + addAs + "'");
		DistributedFile f = new DistributedFile(filename, addAs);
		map.put(f.filename, f);
		return true;
	}
	
	public DistributedFileset(String filename)
	{
		log.trace("Creating DistributedFileset with one file:");
		addFile(filename);
	}
	
	public DistributedFileset(String[] files)
	{
		log.trace("Creating DistributedFileset multiple files");
		for (String file : files)
		{
			addFile(file);
		}
	}
	
	public void readDirectory(String directory)
	{
		log.trace("Reading directore `" + directory + "' into fileset");
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
		log.trace("Unpacking fileset into directory `" + directory + "'");
		File f = new File(directory);
		f.mkdirs();
		String[] files = map.keySet().toArray(new String[0]);
		for (String file : files)
		{
			System.out.println("Saving " + file);
			map.get(file).saveFile(directory);
		}
	}
	
	public String getFile()
	{
		log.trace("Trying to get default file");
		return map.keySet().toArray(new String[0])[0];
	}
}
