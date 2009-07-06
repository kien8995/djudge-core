package com.alt.djudge.judge.dexecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alt.djudge.judge.dcompiler.DFilePack;
import com.alt.utils.FileWorks;

public class LocalExecutor
{
	public static final String stdErrorFileName = "stderr.txt";
	public static final String stdOutputFileName = "stdout.txt";
	
	private void executeWindowsNT(ExecutorTask task, String workDir, ExecutionResult res)
	{
		// preparing crutch
		FileWorks.CopyFile(workDir + "invoke.dll", "./tools/invoke.dll");
		FileWorks.CopyFile(workDir + "run.exe", "./tools/run.exe");
		FileWorks.CopyFile(workDir + "crutch.exe", "./tools/crutch.exe");
		
		ExecutorFiles files = task.files;
		ExecutorLimits limits = task.limits;
		ExecutorProgram pr = task.program;
		
		// Building command line
		StringBuffer cmd = new StringBuffer();
		
		cmd.append(" -Xifce ");
		
		//if (limits.fCreateSubprocess)
		//FIXME - Security bug: allows all runned programs create child processes 
		cmd.append(" -Xacp ");
		
	//	if (files.rootDirectory != null && files.rootDirectory != "")
		//	cmd.append(" -d \"" + files.rootDirectory.replace('/', '\\') + "\" ");
		
		if (limits.timeLimit > 0)
		{
			cmd.append(" -t " + limits.timeLimit + "ms ");
			// Idleness
			cmd.append(" -y " + (2 * limits.timeLimit) + "ms ");
		}
		
		if (limits.memoryLimit > 0)
			cmd.append(" -m " + limits.memoryLimit +  " ");
		
		if (files.inputFilename != null && files.inputFilename != "")
			cmd.append(" -i \"" + files.inputFilename + "\" ");
			

		if (files.outputFilename != null && files.outputFilename != "")
			cmd.append(" -o \"" + files.outputFilename + "\" ");
		else
			cmd.append(" -o \"" + stdOutputFileName + "\" ");

		if (files.errorFilename != null && files.errorFilename != "")
			cmd.append(" -e \"" + files.errorFilename + "\" ");
		else
			cmd.append(" -e \"" + stdErrorFileName + "\" ");

		// FIXME
		cmd = new StringBuffer("run.exe " + cmd + " \"" + pr.getCommand(workDir) + "\"");
		cmd = new StringBuffer(workDir + "crutch.exe " + workDir + "  " + cmd);
		
		//System.out.println(cmd);
		try
		{
			Process process = Runtime.getRuntime().exec(cmd.toString());
			try
			{
				process.waitFor();
			}
			catch (Exception exc)
			{
				System.out.println("!!! Exception catched (while waiting for external runner): " + exc);
			}

			int retValue = process.exitValue();
						
			long mem = 0, time = 0, cnt = 0, output = 0;
			
			if (files.outputFilename != null)
			{
				output = new File(files.outputFilename).length();
			}
			
			// Return values of external runner
			switch (retValue)
			{
				case  0: res.result = ExecutionResultEnum.OK; break;
				case -1: res.result = ExecutionResultEnum.TimeLimitExceeeded; break;
				case -2: res.result = ExecutionResultEnum.MemoryLimitExceeded; break;
				case -3: res.result = ExecutionResultEnum.TimeLimitExceeeded; break;
				case -4: res.result = ExecutionResultEnum.RuntimeErrorCrash; break;
				default: res.result = ExecutionResultEnum.Other;
			}
			
			// Parsing runner's output
			try
			{
				BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				String runnerOut = "";
				
				while ((line = out.readLine()) != null)
				{
					runnerOut += line + "\n";
					cnt++;
					// exit code
					if (cnt == 3 && res.result == ExecutionResultEnum.OK)
					{
						try
						{
    						String token = line.substring(line.lastIndexOf(" ") + 1, line.length());
    						retValue = Integer.parseInt(token);
    						if (retValue != 0)
    						{
    							res.result = ExecutionResultEnum.NonZeroExitCode;
    						}
						}
						catch (Exception ex){ };
					}
					// time
					if (cnt == 4 && res.result == ExecutionResultEnum.OK)
					{
						try
						{
    						String token = line.substring(line.indexOf(':') + 1, line.lastIndexOf("of") - 1);
    						Double val = Double.parseDouble(token);
    						time = (long)Math.round(val * 1000.0);
						}
						catch (Exception ex){ }
					}
					//memory
					else if (cnt == 6 && res.result == ExecutionResultEnum.OK)
					{
						try
						{
							String token = line.substring(line.lastIndexOf("  ") + 2, line.lastIndexOf("of") - 1);
							mem = Integer.parseInt(token);			
						}
						catch (Exception ex){ }
					}
					res.runnerOutput = runnerOut;
				}
				out.close();
			}
			catch (Exception exc)
			{
				System.out.println("!!! IOException catched (while parsing runner's stdout): " + exc);
			}
			
			// Cleaning crutch
			FileWorks.deleteFile(workDir + "invoke.dll");
			FileWorks.deleteFile(workDir + "run.exe");
			FileWorks.deleteFile(workDir + "crutch.exe");			
			
			res.exitCode = retValue;
			res.memoryConsumed = mem;
			res.outputGenerated = output;
			res.timeConsumed = time;
			
			if (files.outputFilename != null && files.outputFilename != "")
			{
				res.outputGenerated = new File(workDir + files.outputFilename).length();
				res.stdOutputContent = FileWorks.readFile(workDir + files.outputFilename);
			}
			else
			{
				res.outputGenerated = new File(workDir + stdOutputFileName).length();
				res.stdOutputContent = FileWorks.readFile(workDir + stdOutputFileName);
			}

			if (files.errorFilename != null && files.errorFilename != "")
				res.stdErrorContent = FileWorks.readFile(workDir + files.errorFilename);
			else
				res.stdErrorContent = FileWorks.readFile(workDir + stdErrorFileName);
			
			if (task.returnDirectoryContent)
			{
				res.files = new DFilePack();
				res.files.readDirectory(workDir);
			}
			
		}
		catch (IOException exc)
		{
			System.out.println("!!! IOException catched: " + exc);
		}
		FileWorks.saveToFile(cmd.toString() + "\n\n" + res.runnerOutput, workDir + "runner.data");
	}
	
	/*
	 * Виконує задачу task та повертає результат виконання
	 */
	public ExecutionResult execute(ExecutorTask task)
	{
		ExecutionResult res = new ExecutionResult();
		
		/* Generating name for working directory */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date()) + "_ex";
        String workDir = "./work/" + id + "/";
        
        /* Unpacking files to execute (if any) to the working directory */
        task.program.files.unpack(workDir);
        
        executeWindowsNT(task, workDir, res);
        
		return res;
	}
	
	/*
	public static void main(String[] args)
	{
		LocalExecutor ex = new LocalExecutor();
		
		ExecutorFiles files = new ExecutorFiles("input.txt", "output.txt");
		ExecutorLimits limits = new ExecutorLimits(100, 2000000);
		ExecutorProgram program = new ExecutorProgram();
		program.files = new DFilePack("d:\\a.exe");
		program.files.addFile("d:\\input.txt");
		program.command = "a.exe";
		
		ExecutorTask task = new ExecutorTask();
		task.files = files;
		task.limits = limits;
		task.program = program;
		
		ExecutionResult res = ex.execute(task);
		//res.files.unpack("d:\\1\\");
		//System.out.println(res.runnerOutput);
	}*/
}
