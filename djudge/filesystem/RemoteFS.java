package djudge.filesystem;

import java.io.File;
import java.util.Random;
import java.util.Vector;

import djudge.judge.dvalidator.RemoteFile;
import utils.FileWorks;

// FIXME: this is just a stub
public class RemoteFS
{
	private static Vector<String> files = new Vector<String>();
	
	public static void startSession()
	{
		files = new Vector<String>();
	}
	
	public static void clearSession()
	{
		for (int i = 0; i < files.size(); i++)
			FileWorks.deleteFile(files.get(i));
		files.clear();
	}
	
	public static String getUID()
	{
		StringBuffer s = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < 50; i++)
			s.append((char)('A' + (rand.nextInt() % 26 + 26)%26));
		return s.toString();
	}
	
	public static String saveToRemoteStorage(RemoteFile file)
	{
		String name = getUID();
		while (new File("./temp/" + name).exists())
		{
			name = getUID();
		}
		//System.out.println("Saving " + file.filename + " to " + "./temp/" + name);
		FileWorks.saveToFile(file, "./temp/" + name);
		files.add("./temp/" + name);
		return "./temp/" + name;
	}
	
	public static String readContent(String filename)
	{
		return FileWorks.readFile(filename);
	}
	
	public static boolean writeContent(String content, String filename)
	{
		FileWorks.saveToFile(content, filename);
		return true;
	}
}
