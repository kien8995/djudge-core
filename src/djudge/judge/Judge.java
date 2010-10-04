/* $Id$ */

package djudge.judge;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import utils.DirectoryResult;
import utils.FileWorks;
import utils.HtmlWorks;
import utils.JudgeDirectory;

import djudge.common.JudgeDirs;
import djudge.filesystem.RemoteFS;
import djudge.judge.checker.CheckerResult;
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
import djudge.judge.dchecker.LocalChecker;
import djudge.judge.dchecker.RemoteFile;
import djudge.judge.dchecker.ValidatorTask;

public class Judge
{
	private static final Logger log = Logger.getLogger(Judge.class);
	
	public static ProblemResult judgeProblem(ProblemDescription desc, CheckParams params, ExecutorProgram program)
	{
		ProblemResult res = new ProblemResult(desc);

		if (params.fFirstTestOnly)
		{
			log.info("Testing on first test only");
			res.groupCount = 1;
			res.groupResults = new GroupResult[1];
			res.groupResults[0] = judgeGroup(desc.groups[0], params, program);
		}
		else
		{
			log.info("Testing on all tests");
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
		String testsDir = JudgeDirs.getProblemsDir() + contestId + "/" + problemId + "/" + "tests/";
		
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
			rf.filename = FileWorks.concatPaths(exRes.tempDir, filename);
			task.programOutput.filename = RemoteFS.saveToRemoteStorage(rf);
			task.programOutput.fIsPresent = false;
			task.testInput.filename = FileWorks.concatPaths(testsDir, test.getInputMask());
			task.testInput.fIsPresent = false;
			task.testOutput.filename = FileWorks.concatPaths(testsDir, test.getOutputMask());
			task.testOutput.fIsPresent = false;
			// validation
			CheckerResult vres = LocalChecker.validate(task);
			res.setValidationInfo(vres);
		}
		else
		{
			res.setValidationInfo(new CheckerResult(""));
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
			String s = HtmlWorks.directoryResultToHtml(res, desc);
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
		res.setJudgingStarted(new Date());
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
		res.setJudgingFinished(new Date());
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
	
	public static SubmissionResult generateAnswerFiles(String file, ProblemDescription problem)
	{
		RemoteFS.startSession();
		SubmissionResult res = new SubmissionResult(problem);
		res.setJudgingStarted(new Date());
		res.comment = file;
		CompilerTask ctask = new CompilerTask(file, "%AUTO%");
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
			ProblemResult pres = generateAnswerFiles(problem, pr);
			res.setProblemResult(pres);
		}
		RemoteFS.clearSession();
		res.setJudgingFinished(new Date());
		log.info("Judgement: " + res.result);
		return res;
	}
	
	public static ProblemResult generateAnswerFiles(ProblemDescription desc, ExecutorProgram program)
	{
		ProblemResult res = new ProblemResult(desc);
		
		System.out.println("All tests");
   		for (int i = 0; i < desc.groupsCount; i++)
   		{
   			res.groupResults[i] = generateAnswerFiles(desc.groups[i], program);
   		}
		res.updateResult();
		return res;
	}	

	public static GroupResult generateAnswerFiles(GroupDescription desc, ExecutorProgram program)
	{
		GroupResult res = new GroupResult(desc);
		
		for (int i = 0; i < desc.getTestCount(); i++)
		{
			res.testResults[i] = generateAnswerFiles(desc.tests.get(i), program);
		}
		res.updateResult();
		
		return res;		
	}
	
	public static TestResult generateAnswerFiles(TestDescription test, ExecutorProgram program)
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
		exTask.returnDirectoryContent = true;
		LocalExecutor ex = new LocalExecutor();
		
		ExecutionResult exRes = ex.execute(exTask);
		res.setRuntimeInfo(exRes);
		if (exRes.getResult() == ExecutionResultEnum.OK)
		{
			String filename = test.getAnswerFilename();			
			if (filename == null || "".equals(filename) || "stdout".equals(filename))
			{
				filename = "output.txt";
			}
			FileWorks.copyFile(FileWorks.concatPaths(testsDir, test.getOutputMask()), FileWorks.concatPaths(exRes.tempDir, filename));
		}
		return res;
	}	
}
