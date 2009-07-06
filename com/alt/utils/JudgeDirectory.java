/* $Id: JudgeDirectory.java, v 0.1 2008/07/26 05:13:08 alt Exp $ */

/* Copyright (C) 2008 Olexiy Palinkash <olexiy.palinkash@gmail.com> */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.alt.utils;

import java.io.File;

import com.alt.djudge.judge.Judge;
import com.alt.djudge.judge.ProblemDescription;
import com.alt.utils.FileWorks;



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
					res.setProblemResult(i, Judge.judgeSourceFile(FileWorks.getAbsolutePath(files[i].getAbsolutePath()), "%AUTO%", desc));
		return res;
	}
}
