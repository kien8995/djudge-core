/* $Id$ */

package djudge.scripts;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import utils.FileTools;


public class UsacoContestCreator
{
	public static void collectProblem(String inDir, String outDir, String pname, int tCount)
	{
		System.out.println(pname + ": " + tCount + " " + outDir);
		if (!inDir.endsWith("/"))
			inDir = inDir + "/";
		if (!outDir.endsWith("/"))
			outDir = inDir + "/";
		File testsDir = new File(outDir + "tests/");
		File solsDir = new File(outDir + "solutions/");
		testsDir.mkdirs();
		solsDir.mkdirs();
		for (int t = 1; t <= tCount; t++)
		{
			String fnameIn = pname + "." + t + ".in";
			String fnameOut = pname + "." + t + ".out";
			FileTools.createLink(testsDir + "/" + fnameIn , inDir + fnameIn);
			FileTools.createLink(testsDir + "/" + fnameOut , inDir + fnameOut);
		}
		File problemXmlFile = new File(outDir + "problem.xml");
		PrintWriter pw;
		try
		{
			pw = new PrintWriter(problemXmlFile);
			pw.println("<problem");
			
			pw.println("    test-count    = \"" + tCount + "\"");
			pw.println("    input-mask    = \"" + pname + ".%d.in" + "\"");
			pw.println("    output-mask   = \"" + pname + ".%d.out" + "\"");
			pw.println("    input-file    = \"" + pname + ".in" + "\"");
			pw.println("    output-file   = \"" + pname + ".out" + "\"");
			pw.println("    time-limit    = \"2000\"");
			pw.println("    memory-limit  = \"256M\"");
			pw.println("    checker       = \"@STR\"");
			
			pw.println("/>");
/*
m
    test-count       = "15"
    input-mask       = "%02d"
    output-mask      = "%02d.a"
    time-limit       = "2000"
    memory-limit     = "64M"
    checker          = "@STR"
/>
 */
			
			pw.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}		
	}
	
	public static void parseDirectory(String inDir, String outDir)
	{
		if (!inDir.endsWith("/"))
			inDir = inDir + "/";
		if (!outDir.endsWith("/"))
			outDir = inDir + "/";
		HashMap<String, Integer> testsCount = new HashMap<String, Integer>();
		File dirIn = new File(inDir);
		for (File f : dirIn.listFiles())
			if (!f.isDirectory())
				if (f.getName().endsWith(".in"))
				{
    				String[] sa = f.getName().split("\\.");
    				int k = Integer.parseInt(sa[1]);
    				Integer prev = testsCount.get(sa[0]);
    				if (prev == null || prev < k)
    					testsCount.put(sa[0], k);
				}
		
		for (String pname: testsCount.keySet())
		{
			collectProblem(inDir, outDir + pname + "/", pname, testsCount.get(pname));
		}
	}
	
	public static void main(String[] args)
	{
		String id = "allopen10";
		parseDirectory("/home/alt/work/java/djudge/problems/usaco/" + id + "/", "/home/alt/" + id + "/");
	}
}
