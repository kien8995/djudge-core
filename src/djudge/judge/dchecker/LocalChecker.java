/* $Id$ */

package djudge.judge.dchecker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import utils.FileTools;

import djudge.common.JudgeDirs;
import djudge.judge.ProblemDescription;
import djudge.judge.checker.CheckerResult;
import djudge.judge.checker.Checker;

public class LocalChecker
{
	private static final Logger log = Logger.getLogger(LocalChecker.class);
	
	public static CheckerResult check(ValidatorTask task)
	{
		return check(task, null);
	}
	
	public static CheckerResult check(ValidatorTask task, String workDir)
	{
		if (workDir == null)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
			String id = dateFormat.format(new Date()) + "_val";
			workDir = JudgeDirs.getWorkDir() + id + "/";
		}
        FileTools.saveToFile(task.programOutput, workDir + "answer.txt");
        try
        {
        	ProblemDescription pd = new ProblemDescription(task.contestId, task.problemId);
        	CheckerResult res = new Checker(pd.getTestValidator(task.groupNumber, task.testNumber)).validateOutput(task.testInput.filename, workDir + "answer.txt", task.testOutput.filename);
        	return res;
        }
        catch (Exception e)
        {
        	log.error("Unknown exception", e);
		}
        // TODO: delete this?
        // FileWorks.deleteFile(workDir + "answer.txt");
        return null;
		
	}
}
