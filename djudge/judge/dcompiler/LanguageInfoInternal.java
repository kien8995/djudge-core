package djudge.judge.dcompiler;

import org.w3c.dom.Element;

public class LanguageInfoInternal
{
	// Command for compilation (e.g. 'g++ %name%ext -o %name.exe')
	private String compileCommand;
	
	// %name.exe
	private String runCommand;
	
	// VC60
	private String LanguageID;
	
	// C++
	@SuppressWarnings("unused")
	private String ShortName;
	
	// Microsoft Visual C++ 6.0
	private String FullName;
	
	// test.cpp
	private String TestSolutions[];
	
	// .cpp
	private String Extension;
	
	public void showInfo()
	{
		System.out.println("ID: " + LanguageID);
		System.out.println("Name: " + FullName);
		System.out.println("CompileCommand: " + compileCommand);
		System.out.println("RunCommand: " + runCommand);
		System.out.println("Extension: " + Extension);
		System.out.println("Test solutions:");
		for (int i = 0; i < TestSolutions.length; i++)
			System.out.println(TestSolutions[i]);		
	}
	
	public LanguageInfoInternal(Element lang)
	{
		compileCommand = lang.getAttribute("compile-command");
		runCommand = lang.getAttribute("run-command");
		LanguageID = lang.getAttribute("id");
		TestSolutions = new String[1]; 
		TestSolutions[0] = lang.getAttribute("test-solution");
		FullName = lang.getAttribute("compiler-name");
		ShortName = lang.getAttribute("compiler-name");
		Extension = lang.getAttribute("file-extension");
	}
	
	public String getID()
	{
		return LanguageID;
	}
	
	public String getName()
	{
		return FullName;
	}
	
	public String getExtension()
	{
		return Extension;
	}
	public String getCompileCommand()
	{
		return compileCommand;
	}
	
	public String getRunCommand()
	{
		return runCommand;
	}
}
