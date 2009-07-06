package com.alt.djudge.judge.dvalidator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alt.djudge.judge.ProblemDescription;
import com.alt.djudge.judge.validator.ValidationResult;
import com.alt.utils.FileWorks;

public class LocalValidator
{
	public ValidationResult validate(ValidatorTask task)
	{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date()) + "_val";
        String workDir = FileWorks.getAbsolutePath("./work/" + id + "/") + "/";
        FileWorks.saveToFile(task.programOutput, workDir + "answer.txt");
		ProblemDescription pd = new ProblemDescription(task.contestId, task.problemId);
		ValidationResult res = pd.getValidator().Validate(task.testInput, task.testOutput, workDir + "answer.txt");		
		return res;
	}
}
