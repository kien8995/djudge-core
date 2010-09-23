/* $Id$ */

package utils;

import java.io.File;

import utils.FileWorks;

import djudge.judge.CheckParams;
import djudge.judge.Judge;
import djudge.judge.ProblemDescription;
import djudge.judge.SubmissionResult;



public class JudgeDirectory 
{
	ProblemDescription desc;
		
	public JudgeDirectory(ProblemDescription desc)
	{
		this.desc = desc;
	}

	public DirectoryResult judge(String Directory)
	{
		DirectoryResult res = new DirectoryResult(Directory);
		
		File f = new File(Directory);
		File[] files = f.listFiles();
		
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
