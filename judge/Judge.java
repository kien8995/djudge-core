package judge;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import runner.Runner;
import runner.RunnerResult;
import runner.RunnerResultEnum;
import utils.FileWorks;
import validator.ValidationResult;

import common.JudgeException;
import common.JudgeExceptionType;
import common.settings;

public class Judge
{
	public static ProblemResult judgeProblem(String command, ProblemDescription problem)
	{
		return judgeProblem(command, problem, false, false);
	}
	
	public static ProblemResult judgeProblem(String command, ProblemDescription problem, boolean fFullTesting)
	{
		return judgeProblem(command, problem, fFullTesting, false);
	}
	
	public static ProblemResult judgeProblem(String command, ProblemDescription problem, boolean fFullTesting, boolean fFullResults)
	{
		ProblemResult res = new ProblemResult(problem);

		for (int i = 0; i < problem.groupsCount; i++)
		{
			GroupResult t = res.groupResults[i] = judgeGroup(command, problem.groups[i], fFullTesting, fFullResults);
			if (!fFullTesting && t.result != TestResultEnum.AC)
				break;
		}
		
		return res;
	}
	
	public static GroupResult judgeGroup(String command, GroupDescription group, boolean fFullTesting, boolean fFullResults)
	{
		GroupResult res = new GroupResult(group);
		
		for (int i = 0; i < group.testsCount; i++)
		{
			TestResult t = res.testResults[i] = judgeTest(command, group.tests[i], fFullResults);
			if (!fFullTesting && t.result != TestResultEnum.AC)
				break;
		}
		
		return res;
	}
	
	public static TestResult judgeTest(String command, TestDescription test, boolean fFullResults)
	{
		TestResult res = new TestResult(test.testNumber);
		
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date());
		
        try
        {
        	String input = test.problemInfo.problemRoot + "\\tests\\" + test.judgeInput;
        	String output = test.problemInfo.problemRoot + "\\tests\\" + test.judgeOutput;
        	res = judgeTest(id, command, input, output, test, fFullResults);
        }
        catch (Exception exc)
        {
        	res.systemMessage = exc.toString();
        	System.out.println(exc);
        }
		
		return res;
	}
	
	
	public static TestResult judgeTest(String solutionID, String command, String inputGlobalFile, String etalonGlobalFile, TestDescription desc, boolean fFullResults) throws JudgeException
	{
		TestResult res = new TestResult();
		File f;
		
		String globalTempDir = settings.getTempDir();
		String tempDir = globalTempDir + solutionID + "/";
		
		FileWorks.CopyFile(tempDir + FileWorks.getFileName(command), command);
		
        // global input & etalon files checking 
        f = new File(inputGlobalFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotFound, inputGlobalFile, "global input test #" + desc.getTestNumber());

        f = new File(etalonGlobalFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotFound, etalonGlobalFile, "etalon test #" + desc.getTestNumber());

        // local answer & input files
		String inputFile = tempDir + desc.getInputFilename();		
		String answerFile = tempDir + desc.getFiles().outputFilename;
        
        f = new File(inputFile);
        FileWorks.CopyFile(inputFile, inputGlobalFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotCreated, inputFile, "local input test #" + desc.getTestNumber());                
        
        // now all files are present, we can running 'command'
        
        f = new File(command);
        if (f.exists())
        {
        	String cexename = FileWorks.getFileName(command);
        	String new_commad = tempDir + cexename;
        	FileWorks.CopyFile(new_commad, command);
        	f = new File(new_commad);
        	if (f.exists())
        		command = new_commad;
        }
    	
        Runner run = new Runner(desc.getLimits(), desc.getFiles());
        run.saveOutputTo(tempDir + "runner.out");
    	ValidationResult validationInfo = new ValidationResult("No_validator");
    	RunnerResult runtimeInfo = run.run(command);
    		
    	if (runtimeInfo.state == RunnerResultEnum.OK)
    		validationInfo = desc.getValidator().Validate(inputGlobalFile, etalonGlobalFile, answerFile);
    		
    	res.setRuntimeInfo(runtimeInfo);
    	res.setValidationInfo(validationInfo);
        
		return res;
	}
	
}
