/* $Id$ */

// TODO: review this class (old version used)
package djudge.judge.executor;

import java.io.*;
import java.util.Vector;

import org.apache.log4j.Logger;

import utils.FileTools;

import djudge.common.Deployment;
import djudge.common.JudgeDirs;
import djudge.common.Review;
import djudge.judge.common_data_structures.ExecutorFiles;
import djudge.judge.common_data_structures.ExecutorLimits;
import djudge.judge.common_data_structures.ExecutorSecurityLimits;

// TODO: merge this class with LocalExecutor
public class Runner implements ExecutorLinuxExitCodes
{
	private static final Logger log = Logger.getLogger(Runner.class);
	
	private final String homeDirectory;
	
	private final ExecutorLimits limits;
	
	private final ExecutorFiles files;
	
	private String saveOutputTo;
	
	private boolean fRedirect = false;
	
	@SuppressWarnings("unused")
	private ExecutorSecurityLimits security = new ExecutorSecurityLimits();
	
	public Runner(String homeDir, ExecutorLimits limits, ExecutorFiles files)
	{
		this.homeDirectory = homeDir;
		this.limits = limits;
		this.files = files;
	}
	
	public Runner(ExecutorLimits limits, ExecutorFiles files)
	{
		this.limits = limits;
		this.files = files;
		this.homeDirectory = null;
	}
	
	public Runner(ExecutorLimits limits)
	{
		this.limits = limits;
		this.files = new ExecutorFiles();
		this.homeDirectory = null;
	}	
	
	public Runner()
	{
		this.files = new ExecutorFiles();
		this.limits = new ExecutorLimits();
		this.homeDirectory = null;
	}
	
	@Review private ExecutionResult runWinNT(String command)
	{
	/*	StringBuffer cmd = new StringBuffer();
		
		cmd.append(" -Xifce ");
		
		//if (limits.fCreateSubprocess)
		//FIXME - Security bug: allows all runned programs create child processes 
		cmd.append(" -Xacp ");
		
		if (homeDirectory != null)
			cmd.append(" -d \"" + homeDirectory + "\" ");
		
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

		if (files.errorFilename != null && files.errorFilename != "")
			cmd.append(" -e \"" + files.errorFilename + "\" ");

		// FIXME
		cmd = new StringBuffer("./tools/run.exe " + cmd + " \"" + command + "\"");
		
		//System.out.println(cmd);

		RunnerResult res = new RunnerResult();
		
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
						
			int mem = 0, time = 0, cnt = 0, output = 0;
			
			if (files.outputFilename != null)
				output = (int)new File(files.outputFilename).length();
			
			// Return values of external runner
			switch (retValue)
			{
				case 0: res.state = RunnerResultEnum.OK; break;
				case -1: res.state = RunnerResultEnum.TimeLimitExceeeded; break;
				case -2: res.state = RunnerResultEnum.MemoryLimitExceeded; break;
				case -4: res.state = RunnerResultEnum.RuntimeErrorCrash; break;
				default: res.state = RunnerResultEnum.Other;
			}
			// Parsing runner's output
			try
			{
				BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedWriter file = null;
				if (fRedirect)
				{
					file = new BufferedWriter(new FileWriter(new File(saveOutputTo)));
					file.write(cmd.toString() + "\n");
				}
				String line;
				
				while ((line = out.readLine()) != null)
				{
					if (fRedirect)
						file.write(line + "\n");
					cnt++;
					// exit code
					if (cnt == 3 && res.state == RunnerResultEnum.OK)
					{
						try
						{
    						String token = line.substring(line.lastIndexOf(" ") + 1, line.length());
    						retValue = Integer.parseInt(token);
    						if (retValue != 0)
    							res.state = RunnerResultEnum.NonZeroExitCode;
						}
						catch (Exception ex){ };
					}
					// time
					if (cnt == 4 && res.state == RunnerResultEnum.OK)
					{
						try
						{
    						String token = line.substring(line.indexOf(':') + 1, line.lastIndexOf("of") - 1);
    						Double val = Double.parseDouble(token);
    						time = (int)Math.round(val * 1000.0);
						}
						catch (Exception ex){ }
					}
					//memory
					else if (cnt == 6 && res.state == RunnerResultEnum.OK)
					{
						try
						{
							String token = line.substring(line.lastIndexOf("  ") + 2, line.lastIndexOf("of") - 1);
							mem = Integer.parseInt(token);			
						}
						catch (Exception ex){ }
					}
					// Debug info
					if (true)
					{
						//System.out.println(line);
					}
				}
				out.close();
				if (file != null)
					file.close();
			}
			catch (Exception exc)
			{
				System.out.println("!!! IOException catched (while parsing runner's stdout): " + exc);
			}
			
			res.OK(retValue, time, mem, output);
			
		}
		catch (IOException exc)
		{
			System.out.println("!!! IOException catched: " + exc);
		}
		return res;
		*/
		return new ExecutionResult();
	}
	
	/*private ExecutionResult runLinux(String command)
	{
		Vector<String> params = new Vector<String>();		
		if (homeDirectory != null)
		{
			params.add("-d");
			params.add(FileTools.getAbsolutePath(homeDirectory));
		}
		
		if (limits.timeLimit > 0)
		{
			params.add("-c");
			params.add("" + limits.timeLimit + "ms");
			params.add("-r");
			params.add("" + (2 * limits.timeLimit) + "ms");
		}
		
		if (limits.memoryLimit > 0)
		{
			params.add("-m");
			params.add("" + limits.memoryLimit);
			
			// TODO: review
			// for java
			params.add("-p");
		}
		
		// Redirecting I/O stream
		if (files.inputFilename != null && files.inputFilename != "")
		{
			params.add("-I");
			params.add(files.inputFilename);
		}
		
		if (files.outputFilename != null && files.outputFilename != "")
		{
			params.add("-O");
			params.add(files.outputFilename);
		}

		if (files.errorFilename != null && files.errorFilename != "")
		{
			params.add("-E");
			params.add(files.errorFilename);
		}

		params.add(0, JudgeDirs.getToolsDir() + "linux/runner");
		params.add(command);
		
		String cmd = "";
		for (String s : params)
			cmd += " " + s; 
		
		RunnerResult res = new RunnerResult();
		
		try
		{
			Process process = Runtime.getRuntime().exec(params.toArray(new String[0]));
			
			try
			{
				process.waitFor();
			}
			catch (Exception exc)
			{
				log.error("Exception catched (while waiting for external runner)", exc);
			}

			int retValue = process.exitValue();
						
			int mem = 0, time = 0, cnt = 0, output = 0;
			
			if (files.outputFilename != null)
				output = (int) new File(files.outputFilename).length();
			
			log.debug("Validator's exit code: " + retValue);
			
			// Return values of external runner
			switch (retValue)
			{
				case EXIT_OK: res.state = RunnerResultEnum.OK; break;
				case EXIT_TLE: res.state = RunnerResultEnum.TimeLimitExceeeded; break;
				case EXIT_MLE: res.state = RunnerResultEnum.MemoryLimitExceeded; break;
				case EXIT_RE: res.state = RunnerResultEnum.RuntimeErrorCrash; break;
				case EXIT_OLE: res.state = RunnerResultEnum.OutputLimitExceeded; break;
				case EXIT_IE: res.state = RunnerResultEnum.InternalError; break;
				// FIXME
				default: res.state = RunnerResultEnum.InternalError;
			}
			// Parsing runner's output
			try
			{
				BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedWriter file = null;
				if (fRedirect)
				{
					file = new BufferedWriter(new FileWriter(new File(saveOutputTo)));
					file.write(cmd.toString() + "\n");
				}
				String line;
				
				while ((line = out.readLine()) != null)
				{
					if (fRedirect)
						file.write(line + "\n");
					cnt++;
					// exit code
					if (cnt == 3 && res.state == RunnerResultEnum.OK)
					{
						try
						{
    						String token = line.substring(line.lastIndexOf(" ") + 1, line.length());
    						retValue = Integer.parseInt(token);
    						if (retValue != 0)
    							res.state = RunnerResultEnum.NonZeroExitCode;
						}
						catch (Exception ex){ };
					}
					// time
					if (cnt == 4 && res.state == RunnerResultEnum.OK)
					{
						try
						{
    						String token = line.substring(line.indexOf(':') + 1, line.lastIndexOf("of") - 1);
    						Double val = Double.parseDouble(token);
    						time = (int)Math.round(val * 1000.0);
						}
						catch (Exception ex){ }
					}
					//memory
					else if (cnt == 6 && res.state == RunnerResultEnum.OK)
					{
						try
						{
							String token = line.substring(line.lastIndexOf("  ") + 2, line.lastIndexOf("of") - 1);
							mem = Integer.parseInt(token);			
						}
						catch (Exception ex){ }
					}
				}
				out.close();
				
				if (file != null)
					file.close();
			}
			catch (IOException exc)
			{
				log.error("IOException catched (while parsing runner's stdout): ", exc);
			}
			
			res.OK(retValue, time, mem, output);
			
			log.info(cmd);
			log.info("Retval: " + retValue);
		}
		catch (IOException exc)
		{
			log.error("IOException catched: ", exc);
		}
		return res;
	}
*/
	
	public ExecutionResult run(String command)
	{
		if (Deployment.isOSSupported())
		{
			return null;
		}
		else
		{
			log.fatal("Error. Your OS in not supported");
			return null;
		}
	}
	
	public void saveOutputTo(String file)
	{
		this.saveOutputTo = file;
		fRedirect = true;
	}
}
