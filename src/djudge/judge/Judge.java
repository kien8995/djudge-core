/* $Id$ */

package djudge.judge;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.io.File;

import org.apache.log4j.Logger;
import utils.FileTools;

import djudge.common.JudgeDirs;
import djudge.judge.checker.CheckerResult;
import djudge.judge.dcompiler.Compiler;
import djudge.judge.dcompiler.CompilerResult;
import djudge.judge.dcompiler.CompilerTask;
import djudge.judge.dchecker.LocalChecker;
import djudge.judge.dchecker.RemoteFile;
import djudge.judge.dchecker.ValidatorTask;
import djudge.judge.executor.ExecutionResult;
import djudge.judge.executor.ExecutionResultEnum;
import djudge.judge.executor.ExecutorFiles;
import djudge.judge.executor.ExecutorLimits;
import djudge.judge.executor.ExecutorProgram;
import djudge.judge.executor.ExecutorTask;
import djudge.judge.executor.LocalExecutor;
import djudge.remotefs.RemoteFS;


public class Judge
{
	private static final Logger log = Logger.getLogger(Judge.class);
	
	public static ProblemResult judgeProblem(ProblemDescription desc, CheckParams params, ExecutorProgram program)
	{
		return judgeProblem(desc, params, program, null);
	}
	
	public static ProblemResult judgeProblem(ProblemDescription desc, CheckParams params, ExecutorProgram program, String rootDir)
	{
		ProblemResult res = new ProblemResult(desc);

		if (params.fFirstTestOnly)
		{
			log.info("Testing on first test only");
			res.groupCount = 1;
			res.groupResults = new GroupResult[1];
			res.groupResults[0] = judgeGroup(desc.groups[0], params, program, rootDir);
		}
		else
		{
			log.info("Testing on all tests");
    		for (int i = 0; i < desc.groupsCount; i++)
    		{
    			res.groupResults[i] = judgeGroup(desc.groups[i], params, program, rootDir);
    		}
		}
		res.updateResult();
		return res;
	}
	
	public static GroupResult judgeGroup(GroupDescription desc, CheckParams params, ExecutorProgram program)
	{
		return judgeGroup(desc, params, program, null);
	}
	
	public static GroupResult judgeGroup(GroupDescription desc, CheckParams params, ExecutorProgram program, String rootDir)
	{
		GroupResult res = new GroupResult(desc);
		
		for (int i = 0; i < desc.getTestCount(); i++)
		{
			String groupDir = rootDir != null ? rootDir + "" + (i + 1) + "/" : null;
			
			for (int rep = 0; rep < 3; rep++)
			{
				TestResult tr = res.testResults[i] = judgeTest(desc.tests.get(i), params, program, groupDir);
				
				// FIXME START: temp patch for issue #16
				if (tr.getResult() != TestResultEnum.WA)
					break;
				// if judgment is `WA' and no output was generated then repeat run&check
				if (tr.getRuntimeInfo().outputGenerated == 0L)
					continue;
				break;
				// FIXME END
			}
		}
		res.updateResult();
		
		return res;		
	}
	
	public static TestResult judgeTest(TestDescription test, CheckParams params, ExecutorProgram program)
	{
		return judgeTest(test, params, program, null);
	}
	
	public static TestResult judgeTest(TestDescription test, CheckParams params, ExecutorProgram program, String groupDir)
	{
		TestResult res = null;
		String testDir = groupDir != null ? groupDir + "" + (test.testNumber + 1) + "/" : null;
		int repCount = test.getInteractionDescription().getInteractionType() == InteractionType.NEERC_INTERACTOR ? 10 : 1;
		for (int i = 1; i <= repCount; i++)
		{
    		res = new TestResult(test);
    		String contestId = test.problemInfo.contestID;
    		String problemId = test.problemInfo.problemID;
    		String testsDir = JudgeDirs.getProblemsDir() + contestId + "/" + problemId + "/" + "tests/";
    		
    		File f = new File(testsDir + test.getInputMask());
    		if (!f.exists() || !f.isFile())
    		{
    			String msg = "No test's input file: " + test.getInputMask();
    			res.result = TestResultEnum.ProblemError;
    			res.resultDetails = "No test's input file: " + test.getInputMask();
    			log.error(msg);
    			return res;
    		}
    		
    		String inputTestFilename = test.getInputFilename();
    		if (test.isEmptyInputFilename())
    		{
    			inputTestFilename = "input.txt";
    		}
    		program.files.addFile(testsDir + test.getInputMask(), inputTestFilename);
    		
    		ExecutorFiles files = test.getExecutorFiles();
    		ExecutorLimits limits = test.getActualLimits();
    		
    		ExecutorTask exTask = new ExecutorTask(program, limits, files);
    		exTask.returnDirectoryContent = false;
    		LocalExecutor ex = new LocalExecutor();
    		
    		ExecutionResult exRes = ex.execute(exTask, testDir != null ? testDir + "ex/" : null);
    		res.setRuntimeInfo(exRes);
    		if (exRes.getResult() == ExecutionResultEnum.OK)
    		{
    			if (exRes.outputGenerated == 0 && i != repCount)
    				continue;
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
    			else
    			{
    				res.getRuntimeInfo().outputGenerated = new File(FileTools.concatPaths(exRes.tempDir, filename)).length();
    			}
    			
    			RemoteFile rf = new RemoteFile();
    			rf.fIsPresent = false;
    			rf.filename = FileTools.concatPaths(exRes.tempDir, filename);
    			
    			task.programOutput.filename = RemoteFS.saveToRemoteStorage(rf);
    			task.programOutput.fIsPresent = false;
    			
    			task.testInput.filename = FileTools.concatPaths(testsDir, test.getInputMask());
    			task.testInput.fIsPresent = false;
    			
    			task.testOutput.filename = FileTools.concatPaths(testsDir, test.getOutputMask());
    			task.testOutput.fIsPresent = false;
    			// output checking
    			CheckerResult vres = LocalChecker.check(task, testDir != null ? testDir + "val/" : null);
    			res.setCheckInfo(vres);
    		}
    		else
    		{
    			res.setCheckInfo(new CheckerResult(""));
    		}
		}
		return res;
	}
		
	public static SubmissionResult judgeSourceFile(String file, String lang, ProblemDescription problem, CheckParams params)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
		String id = dateFormat.format(new Date());
		String rootDir = JudgeDirs.getWorkDir() + id + "/";
		
		log.info("Judging file " + file + " started");
		RemoteFS.startSession();
		SubmissionResult res = new SubmissionResult(problem);
		res.setJudgingStarted(new Date());
		res.comment = file;
		CompilerTask ctask = new CompilerTask(file, lang);
		CompilerResult ci = Compiler.compile(ctask, rootDir + "compilation/");
		res.setCompilationInfo(ci);
		if (!ci.isSuccessfull())
		{
			res.result = TestResultEnum.CE;
		}
		else
		{
			ExecutorProgram pr = new ExecutorProgram();
			InteractionDescription interact = problem.getInteractionDescription();
			if (interact.getInteractionType() == InteractionType.NEERC_INTERACTOR)
			{
				pr.command = ci.program.getRunCommand();
				pr.files = ci.program.files;
				pr.files.addFile(JudgeDirs.getProblemDir(problem.getContestID(), problem.getProblemID()) + interact.getInteractorExe());
				//pr.files.addFile(JudgeDirs.getToolsDir() + "interaction/run2.jar");
				/*System.out.println("");
				System.out.println("Before Interactor command");
				System.out.println(pr.command);*/
				
				pr.command = "java -jar " +
						JudgeDirs.getToolsDir() + "interaction/run2.jar " + 
						"./" + interact.getInteractorExe() + " " + 
						pr.command;
				
				/*System.out.println("After: ");
				System.out.println(pr.command);*/
			}
			else
			{
				pr.command = ci.program.getRunCommand();
				pr.files = ci.program.files;
			}
			ProblemResult pres = judgeProblem(problem, params, pr, rootDir);
			res.setProblemResult(pres);
		}
		RemoteFS.clearSession();
		res.setJudgingFinished(new Date());
		log.info("Judgement of " + file + ": " + res.result);
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
		
		FileTools.saveToFile(task.tsourcecode, filesrc);
		FileTools.saveToFile(task.tsourcecode, filesrc2);
		
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
			FileTools.copyFile(FileTools.concatPaths(testsDir, test.getOutputMask()), FileTools.concatPaths(exRes.tempDir, filename));
		}
		return res;
	}
	
	public static void main(String[] args)
	{
		//ProblemsetTester.main(new String[] {"@SystemTest", "ExternalChecker"});
		ProblemsetTester.main(new String[] {"NCPC-2009"});
	}
}
