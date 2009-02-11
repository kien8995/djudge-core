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
import common_data_structures.RunnerFiles;
import compiler.CompilationInfo;
import compiler.Compiler;

public class Judge
{
	public static JudgeTaskResult judgeTask(JudgeTaskDescription task)
	{
		JudgeTaskResult res = new JudgeTaskResult();
		res.desc = task;
		ProblemDescription pd;
		
		try
		{
			pd = new ProblemDescription(task.tcontest, task.tproblem);
		}
		catch (Exception e)
		{
			e.getStackTrace();
			return res;
		}
		
		String filesrc = settings.getTempDir() + task.tid + ".xml";
		
		FileWorks.saveToFile(task.tsourcecode, filesrc);
		
		try
		{
			res.res = judgeSourceFile(filesrc, task.tlanguage, pd, task.fTrial == 1);
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
		
		return res;
	}
	
	public static SubmissionResult judgeSourceFile(String file, String lang, ProblemDescription problem, boolean fTrial)
	{
		System.out.println("Trial: " + fTrial);
		SubmissionResult res = new SubmissionResult(problem);
		String fname = FileWorks.getFileName(file);
		FileWorks.CopyFile(settings.getWorkDir() + fname, file);
		CompilationInfo ci = Compiler.Compile(settings.getWorkDir() + fname, lang);
		res.setCompilationInfo(ci);
		if (!ci.isSuccessfull())
		{
			System.out.println(ci.getCompilerOutput());
			res.result = TestResultEnum.CE;
		}
		else
		{
			System.out.println("Trial: " + fTrial);
			if (fTrial)
				res.setProblemResult(judgeProblemTrial(ci.getCommand(), problem));
			else
				res.setProblemResult(judgeProblem(ci.getCommand(), problem));
		}
		return res;
	}
	
	public static ProblemResult judgeProblem(String command, ProblemDescription problem)
	{
		return judgeProblem(command, problem, true, false);
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
		res.updateResult();
		
		return res;
	}
	
	public static ProblemResult judgeProblemTrial(String command, ProblemDescription problem)
	{
		System.out.println("Trial: " + true);
		ProblemResult res = new ProblemResult(problem);
		res.groupCount = 1;
		res.groupResults = new GroupResult[1];

		res.groupResults[0] = judgeGroup(command, problem.groups[0], false, false);
		res.updateResult();
		
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
		res.updateResult();
		
		return res;
	}
	
	public static TestResult judgeTest(String command, TestDescription test, boolean fFullResults)
	{
		TestResult res = new TestResult(test);
		
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date());
		
        try
        {
        	String input = (test.judgeInput != null && test.judgeInput != "") ? test.problemInfo.problemRoot + "\\tests\\" + test.judgeInput : "";
        	String output = (test.judgeOutput != null && test.judgeOutput != "") ? test.problemInfo.problemRoot + "\\tests\\" + test.judgeOutput : "";
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
		TestResult res = new TestResult(desc);
		File f;
		
		String globalTempDir = settings.getWorkDir();
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
        
        desc.getFiles().print();
        
		String inputFile = tempDir + (desc.getInputFilename() != null ? desc.getInputFilename() : "input.txt");
		String answerFile = tempDir + (desc.getAnswerFilename() != null ? desc.getAnswerFilename() : "output.txt");
        
    	System.out.println("Input: " + inputFile);
		
		
		//System.out.println(desc.);
		
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
        		command = cexename;
        }
    	
        RunnerFiles files = desc.getFiles();
        
        files.rootDirectory = tempDir;
        
        Runner run = new Runner(desc.getLimits(), files);
        run.saveOutputTo(tempDir + "runner.out");
    	ValidationResult validationInfo = new ValidationResult("No_validator");
    	RunnerResult runtimeInfo = run.run(command);
		runtimeInfo.output = (int)new File(answerFile).length();
    	
    	if (runtimeInfo.state == RunnerResultEnum.OK)
    		validationInfo = desc.getValidator().Validate(inputGlobalFile, etalonGlobalFile, answerFile);
    		
    	res.setRuntimeInfo(runtimeInfo);
    	res.setValidationInfo(validationInfo);
        
		return res;
	}
	
}
