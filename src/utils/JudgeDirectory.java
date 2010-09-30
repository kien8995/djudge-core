/* $Id$ */

package utils;


import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

import utils.FileWorks;

import djudge.judge.CheckParams;
import djudge.judge.Judge;
import djudge.judge.ProblemDescription;
import djudge.judge.SubmissionResult;

public class JudgeDirectory 
{
	private static final Logger log = Logger.getLogger(JudgeDirectory.class);
	
	ProblemDescription desc;
		
	public JudgeDirectory(ProblemDescription desc)
	{
		this.desc = desc;
	}

	public DirectoryResult judge(String directory)
	{
		log.info("Judging directory: " + directory);
		
		DirectoryResult res = new DirectoryResult(directory);
		
		File f = new File(directory);
		File[] files = f.listFiles();
		
		Arrays.sort(files);
		
		for (int i = 0; i < files.length; i++)
			if (!files[i].isDirectory())
				if (files[i].getName().charAt(0) != '_')
				{
					SubmissionResult sr = Judge.judgeSourceFile(FileWorks
							.getAbsolutePath(files[i].getAbsolutePath()),
							"%AUTO%", desc, new CheckParams());
					res.setProblemResult(i, sr);
				}
		return res;
	}
}
