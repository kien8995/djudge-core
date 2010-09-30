/* $Id$ */

package djudge.judge.dexecutor;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import utils.FileWorks;

import djudge.common.Deployment;
import djudge.common.JudgeDirs;
import djudge.judge.dcompiler.DistributedFileset;

public class LocalExecutor
{
	public static final String stdErrorFileName = "stderr.txt";
	public static final String stdOutputFileName = "stdout.txt";
	
	private void executeWindowsNT(ExecutorTask task, String workDir, ExecutionResult res)
	{
		// preparing `crutch`
		FileWorks.copyFile(workDir + "invoke.dll",  JudgeDirs.getToolsDir() + "winnt/invoke.dll");
		FileWorks.copyFile(workDir + "run.exe", JudgeDirs.getToolsDir() + "winnt/run.exe");
		FileWorks.copyFile(workDir + "crutch.exe", JudgeDirs.getToolsDir() + "winnt/crutch.exe");
		
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
				case -4: res.result = ExecutionResultEnum.RuntimeErrorGeneral; break;
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
			
			// Cleaning crutchtools
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
			}
			else
			{
				res.outputGenerated = new File(workDir + stdOutputFileName).length();
			}

			if (task.returnDirectoryContent)
			{
				res.files = new DistributedFileset();
				res.files.readDirectory(workDir);
			}
			
		}
		catch (IOException exc)
		{
			System.out.println("!!! IOException catched: " + exc);
		}
		FileWorks.saveToFile(cmd.toString() + "\n\n" + res.runnerOutput, workDir + "runner.data");
	}
	
	private String quote(String param)
	{
		return param.contains(" ") ? "\"" + param + "\"" : param;
	}

	private void executeLinux(ExecutorTask task, String workDir, ExecutionResult res)
	{
		ExecutorFiles files = task.files;
		ExecutorLimits limits = task.limits;
		ExecutorProgram pr = task.program;
		
		/* Building command line */
		// single-line params
		StringBuffer cmd = new StringBuffer();
		// vector<string> of params
		Vector<String> params = new Vector<String>();
		
		// setting time limit
		if (limits.timeLimit > 0)
		{
			cmd.append(" -c " + limits.timeLimit + "ms ");
			// Idleness
			cmd.append(" -r " + (2 * limits.timeLimit) + "ms ");
			
			params.add("-c");
			params.add("" + limits.timeLimit + "ms");
			params.add("-r");
			params.add("" + (2 * limits.timeLimit) + "ms");
		}
		
		cmd.append(" -d " + quote(FileWorks.getAbsolutePath(workDir)));
		params.add("-d");
		params.add(FileWorks.getAbsolutePath(workDir));
		
		// verbose param (for debug)
		// cmd.append(" -v ");
		// params.add("-v");
		
		// setting memory limit
		if (limits.memoryLimit > 0)
		{
			cmd.append(" -m " + limits.memoryLimit +  " ");
			params.add("-m");
			params.add("" + limits.memoryLimit);
			
			// for java
			params.add("-p");
		}
		
		/* Redirecting I/O stream */
		if (files.inputFilename != null && files.inputFilename != "")
		{
			cmd.append(" -I " + quote(files.inputFilename));
			params.add("-I");
			params.add(files.inputFilename);
		}
		
		if (files.outputFilename != null && files.outputFilename != "")
		{
			cmd.append(" -O " + quote(files.outputFilename));
			params.add("-O");
			params.add(files.outputFilename);
		}
		else
		{
			cmd.append(" -O " + quote(stdOutputFileName));
			params.add("-O");
			params.add(stdOutputFileName);
		}

		if (files.errorFilename != null && files.errorFilename != "")
		{
			cmd.append(" -E " + quote(files.errorFilename));
			params.add("-E");
			params.add(files.errorFilename);
		}
		else
		{
			cmd.append(" -E " + quote(stdErrorFileName));
			params.add("-E");
			params.add(stdErrorFileName);
		}

		// FIXME
		cmd = new StringBuffer(workDir + "runner " + cmd + " " + quote(pr.getCommand(workDir)));
		params.add(pr.getCommand(workDir));
		
		// adding runner as first param
		params.add(0, JudgeDirs.getToolsDir() + "linux/runner");
		
		try
		{
			Process process = Runtime.getRuntime().exec(params.toArray(new String[0]));
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
			
			System.out.println("Exit Code: " + retValue);
			
			// Return values of external runner
			switch (retValue)
			{
				case 0: res.result = ExecutionResultEnum.OK; break;
				case 1: res.result = ExecutionResultEnum.TimeLimitExceeeded; break;
				case 2: res.result = ExecutionResultEnum.MemoryLimitExceeded; break;
				case 3: res.result = ExecutionResultEnum.TimeLimitExceeeded; break;
				case 4: res.result = ExecutionResultEnum.RuntimeErrorGeneral; break;
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
					if (cnt == 6)
					{
						try
						{
    						String token = line.substring(line.lastIndexOf("\t") + 1);
    						retValue = Integer.parseInt(token);
    						if (retValue != 0)
    						{
    							res.result = ExecutionResultEnum.NonZeroExitCode;
    						}
						}
						catch (Exception ex){ };
					}
					// time
					if (cnt == 2)
					{
						try
						{
    						String token = line.substring(line.lastIndexOf("\t") + 1);
    						long val = Long.parseLong(token);
    						time = val;
    						if (limits.timeLimit > 0 && limits.timeLimit < time)
    						{
    							res.result = ExecutionResultEnum.TimeLimitExceeeded;
    						}
						}
						catch (Exception ex){ ex.printStackTrace(); }
					}
					//memory
					else if (cnt == 4)
					{
						try
						{
    						String token = line.substring(line.lastIndexOf("\t") + 1);
    						long val = Long.parseLong(token);
							mem = val;
    						if (limits.memoryLimit > 0 && limits.memoryLimit < mem)
    						{
    							res.result = ExecutionResultEnum.MemoryLimitExceeded;
    						}
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
			
			res.exitCode = retValue;
			res.memoryConsumed = mem;
			res.outputGenerated = output;
			res.timeConsumed = time;
			
			if (files.outputFilename != null && files.outputFilename != "")
			{
				res.outputGenerated = new File(workDir + files.outputFilename).length();
			}
			else
			{
				res.outputGenerated = new File(workDir + stdOutputFileName).length();
			}

			if (task.returnDirectoryContent)
			{
				res.files = new DistributedFileset();
				res.files.readDirectory(workDir);
			}
		}
		catch (IOException exc)
		{
			System.out.println("!!! IOException catched: " + exc);
		}
		FileWorks.saveToFile("Executed command:\n" + cmd.toString() + "\n\n" + res.runnerOutput, workDir + "runner.data");
		System.out.println("Result: " + res.getResult());
	}	
	
	public ExecutionResult execute(ExecutorTask task)
	{
		ExecutionResult res = new ExecutionResult();
		
		/* Generating name for working directory */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date()) + "_ex";
        String workDir = JudgeDirs.getWorkDir() + id + "/";
        
        res.tempDir = workDir;
        
        /* Unpacking files to execute (if any) to the working directory */
        task.program.files.unpack(workDir);
        
        if (Deployment.isOSWinNT())
        {
        	executeWindowsNT(task, workDir, res);
        }
        else if (Deployment.isOSLinux())
        {
        	executeLinux(task, workDir, res);
        }
        else
        {
        	// TODO: do someting
        }
        
        // TODO: probable enable this in release version
        //FileWorks.deleteDirectory(workDir);
        
		return res;
	}
}
