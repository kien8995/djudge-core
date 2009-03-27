/* $Id: DirectoryResult.java, v 0.1 2008/07/28 05:13:08 alt Exp $ */

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

package utils;

import java.util.ArrayList;

import judge.SubmissionResult;

public class DirectoryResult 
{
	String directory;
	
	ArrayList<SubmissionResult> res;
	
	public DirectoryResult(String directory)
	{
		this.directory = directory;
		res = new ArrayList<SubmissionResult>();
	}
	
	public int getFilesCount()
	{
		return res.size();
	}
	
	public String getDirectory()
	{
		return directory;
	}
	
	public SubmissionResult getSubmissionResult(int k)
	{
		return res.get(k);
	}
	
	public void setProblemResult(int k, SubmissionResult pres)
	{
		if (res.size() > k)
			res.set(k, pres);
		else
			res.add(pres);
	}
	
	
}
