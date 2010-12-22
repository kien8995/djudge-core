/* $Id$ */

package utils;

import java.util.ArrayList;
import java.io.*;

import org.apache.log4j.Logger;

import djudge.common.Deployment;
import djudge.common.Review;
import djudge.judge.dchecker.RemoteFile;

public class FileTools
{
	private static final Logger log = Logger.getLogger(FileTools.class);

	public static boolean deleteFile(String filename)
	{
		File f = new File(filename);
		return f.delete();
	}

	public static String getFileExtension(String file)
	{
		File f = new File(file);
		String name = f.getName();
		int k = name.indexOf('.');
		if (k < 0)
			return "";
		return name.substring(k, name.length());
	}

	@Review
	public static String getAbsolutePath(String relativePath)
	{
		if (Deployment.isOSWinNT())
			relativePath = relativePath.replace("/", "\\");
		File f = new File(relativePath);
		// FIXME
		String res = f.getAbsolutePath().replace("\\.", "").replace("/.", "");
		return res;
	}

	public static String getNameOnly(String filePathName)
	{
		String name = (new File(filePathName)).getName();
		String ext = getFileExtension(name);
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
		if (!file.exists() || !file.isDirectory())
			return new String[0];
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

	@Review
	public static String concatPaths(String dir, String file)
	{
		return (dir + "/" + file).replace("\\\\", "\\").replace("\\.", "")
				.replace("//", "/");
	}

	/*
	 * Creates symbolic link from destFilename to srcFilename on Linux - creates
	 * link other platforms - copies file srcFilename to destFilename TODO: on
	 * Windows NTFS - try to create symlink too
	 */
	public static boolean createLink(String destFilename, String srcFilename)
	{
		// Trying to create link
		if (Deployment.isOSLinux() && Deployment.useLinks())
		{
			File f = new File(destFilename);
			f.getParentFile().mkdir();

			String[] command = new String[]
			{ "ln", srcFilename, destFilename };

			try
			{
				Process process = Runtime.getRuntime().exec(command);
				process.waitFor();
				if (process.exitValue() != 0)
					throw new UnsupportedOperationException();
				return true;
			}
			catch (Exception ex)
			{
				log.warn("Link creation failed (from " + srcFilename + " to " + destFilename + ")");
			}
		}
		if (Deployment.isOSWinNT() && Deployment.useLinks())
		{
			File f = new File(destFilename);
			f.getParentFile().mkdir();

			String[] command = new String[]{ "fsutil", "hardlink", "create", destFilename , srcFilename};

			try
			{
				Process process = Runtime.getRuntime().exec(command);
				process.waitFor();
				if (process.exitValue() != 0)
					throw new UnsupportedOperationException();
				return true;
			}
			catch (Exception ex)
			{
				log.warn("Link creation failed (from " + srcFilename + " to " + destFilename + ")");
			}
		}
		// else trying to copy file
		return copyFile(destFilename, srcFilename);
	}

	public static boolean copyFile(String destFilename, String srcFilename,
			boolean setExecutable)
	{
		try
		{
			File scrFile = new File(srcFilename);
			if (!scrFile.exists())
			{
				throw new FileNotFoundException("Cannot find source file");
			}

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

			return true;
		}
		catch (FileNotFoundException ex)
		{
			log.warn(ex.getMessage() + " in the specified directory.", ex);
		}
		catch (IOException ex2)
		{
			log.error("IOException", ex2);
		}
		return false;
	}

	public static boolean copyFile(String destFilename, String srcFilename)
	{
		return copyFile(destFilename, srcFilename, false);
	}

	public static void writeFileContent(String filename, byte[] content)
	{
		try
		{
			File f = new File(filename);
			f.getParentFile().mkdirs();
			OutputStream out = new FileOutputStream(f);
			out.write(content, 0, content.length);
			out.close();
		}
		catch (IOException ex)
		{
			log.error("IOException", ex);
		}
	}

	public static void writeFileContent(String filename, String content)
	{
		try
		{
			File f = new File(filename);
			f.getParentFile().mkdirs();
			PrintWriter pw = new PrintWriter(f);
			pw.println(content);
			pw.close();
		}
		catch (IOException ex)
		{
			log.error("IOException", ex);
		}
	}

	public static byte[] readFileContent(String file)
	{
		byte[] res = null;
		try
		{
			File f = new File(file);
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(f));
			res = new byte[(int) f.length()];
			in.read(res);
			in.close();
		}
		catch (FileNotFoundException ex)
		{
			log.error(ex.getMessage() + " in the specified directory.", ex);
		}
		catch (IOException ex)
		{
			log.error("IOException", ex);
		}
		return res;
	}

	public static boolean saveToFile(String content, String filename)
	{
		try
		{
			File f = new File(filename);
			f.getParentFile().mkdirs();
			PrintWriter writer = new PrintWriter(new FileOutputStream(filename));
			writer.print(content);
			writer.close();
			return true;
		}
		catch (FileNotFoundException ex)
		{
			log.error(ex.getMessage() + " in the specified directory.", ex);
		}
		return false;
	}

	public static boolean saveToFile(RemoteFile file, String filename)
	{
		// if file content is in memory
		if (file.fIsPresent)
		{
			return saveToFile(file.filename, filename);
		}
		
		// file on disk - creating link on it
		return createLink(filename, file.filename);
	}

	public static String readFile(String filename)
	{
		StringBuilder res = new StringBuilder();
		try
		{
			BufferedReader r = new BufferedReader(new FileReader(new File(
					filename)));
			String str = null;
			while ((str = r.readLine()) != null)
			{
				res.append(str);
				res.append('\n');
			}
			r.close();
		}
		catch (FileNotFoundException ex)
		{
			log.error(ex.getMessage() + " in the specified directory.", ex);
		}
		catch (IOException ex)
		{
			log.error("IOException", ex);
		}
		return res.toString();
	}

	public static void deleteDirectory(String directory)
	{
		File f = new File(directory);
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
