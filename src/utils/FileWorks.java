/* $Id$ */

package utils;

import java.io.*;
import java.util.ArrayList;

import djudge.judge.dvalidator.RemoteFile;

public class FileWorks
{
	public static boolean deleteFile(String filename)
	{
		File f = new File(filename);
		return f.delete();
	}
	
	public static String getExtension(String file)
	{
		File f = new File(file);
		String name = f.getName();
		int k = name.indexOf('.');
		if (k < 0)
			return "";
		return name.substring(k, name.length());
	}
	
	public static String getAbsolutePath(String relativePath)
	{
		File f = new File(relativePath);
		// FIXME
		String res = f.getAbsolutePath().replace("\\.", "").replace("/.", "");
		return res;
	}
	
	public static String getNameOnly(String filePathName)
	{
		String name = (new File(filePathName)).getName();
		String ext = getExtension(name);
		return name.substring(0, name.length() - ext.length());
	}
	
	public static String getFileName(String filePathName)
	{
		return (new File(filePathName)).getName();
	}
	
	public static void clearDirectory(String dir)
	{
		clearDirectory(dir, new ArrayList<String>());
	}
	
	public static String[] getDirectoryListing(String dir)
	{
		File file = new File(dir);
		if (!file.exists() || !file.isDirectory()) return new String[0];
		return file.list();
	}
	
	public static void clearDirectory(String dir, ArrayList<String> excl)
	{
		File f1 = new File(dir);
		if (f1.isDirectory())
		{
			File files[] = f1.listFiles();
			for (int i = 0; i < files.length; i++)
				if (files[i].isFile() && !excl.contains(files[i].getName()))
					files[i].delete();
		}
	}
	
	public static String concatPaths(String dir, String file)
	{
		return (dir + "/" + file).replace("\\\\", "\\").replace("\\.", "").replace("//", "/");
	}
	
	public static void copyFile(String destFilename, String srcFilename, boolean setExecutable)
	{
		try
		{
			File scrFile = new File(srcFilename);
			if (!scrFile.exists())
				throw new FileNotFoundException("Cannot find source file");
			
			File destFile = new File(destFilename);
			destFile.getParentFile().mkdirs();
			
			InputStream in = new FileInputStream(scrFile);
			OutputStream out = new FileOutputStream(destFile);
			
			// TODO: review buffer size
			byte[] buf = new byte[64 * 1024];
			int len;
			
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			
			in.close();
			out.close();
			
			destFile.setExecutable(setExecutable | scrFile.canExecute());
		}
		catch(FileNotFoundException ex)
	    {
			System.out.println(ex.getMessage() + " in the specified directory.");
	    }
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());      
	    }
	}
	
	public static void copyFile(String destFilename, String srcFilename)
	{
		copyFile(destFilename, srcFilename, false);
	}
	
	public static void writeFileContent(String file, byte[] content)
	{
		try
		{
			//System.out.println(file.toString());
			File f = new File(file);
			f.getParentFile().mkdirs();
			OutputStream out = new FileOutputStream(f);
			out.write(content, 0, content.length);
			out.close();
		}
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());      
	    }
	}
	
	public static void writeFileContent(String file, String content)
	{
		try
		{
			File f = new File(file);
			f.getParentFile().mkdirs();
			PrintWriter pw = new PrintWriter(f);
			pw.println(content);
			pw.close();
		}
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());      
	    }
	}
	
	public static byte[] readFileContent(String file)
	{
		byte[] res = null;
		try
		{
			File f = new File(file);
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			res = new byte[(int)f.length()];
			in.read(res);
			in.close();
		}
		catch(FileNotFoundException ex)
	    {
			System.out.println(ex.getMessage() + " in the specified directory.");
	    }
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());      
	    }
	    return res;
	}
	
	public static void saveToFile(String s, String filename)
	{
		try
		{
			File f = new File(filename);
			f.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileOutputStream(filename));
            writer.print(s);
            writer.close();
		}
		catch(FileNotFoundException ex)
	    {
			System.out.println(ex.getMessage() + " in the specified directory.");
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Exception in FileWorks.saveToFile: " + e.getMessage());
	    	e.printStackTrace();
	    }		
	}
	
	public static void saveToFile(RemoteFile file, String filename)
	{
		if (file.fIsPresent)
		{
			saveToFile(file.filename, filename);
			return;
		}
		try
		{
			copyFile(filename, file.filename);
		}
	    catch(Exception e)
	    {
	    	System.out.println("Exception in FileWorks.saveToFile: " + e.getMessage());
	    	e.printStackTrace();
	    }		
	}
	
	public static String readFile(String filename)
	{
		StringBuilder res = new StringBuilder();
		try
		{
            BufferedReader r = new BufferedReader(new FileReader(new File(filename)));
            String str = null;
            while ((str = r.readLine()) != null)
            {
            	res.append(str);
            	res.append('\n');
            }
            r.close();
		}
		catch(FileNotFoundException ex)
	    {
			System.out.println(ex.getMessage() + " in the specified directory.");
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e.getMessage());      
	    }
	    return res.toString();
	}
	
	public static void deleteDirectory(String dir)
	{
		File f = new File(dir);
		if (f.isDirectory())
		{
			File[] files = f.listFiles();
			for (File file : files)
			{
				if (file.isDirectory())
				{
					deleteDirectory(file.getAbsolutePath());
				}
				else
				{
					file.delete();
				}
			}
			f.delete();
		}
	}
}
