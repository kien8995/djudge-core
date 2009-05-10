/* $Id: Language.java, v 0.1 2008/07/22 05:13:08 alt Exp $ */

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

package com.alt.djudge.judge.compiler;


import java.io.BufferedReader;
import java.io.FileReader;
import org.w3c.dom.*;

import com.alt.djudge.judge.common_data_structures.RunnerFiles;
import com.alt.djudge.judge.common_data_structures.RunnerLimits;
import com.alt.djudge.judge.runner.Runner2;
import com.alt.djudge.judge.runner.RunnerResult;
import com.alt.utils.FileWorks;





/**
 * Describes programming language
 * @author alt
 *
 */
public class Language
{
	LanguageInfoInternal info;
	
	public LanguageInfoInternal getLanguageInfo()
	{
		return info;
	}
	
	public String getExtension()
	{
		return info.getExtension();
	}
	
	Language(Element lang)
	{
		info = new LanguageInfoInternal(lang);
	}
	
	public void ShowInfo()
	{
		info.showInfo();
	}
	
	public String getID()
	{
		return info.getID();
	}
	
	public CompilationInfo Compile(String file)
	{
		CompilationInfo res = new CompilationInfo();
		try
		{
			String currDir = System.getProperty("user.dir");
			String name = FileWorks.getNameOnly(file);
			String namePath = currDir + "\\temp\\" + name;
			
			FileWorks.CopyFile(namePath + info.getExtension(), file);
			
			StringBuffer cmd = new StringBuffer(info.getCompileCommand());
			int i1;
			while ((i1 = cmd.indexOf("%name")) != -1)
				cmd.replace(i1, i1 + 5, namePath);
			while ((i1 = cmd.indexOf("%ext")) != -1)
				cmd.replace(i1, i1 + 4, info.getExtension());
			
			String compOutput = FileWorks.getAbsolutePath(".//temp//compiler.output"); 
			
			RunnerFiles files = new RunnerFiles(compOutput);
			
			RunnerLimits limits = new RunnerLimits(20000, 256 * 1024 * 1024);
			
			Runner2 run = new Runner2(limits, files);
			
			RunnerResult rres = run.run(cmd.toString());
			
			res.compilerExitCode = rres.exitCode;
			res.command = info.getRunCommand().replace("%name", (info.getID().equals("JAVA") ? name : namePath));

			if (res.compilerExitCode == 0)
				res.state = CompilationResult.OK;
			else
				res.state = CompilationResult.CompilationError;
			
			BufferedReader out = new BufferedReader(new FileReader(compOutput));				
			String line;
			
			while ((line = out.readLine()) != null)
				res.compilerOutput.add(line);
			out.close();
			
		}
		catch (Exception exc)
		{
			System.out.println("!!! Exception catched (while compiling): " + exc);
		}
		return res;
	}
}
