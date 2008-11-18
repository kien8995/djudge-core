package judge;

import java.io.File;

import runner.Runner;
import runner.RunnerResult;
import runner.RunnerResultEnum;
import utils.FileWorks;
import validator.ValidationResult;

import common.JudgeException;
import common.JudgeExceptionType;
import common.settings;

public class Judge
{
	
	public TestResult judgeTest(String solutionID, String command, String inputGlobalFile, String etalonGlobalFile, TestDescription desc) throws JudgeException
	{
		TestResult res = new TestResult();
		File f;
		
		String globalTempDir = settings.getTempDir();
		String tempDir = globalTempDir + solutionID + "/";
		
        // global input & etalon files checking 
        f = new File(inputGlobalFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotFound, inputGlobalFile, "global input test #" + desc.getTestNumber());

        f = new File(etalonGlobalFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotFound, etalonGlobalFile, "etalon test #" + desc.getTestNumber());

        // local answer & input files
		String inputFile = tempDir + desc.getInputFilename();		
		String answerFile = tempDir + desc.getFiles().outputFilename;
        
        f = new File(inputFile);
        FileWorks.CopyFile(inputFile, inputGlobalFile);
        if (!f.exists())
        	throw new JudgeException(JudgeExceptionType.FileNotCreated, inputFile, "local input test #" + desc.getTestNumber());                
        
        // now all files are present, we can running 'command'
        
        f = new File(command);
        if (f.exists())
        {
        	String cexename = FileWorks.getFileName(command);
        	String new_commad = tempDir + cexename;
        	FileWorks.CopyFile(new_commad, command);
        	f = new File(new_commad);
        	if (f.exists())
        		command = new_commad;
        }
    	
        Runner run = new Runner(desc.getLimits(), desc.getFiles());
        run.saveOutputTo(tempDir + "runner.out");
    	ValidationResult validationInfo = new ValidationResult("No_validator");
    	RunnerResult runtimeInfo = run.run(command);
    		
    	if (runtimeInfo.state == RunnerResultEnum.OK)
    		validationInfo = desc.getValidator().Validate(inputGlobalFile, etalonGlobalFile, answerFile);
    		
    	res.setRuntimeInfo(runtimeInfo);
    	res.setValidationInfo(validationInfo);
        
		return res;
	}
	
}
