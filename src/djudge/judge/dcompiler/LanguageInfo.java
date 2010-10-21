/* $Id$ */

package djudge.judge.dcompiler;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LanguageInfo
{
	// Command for compilation (e.g. 'g++ %name%ext -o %name.exe')
	private String compileCommand;
	
	// %name.exe
	private String runCommand;
	
	// VC60
	private String languageID;
	
	// C++
	@SuppressWarnings("unused")
	private String shortName;
	
	// Microsoft Visual C++ 6.0
	private String fullName;
	
	// test.cpp
	private String testSolutions[];
	
	// .cpp
	private String extension;
	
	// extensions
	private Set<String> extensions = new HashSet<String>();
	
	public void showInfo()
	{
		System.out.println("ID: " + languageID);
		System.out.println("Name: " + fullName);
		System.out.println("CompileCommand: " + compileCommand);
		System.out.println("RunCommand: " + runCommand);
		System.out.println("Extension: " + extension);
		System.out.println("Test solutions:");
		for (int i = 0; i < testSolutions.length; i++)
			System.out.println(testSolutions[i]);
		System.out.println("Extensions:");
		for (String s : extensions)
			System.out.println(s);
	}
	
	public LanguageInfo(Element lang)
	{
		compileCommand = lang.getAttribute("compile-command");
		runCommand = lang.getAttribute("run-command");
		languageID = lang.getAttribute("id");
		testSolutions = new String[1]; 
		testSolutions[0] = lang.getAttribute("test-solution");
		fullName = lang.getAttribute("compiler-name");
		shortName = lang.getAttribute("compiler-name");
		extension = lang.getAttribute("file-extension");
		NodeList exts = lang.getElementsByTagName("extension");
		for (int i = 0; i < exts.getLength(); i++)
		{
			Element elem = (Element) exts.item(i);
			extensions.add(elem.getTextContent());
		}
	}
	
	public String getID()
	{
		return languageID;
	}
	
	public String getName()
	{
		return fullName;
	}
	
	public String getExtension()
	{
		return extension;
	}
	public String getCompileCommand()
	{
		return compileCommand;
	}
	
	public String getRunCommand()
	{
		return runCommand;
	}

	public Set<String> getExtensions()
	{
		return extensions;
	}
}
