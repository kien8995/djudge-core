/* $Id$ */

package djudge.judge.dvalidator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import utils.FileWorks;

import djudge.judge.ProblemDescription;
import djudge.judge.checker.ValidationResult;
import djudge.judge.checker.Validator;

public class LocalValidator
{
	private static final Logger log = Logger.getLogger(LocalValidator.class);
	
	public static ValidationResult validate(ValidatorTask task)
	{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date()) + "_val";
        String workDir = FileWorks.getAbsolutePath("./work/" + id + "/") + "/";
        FileWorks.saveToFile(task.programOutput, workDir + "answer.txt");
        try
        {
        	ProblemDescription pd = new ProblemDescription(task.contestId, task.problemId);
        	ValidationResult res = new Validator(pd.getTestValidator(task.groupNumber, task.testNumber)).validateOutput(task.testInput.filename, task.testOutput.filename, workDir + "answer.txt");
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
