package com.alt.djudge.judge;

import com.alt.djudge.dservice.DServiceTask;

public class JudgeTaskDescription
{
	public String tproblem;
	public String tcontest;
	public int tid;
	public int fTrial;
	public String tlanguage;
	public String tsourcecode;
	
	public JudgeTaskDescription()
	{
		
	}

	public JudgeTaskDescription(DServiceTask task)
	{
		tproblem = task.getProblem();
		tcontest = task.getContest();
		fTrial = 0;
		tlanguage = task.getLanguage();
		tsourcecode = task.getSource();
		tid = task.getID();
	}
}
