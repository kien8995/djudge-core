// TODO: review this class (old version used)

package runner;

import java.io.*;

import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;
import common_data_structures.RunnerSecurityLimits;

public class Runner
{
	private String homeDirectory;
	
	private RunnerLimits limits;
	
	private RunnerFiles files;
	
	String saveOutputTo;
	boolean fRedirect = false;
	
	
	@SuppressWarnings("unused")
	private RunnerSecurityLimits security = new RunnerSecurityLimits();
	
	public Runner(String homeDir, RunnerLimits limits, RunnerFiles files)
	{
		this.homeDirectory = homeDir;
		this.limits = limits;
		this.files = files;
	}
	
	public Runner(RunnerLimits limits, RunnerFiles files)
	{
		this.limits = limits;
		this.files = files;
	}
	
	public Runner(RunnerLimits limits)
	{
		this.limits = limits;
		this.files = new RunnerFiles();
	}	
	
	public Runner()
	{
		this.files = new RunnerFiles();
		this.limits = new RunnerLimits();
	}
	
	public RunnerResult run(String command)
	{
		StringBuffer cmd = new StringBuffer();
		
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
		
		System.out.println(cmd);

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
						System.out.println(line);
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
	}
	
	public void saveOutputTo(String file)
	{
		this.saveOutputTo = file;
		fRedirect = true;
	}
}
