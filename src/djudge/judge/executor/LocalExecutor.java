/* $Id$ */

package djudge.judge.executor;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import utils.FileTools;

import djudge.common.Deployment;
import djudge.common.JudgeDirs;
import djudge.judge.dcompiler.DistributedFileset;

public class LocalExecutor implements ExecutorLinuxExitCodes
{
	private final static Logger log = Logger.getLogger(LocalExecutor.class);
	
	public final static String standardErrorFileName = "stderr.txt";
	
	public final static String standardOutputFileName = "stdout.txt";

	private void executeWindowsNT(ExecutorTask task, String workDir, ExecutionResult res)
	{
		// preparing `crutch`
		FileTools.copyFile(workDir + "invoke.dll",  JudgeDirs.getToolsDir() + "winnt/invoke.dll");
		FileTools.copyFile(workDir + "run.exe", JudgeDirs.getToolsDir() + "winnt/run.exe");
		FileTools.copyFile(workDir + "crutch.exe", JudgeDirs.getToolsDir() + "winnt/crutch.exe");
		
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
			cmd.append(" -o \"" + standardOutputFileName + "\" ");

		if (files.errorFilename != null && files.errorFilename != "")
			cmd.append(" -e \"" + files.errorFilename + "\" ");
		else
			cmd.append(" -e \"" + standardErrorFileName + "\" ");

		// FIXME
		cmd = new StringBuffer("run.exe " + cmd + " \"" + pr.getCommand(workDir) + "\"");
		cmd = new StringBuffer(workDir + "crutch.exe " + workDir + "  " + cmd);
		
		log.debug("Executing " + cmd);
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
				// FIXME
				default: res.result = ExecutionResultEnum.RuntimeErrorGeneral;
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
			FileTools.deleteFile(workDir + "invoke.dll");
			FileTools.deleteFile(workDir + "run.exe");
			FileTools.deleteFile(workDir + "crutch.exe");			
			
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
				res.outputGenerated = new File(workDir + standardOutputFileName).length();
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
		FileTools.saveToFile(cmd.toString() + "\n\n" + res.runnerOutput, workDir + "runner.data");
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
		
		//if (!task.program.command.startsWith("/"))
		{
			cmd.append(" -d " + quote(FileTools.getAbsolutePath(workDir)));
			params.add("-d");
			params.add(FileTools.getAbsolutePath(workDir));
		}
		
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
			cmd.append(" -O " + quote(standardOutputFileName));
			params.add("-O");
			params.add(standardOutputFileName);
		}

		if (files.errorFilename != null && files.errorFilename != "")
		{
			cmd.append(" -E " + quote(files.errorFilename));
			params.add("-E");
			params.add(files.errorFilename);
		}
		else
		{
			cmd.append(" -E " + quote(standardErrorFileName));
			params.add("-E");
			params.add(standardErrorFileName);
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
				log.fatal("Unknown exception catched (while waiting for external runner)", exc);
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
				case EXIT_OK: res.result = ExecutionResultEnum.OK; break;
				case EXIT_TLE: res.result = ExecutionResultEnum.TimeLimitExceeeded; break;
				case EXIT_MLE: res.result = ExecutionResultEnum.MemoryLimitExceeded; break;
				case EXIT_RE: res.result = ExecutionResultEnum.RuntimeErrorGeneral; break;
				case EXIT_OLE: res.result = ExecutionResultEnum.OutputLimitExceeded; break;
				case EXIT_IE: res.result = ExecutionResultEnum.InternalError; break;
				case EXIT_SIGNAL_SIGKILL: res.result = ExecutionResultEnum.TimeLimitExceeeded; break;
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
			catch (IOException ex)
			{
				log.error("IOException catched (while parsing runner's stdout)", ex);
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
				res.outputGenerated = new File(workDir + standardOutputFileName).length();
			}

			if (task.returnDirectoryContent)
			{
				res.files = new DistributedFileset();
				res.files.readDirectory(workDir);
			}
		}
		catch (Exception ex)
		{
			log.error("IOException catched (while parsing runner's stdout)", ex);
		}
		FileTools.saveToFile("Executed command:\n" + cmd.toString() + "\n\n" + res.runnerOutput, workDir + "/runner.data");
		log.info("Command: " + cmd.toString());
		log.info("Result: " + res.getResult() + " exit code: " + res.getExitCode());
	}	

	public ExecutionResult execute(ExecutorTask task, String workDir)
	{
		ExecutionResult res = new ExecutionResult();
		
        /* If no working directory is set */
        if (null == workDir)
        {
    		/* Generating name for working directory */
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
            String id = dateFormat.format(new Date()) + "_ex";
            
        	workDir = JudgeDirs.getWorkDir() + id + "/";
        }
        else
        {
        	// linux porting issue (under code review - issue #13)
        	if (Deployment.isOSWinNT())
        	{
        		if (!workDir.endsWith("\\"))
        			workDir = workDir + "\\";
        	}
        }
        
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
        	log.fatal("Your OS is not supported");
        }
        
        // TODO: probable enable this in release version
        //FileWorks.deleteDirectory(workDir);
		return res;
	}
	
	
	public ExecutionResult execute(ExecutorTask task)
	{
		return execute(task, null);
	}
}
