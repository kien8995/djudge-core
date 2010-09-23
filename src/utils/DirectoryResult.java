/* $Id$ */

package utils;

import java.util.ArrayList;

import djudge.judge.SubmissionResult;

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
