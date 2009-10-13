package djudge.judge;

import org.apache.log4j.Logger;

import utils.DirectoryResult;
import utils.FileWorks;
import utils.HtmlWorks;
import utils.JudgeDirectory;

import djudge.common.JudgeDirs;
import djudge.filesystem.RemoteFS;
import djudge.judge.dcompiler.Compiler;
import djudge.judge.dcompiler.CompilerResult;
import djudge.judge.dcompiler.CompilerTask;
import djudge.judge.dexecutor.ExecutionResult;
import djudge.judge.dexecutor.ExecutionResultEnum;
import djudge.judge.dexecutor.ExecutorFiles;
import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.dexecutor.ExecutorProgram;
import djudge.judge.dexecutor.ExecutorTask;
import djudge.judge.dexecutor.LocalExecutor;
import djudge.judge.dvalidator.LocalValidator;
import djudge.judge.dvalidator.RemoteFile;
import djudge.judge.dvalidator.ValidatorTask;
import djudge.judge.validator.ValidationResult;

public class Judge
{
	private static final Logger log = Logger.getLogger(Judge.class);
	
	/*
	public static void checkProblem(String contestId, String problemId)
	{
		ProblemDescription desc = new ProblemDescription(contestId, problemId);
		JudgeDirectory jd = new JudgeDirectory(desc);
		DirectoryResult res = jd.judge(desc.problemRoot + "solutions");
		String s = HtmlWorks.directoryResultToHtml(res);
		FileWorks.saveToFile(s, desc.problemRoot + "rep.html");
	}
	
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
		SubmissionResult res = new SubmissionResult(problem);
		res.comment = file;
		CompilationInfo ci = Compiler.compile(new CompilerTask(file, lang));
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
				res.setProblemResult(judgeProblemTrial(ci, problem));
			else
				res.setProblemResult(judgeProblem(ci, problem));
		}
		return res;
	}
	
	public static ProblemResult judgeProblem(CompilationInfo cinfo, ProblemDescription problem)
	{
		return judgeProblem(cinfo, problem, true, false);
	}
	
	public static ProblemResult judgeProblem(CompilationInfo cinfo, ProblemDescription problem, boolean fFullTesting, boolean fFullResults)
	{
		ProblemResult res = new ProblemResult(problem);

		for (int i = 0; i < problem.groupsCount; i++)
		{
			GroupResult t = res.groupResults[i] = judgeGroup(cinfo, problem.groups[i], fFullTesting, fFullResults);
			if (!fFullTesting && t.result != TestResultEnum.AC)
				break;
		}
		res.updateResult();
		
		return res;
	}
	
	public static ProblemResult judgeProblemTrial(CompilationInfo command, ProblemDescription problem)
	{
		System.out.println("Trial: " + true);
		ProblemResult res = new ProblemResult(problem);
		res.groupCount = 1;
		res.groupResults = new GroupResult[1];

		res.groupResults[0] = judgeGroup(command, problem.groups[0], false, false);
		res.updateResult();
		
		return res;
	}
	
	public static GroupResult judgeGroup(CompilationInfo cinfo, GroupDescription group, boolean fFullTesting, boolean fFullResults)
	{
		GroupResult res = new GroupResult(group);
		
		for (int i = 0; i < group.testsCount; i++)
		{
			TestResult t = res.testResults[i] = judgeTest(cinfo, group.tests[i], fFullResults);
			if (!fFullTesting && t.result != TestResultEnum.AC)
				break;
		}
		res.updateResult();
		
		return res;
	}
	
	public static TestResult judgeTest(CompilationInfo cinfo, TestDescription test, boolean fFullResults)
	{
		TestResult res = new TestResult(test);
		
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date());
		
        try
        {
        	String input = (test.judgeInput != null && test.judgeInput != "") ? test.problemInfo.problemRoot + "\\tests\\" + test.judgeInput : "";
        	String output = (test.judgeOutput != null && test.judgeOutput != "") ? test.problemInfo.problemRoot + "\\tests\\" + test.judgeOutput : "";
        	res = judgeTest(id, cinfo, input, output, test, fFullResults);
        }
        catch (Exception exc)
        {
        	res.systemMessage = exc.toString();
        	System.out.println(exc);
        }
		
		return res;
	}
	
	public static RunnerResult generateTestAnswer(TestDescription desc, String command)
	{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date());
        
        RunnerResult res = null;
        
        try
        {
        	res = generateOutput(id, command,
        			FileWorks.ConcatPaths(desc.problemInfo.problemRoot + "tests", desc.judgeInput), 
        			FileWorks.ConcatPaths(desc.problemInfo.problemRoot + "tests", desc.judgeOutput), 
        			desc);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
		}
		return res;
	}
	
	public static RunnerResult generateOutput(String solutionID, String command, String inputFile, String answerFile, TestDescription desc) throws JudgeException
	{
		File f;
		
		String globalTempDir = settings.getWorkDir();
		String tempDir = globalTempDir + solutionID + "/";
		
		FileWorks.CopyFile(tempDir + FileWorks.getFileName(command), command);
		
		System.out.println("Input: " + inputFile);
        f = new File(inputFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotFound, inputFile, "global input test #" + desc.getTestNumber());

        desc.getFiles().print();
        
		String inputFileLocal = tempDir + (desc.getInputFilename() != null ? desc.getInputFilename() : "input.txt");
		String answerFileLocal = tempDir + (desc.getAnswerFilename() != null ? desc.getAnswerFilename() : "output.txt");
		
    	System.out.println("Input: " + inputFileLocal);
		
        f = new File(inputFileLocal);
        FileWorks.CopyFile(inputFileLocal, inputFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotCreated, inputFileLocal, "local input test #" + desc.getTestNumber());                
        
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
    	
        ExecutorFiles files = desc.getFiles();
        files.rootDirectory = tempDir;
        Runner run = new Runner(desc.getLimits(), files);
        
        run.saveOutputTo(tempDir + "runner.out");
    	RunnerResult res = run.run(command);
    	
    	if (res.state == RunnerResultEnum.OK)
    		FileWorks.CopyFile(answerFile, answerFileLocal);
    	
		return res;		
	}
	
	public static TestResult judgeTest(String solutionID, CompilationInfo cinfo, String inputGlobalFile, String etalonGlobalFile, TestDescription desc, boolean fFullResults) throws JudgeException
	{
		TestResult res = new TestResult(desc);
		File f;
		
		String globalTempDir = settings.getWorkDir();
		String tempDir = globalTempDir + solutionID + "/";
		
		
		
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
		
        f = new File(inputFile);
        FileWorks.CopyFile(inputFile, inputGlobalFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotCreated, inputFile, "local input test #" + desc.getTestNumber());                
        
        // now all files are present, we can running 'command'
        
        if (f.exists())
        {
        	String cexename = FileWorks.getFileName(command);
        	String new_commad = tempDir + cexename;
        	FileWorks.CopyFile(new_commad, command);
        	f = new File(new_commad);
        	if (f.exists())
        		command = cexename;
        }
    	
        ExecutorFiles files = desc.getFiles();
        
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
        
    	FileWorks.deleteFile(inputFile);
    	//FileWorks.deleteFile(command);
    	
		return res;
	}*/
	
	public static ProblemResult judgeProblem(ProblemDescription desc, ParamsOverride params, ExecutorProgram program)
	{
		ProblemResult res = new ProblemResult(desc);

		for (int i = 0; i < desc.groupsCount; i++)
		{
			res.groupResults[i] = judgeGroup(desc.groups[i], params, program);
		}
		res.updateResult();
		
		return res;
	}
	
	public static GroupResult judgeGroup(GroupDescription desc, ParamsOverride params, ExecutorProgram program)
	{
		GroupResult res = new GroupResult(desc);
		
		for (int i = 0; i < desc.getTestCount(); i++)
		{
			res.testResults[i] = judgeTest(desc.tests.get(i), params,  program);
		}
		res.updateResult();
		
		return res;		
	}
	
	public static TestResult judgeTest(TestDescription test, ParamsOverride params, ExecutorProgram program)
	{
		TestResult res = new TestResult(test);
		String contestId = test.problemInfo.contestID;
		String problemId = test.problemInfo.problemID;
		String testsDir = "./problems/" + contestId + "/" + problemId + "/" + "tests/";
		
		String inputTestFilename = test.getInputFilename();
		//System.out.println(inputTestFilename);
		if ((inputTestFilename == null) || ("".equals(inputTestFilename)))
		{
			inputTestFilename = "input.txt";
		}
		program.files.addFile(testsDir + test.getInputMask(), inputTestFilename);
		
		ExecutorFiles files = test.getExecutorFiles();
		ExecutorLimits limits = test.getActualLimits();
		ExecutorTask exTask = new ExecutorTask(program, limits, files);
		exTask.returnDirectoryContent = false;
		LocalExecutor ex = new LocalExecutor();
		
		ExecutionResult exRes = ex.execute(exTask);
		res.setRuntimeInfo(exRes);
		if (exRes.getResult() == ExecutionResultEnum.OK)
		{
			// Filling ValidatorTask structure
			ValidatorTask task = new ValidatorTask();
			task.contestId = contestId;
			task.groupNumber = test.groupDecription.getGroupNumber();
			task.testNumber = test.testNumber;
			task.problemId = problemId;
			String filename = test.getAnswerFilename();			
			if ((filename == null) || ("".equals(filename)))
			{
				filename = "output.txt";
			}
			RemoteFile rf = new RemoteFile();
			rf.fIsPresent = false;
			rf.filename = FileWorks.ConcatPaths(exRes.tempDir, filename);
			task.programOutput.filename = RemoteFS.saveToRemoteStorage(rf);
			task.programOutput.fIsPresent = false;
			task.testInput.filename = FileWorks.ConcatPaths(testsDir, test.getInputMask());
			task.testInput.fIsPresent = false;
			task.testOutput.filename = FileWorks.ConcatPaths(testsDir, test.getOutputMask());
			task.testOutput.fIsPresent = false;
			// validation
			ValidationResult vres = LocalValidator.validate(task);
			res.setValidationInfo(vres);
		}
		else
		{
			res.setValidationInfo(new ValidationResult(""));
		}
		//FileWorks.deleteDirectory(exRes.tempDir);
		return res;
	}
	
	public static void checkProblem(String contestId, String problemId)
	{
		try
		{
			ProblemDescription desc = new ProblemDescription(contestId, problemId);
			JudgeDirectory jd = new JudgeDirectory(desc);
			DirectoryResult res = jd.judge(desc.problemRoot + "solutions");
			String s = HtmlWorks.directoryResultToHtml(res);
			FileWorks.saveToFile(s, desc.problemRoot + "rep.html");			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static SubmissionResult judgeSourceFile(String file, String lang, ProblemDescription problem)
	{
		log.info("Judging file " + file);
		RemoteFS.startSession();
		SubmissionResult res = new SubmissionResult(problem);
		res.comment = file;
		CompilerTask ctask = new CompilerTask(file, lang);
		CompilerResult ci = Compiler.compile(ctask);
		res.setCompilationInfo(ci);
		if (!ci.isSuccessfull())
		{
			res.result = TestResultEnum.CE;
		}
		else
		{
			ExecutorProgram pr = new ExecutorProgram();
			pr.command = ci.program.getRunCommand();
			pr.files = ci.program.files;
			ProblemResult pres = judgeProblem(problem, new ParamsOverride(), pr);
			res.setProblemResult(pres);
		}
		RemoteFS.clearSession();
		log.info("Judgement: " + res.result);
		return res;
	}

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
		
		String filesrc = JudgeDirs.getTempDir() + "Main" + ".xml";
		String filesrc2 = JudgeDirs.getTempDir() + task.tid + ".xml";
		
		FileWorks.saveToFile(task.tsourcecode, filesrc);
		FileWorks.saveToFile(task.tsourcecode, filesrc2);
		
		try
		{
			res.res = judgeSourceFile(filesrc, task.tlanguage, pd);
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
		return res;
	}
	
	public static void main(String[] args)
	{
		checkProblem("NEERC-1998", "G");
	}
}
