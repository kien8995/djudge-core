/*
 * 
 * 
 * 
 */

package djudge.judge.dcompiler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

import utils.FileWorks;


import djudge.common.Loggable;
import djudge.common.settings;
import djudge.judge.dexecutor.ExecutorFiles;
import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.dexecutor.ExecutorProgram;
import djudge.judge.dexecutor.ExecutorTask;
import djudge.judge.dexecutor.LocalExecutor;

/**
 * Describes programming language
 * @author alt
 */
public class Language extends Loggable
{
	LanguageInfoInternal info;
	
	public LanguageInfoInternal getLanguageInfo()
	{
		return info;
	}
	
	public String getExtension()
	{
		return info.getExtension();
	}
	
	Language(Element lang)
	{
		info = new LanguageInfoInternal(lang);
	}
	
	public void ShowInfo()
	{
		info.showInfo();
	}
	
	public String getID()
	{
		return info.getID();
	}
	
	public CompilerResult compile(CompilerTask task)
	{
		CompilerResult res = new CompilerResult();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        String id = dateFormat.format(new Date());
		String tempDir = settings.getWorkDir() + id + "/";
		try
		{
			task.files.unpack(tempDir);
			String fileNameExt = task.files.getFile();
			String fileName = FileWorks.getNameOnly(tempDir + fileNameExt);
			String fileDirNameExt = FileWorks.getAbsolutePath(tempDir + fileNameExt);
			
			String fileDirNameExtComp = tempDir + fileName + info.getExtension();
			
			if (!fileNameExt.equals(fileName + info.getExtension()))
				FileWorks.CopyFile(fileDirNameExtComp, fileDirNameExt);
			
			StringBuffer cmd = new StringBuffer(info.getCompileCommand());
			int i1;
			while ((i1 = cmd.indexOf("%name")) != -1)
				cmd.replace(i1, i1 + 5, fileName);
			while ((i1 = cmd.indexOf("%ext")) != -1)
				cmd.replace(i1, i1 + 4, info.getExtension());
			
			/* Preparing ExecutorTask structure */
			ExecutorProgram program = new ExecutorProgram();
			program.files = task.files;
			program.files.addFile(fileDirNameExtComp);
			program.command = cmd.toString();//info.getRunCommand().replace("%name", fileDirName);
			
			ExecutorTask exTask = new ExecutorTask();
			exTask.returnDirectoryContent = true;
			
			exTask.files = new ExecutorFiles();
			
			//FIXME: Hardcode
			exTask.limits = new ExecutorLimits(20000, 256 * 1024 * 1024);
			
			exTask.program = program;
			
			LocalExecutor exec = new LocalExecutor();
			res.compilerExecution = exec.execute(exTask);
		
			if (res.compilerExecution.getExitCode() == 0)
			{
				res.result = CompilationResult.OK;
			}
			else
			{
				res.result = CompilationResult.CompilationError;
			}
			
			CompiledProgram pr2 = new CompiledProgram();
			pr2.files = res.compilerExecution.getFiles();
			pr2.runCommand = info.getRunCommand().replace("%name", fileName);
			res.program = pr2;
		}
		catch (Exception exc)
		{
			System.out.println("Exception in DJLanguage: " + exc);
			exc.printStackTrace();
		}
		FileWorks.deleteDirectory(tempDir);
		return res;
	}
	
	public static void main2(String[] args)
	{
		CompilerTask task = new CompilerTask();
		task.languageId = "GCC342";
		task.files = new DistributedFileset("d:\\1000.cpp");
		
		CompilerResult res = Compiler.compile(task);
		System.out.println(res.result);
		
	}
}
