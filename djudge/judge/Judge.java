package djudge.judge;


import java.io.File;

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
	}*/
	
	public static ProblemResult judgeProblem(ProblemDescription desc, CheckParams params, ExecutorProgram program)
	{
		ProblemResult res = new ProblemResult(desc);

		if (params.fFirstTestOnly)
		{
			System.out.println("First only");
			res.groupCount = 1;
			res.groupResults = new GroupResult[1];
			res.groupResults[0] = judgeGroup(desc.groups[0], params, program);
		}
		else
		{
			System.out.println("All tests");
    		for (int i = 0; i < desc.groupsCount; i++)
    		{
    			res.groupResults[i] = judgeGroup(desc.groups[i], params, program);
    		}
		}
		res.updateResult();
		
		return res;
	}
	
	public static GroupResult judgeGroup(GroupDescription desc, CheckParams params, ExecutorProgram program)
	{
		GroupResult res = new GroupResult(desc);
		
		for (int i = 0; i < desc.getTestCount(); i++)
		{
			res.testResults[i] = judgeTest(desc.tests.get(i), params,  program);
		}
		res.updateResult();
		
		return res;		
	}
	
	public static TestResult judgeTest(TestDescription test, CheckParams params, ExecutorProgram program)
	{
		TestResult res = new TestResult(test);
		String contestId = test.problemInfo.contestID;
		String problemId = test.problemInfo.problemID;
		String testsDir = "./problems/" + contestId + "/" + problemId + "/" + "tests/";
		
		File f = new File(testsDir + test.getInputMask());
		if (!f.exists())
		{
			res.result = TestResultEnum.IE;
			return res;
		}
		
		String inputTestFilename = test.getInputFilename();
		if (inputTestFilename == null || "".equals(inputTestFilename) || "stdin".equals(inputTestFilename))
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
			if (filename == null || "".equals(filename) || "stdout".equals(filename))
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
		return res;
	}
	
	public static void checkProblem(String contestId, String problemId, CheckParams params)
	{
		try
		{
			ProblemDescription desc = new ProblemDescription(contestId, problemId);
			desc.overrideParams(params);
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
	
	public static SubmissionResult judgeSourceFile(String file, String lang, ProblemDescription problem, CheckParams params)
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
			ProblemResult pres = judgeProblem(problem, params, pr);
			res.setProblemResult(pres);
		}
		RemoteFS.clearSession();
		log.info("Judgement: " + res.result);
		return res;
	}

	public static JudgeTaskResult judgeTask(JudgeTaskDescription task, CheckParams params)
	{
		JudgeTaskResult res = new JudgeTaskResult();
		res.desc = task;
		ProblemDescription pd;
		
		try
		{
			pd = new ProblemDescription(task.tcontest, task.tproblem);
			pd.overrideParams(params);
		}
		catch (Exception e)
		{
			log.error("While creating problem description", e);
			return res;
		}
		
		String filesrc = JudgeDirs.getTempDir() + "Main" + ".xml";
		String filesrc2 = JudgeDirs.getTempDir() + task.tid + ".xml";
		
		FileWorks.saveToFile(task.tsourcecode, filesrc);
		FileWorks.saveToFile(task.tsourcecode, filesrc2);
		
		try
		{
			res.res = judgeSourceFile(filesrc, task.tlanguage, pd, params);
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
		return res;
	}
}
