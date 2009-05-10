/* $Id: CompilerTest.java, v 0.1 2008/07/22 05:13:08 alt Exp $ */

/* Copyright (C) 2008 Olexiy Palinkash <olexiy.palinkash@gmail.com> */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */ 

package com.alt.djudge.judge.compiler;

import java.util.ArrayList;

public class CompilerTest 
{
	static private ArrayList<String> list = new ArrayList<String>();
	static private ArrayList<CompilationResult> state = new ArrayList<CompilationResult>();	
	
	static boolean TestLanguage(String lid)
	{
		System.out.println("Testing Language: " + lid);
		int err = 0;
		for (int i = 0; i < list.size(); i++)
		{
			CompilationInfo res = Compiler.Compile(list.get(i), lid); 
			if (res.state != state.get(i))
			{
				System.out.println("Test #" + (i + 1) + " Failed [" + list.get(i) + "]");
				System.out.println("Expected: " + state.get(i) + ", received: " + res.state);
				err++;
			}
			else 
			{
				System.out.println("Test #" + (i + 1) + " OK [" + list.get(i) + ", " + res.state + "]");
			}
		}
		System.out.println("Test count: " + list.size());
		if (err > 0) 
			System.out.println(err + " error occured\n");
		else
			System.out.println("All tests passed\n");
		
		return (err == 0);
	}
	
	public static void AddTest(String file, CompilationResult res)
	{
		final String Root = "D:\\Temp\\Work\\eJudge\\jTester\\tests\\compiler\\";
		list.add(Root + file);
		state.add(res);
	}
	
	public static void ClearTests()
	{
		list.clear();
		state.clear();
	}
	
	public static boolean func_test()
	{
		Compiler.setupCompiler("./tests/compiler/languages.xml");		
		Compiler.ShowInfo();

		boolean ok = true;
		
		// MSVCPP
		/*		
		ClearTests();
		//AddTest("vc_CE.cpp", CompilationResult.CompilationError);
		AddTest("vc_OK.cpp", CompilationResult.OK);
		ok &= TestLanguage("VC60");
*/
		
		// G++
		ClearTests();
		AddTest("g++_CE.cc", CompilationResult.CompilationError);
		AddTest("g++_OK.cc", CompilationResult.OK);
		AddTest("g++_MEMLIMIT.cc", CompilationResult.CompilationError);		
		AddTest("g++_NoSuchFile.cc", CompilationResult.FileNotFounded);
		ok &= TestLanguage("GCC342");
		
		// BD
		ClearTests();
		AddTest("delphi_CE.dpr", CompilationResult.CompilationError);
		AddTest("delphi_OK.dpr", CompilationResult.OK);
		ok &= TestLanguage("BD");
		
		// FPC220
		ClearTests();
		//AddTest("pascal_CE.pas", CompilationResult.CompilationError);
		AddTest("pascal_OK.pas", CompilationResult.OK);
		ok &= TestLanguage("FPC220");
		
		// Wrong Compiler
		ClearTests();
		AddTest("pascal_CE.pas", CompilationResult.UnknownCompiler);
		AddTest("pascal_CE.dpr", CompilationResult.FileNotFounded);
		ok &= TestLanguage("NOSUCHLANGUAGE");
		
		// %AUTO%
/*		ClearTests();
		AddTest("g++_CE.cc", CompilationResult.CompilationError);
		AddTest("g++_OK.cc", CompilationResult.OK);
		AddTest("delphi_CE.dpr", CompilationResult.CompilationError);
		AddTest("delphi_OK.dpr", CompilationResult.OK);
		AddTest("pascal_OK.pas", CompilationResult.OK);
		AddTest("paóöâöóscal_OK.dpr", CompilationResult.FileNotFounded);
		ok &= TestLanguage("%AUTO%");
*/				
		if (ok)
			System.out.println("All languages passed tests\n");
		else
			System.out.println("Some languages failed\n");
		
		return ok;
	}
	
	
	public static void main(String arg[])
	{
		func_test();
	}
}
