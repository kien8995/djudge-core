/* $Id: Compiler.java, v 0.1 2008/08/13 05:13:08 alt Exp $ */

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

import java.io.File;
import java.util.TreeMap;

import org.w3c.dom.*;

import com.alt.utils.FileWorks;



import javax.xml.parsers.*;

public class Compiler 
{
	private static TreeMap<String, Language> languages = new TreeMap<String, Language>();
	
	private static boolean fSetUp = false; 
	
	static
	{
		setupCompiler();
	}
	
	public static boolean isValidLanguage(String id)
	{
		return languages.containsKey(id);
	}
	
	public static LanguageInfoInternal getLanguage(String id)
	{
		if (languages.containsKey(id))
			return languages.get(id).info;
		return null;
	}
	
	// FIXME
	public static boolean updateLanguage(LanguageInfoInternal info)
	{
		return true;
	}
	
	// FIXME
	public static boolean addLanguage(LanguageInfoInternal info)
	{
		return true;
	}
	
	public static LanguageInfoInternal[] getLanguagesInfo()
	{
		int n = languages.size();
		Language[] l = languages.values().toArray(new Language[0]);
		
		LanguageInfoInternal[] res = new LanguageInfoInternal[n];
		for (int i = 0; i < n; i++)
			res[i] = l[i].getLanguageInfo();
		
		return res;
	}
	
	// FIXME
	public static boolean addLanguage()
	{
		return true;
	}
	
	// FIXME
	public static boolean removeLanguage(String id)
	{
		return true;
	}
	
	public static int getCount()
	{
		return languages.size();
	}
	
	public static void setupCompiler()
	{
		setupCompiler("./languages.xml");
	}
	
	public static void setupCompiler(String filename)
	{
		if (fSetUp) return;
		fSetUp = true;
		try
		{
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.parse(filename);
	        NodeList lang = doc.getElementsByTagName("language");
	        for (int i = 0; i < lang.getLength(); i++)
	        {
	        	Language l = new Language((Element)lang.item(i));
	        	languages.put(l.getID(), l);
	        }
		}
		catch(Exception exc)
		{
			// FIXME
		}
	}
	
	/**
	 * 
	 * @param file - Path to source file
	 * @param LanguageID - Language id (or '%AUTO%' for auto)
	 * @return {@link CompilationInfo}
	 */
	public static CompilationInfo Compile(String file, String LanguageID)
	{
		CompilationInfo res = new CompilationInfo();
		File f = new File(file);
		
		// No such file
		if (!f.exists()) res.state = CompilationResult.FileNotFounded;
		// Empty ID
		else if (LanguageID == null || LanguageID == "")
			res.state = CompilationResult.UnknownCompiler;
		// Automatic language
		else if (LanguageID.equalsIgnoreCase("%AUTO%"))
		{
			System.out.println(LanguageID);
			String native_output[] = new String[0];
			Object[] LangId = languages.keySet().toArray();
			String Extension = FileWorks.getExtension(file);
			for (int i = 0; (i < LangId.length) && (res.state != CompilationResult.OK); i++)
			{
				Language lng = languages.get(LangId[i]);
				res = lng.Compile(file);
				if (Extension.equals(lng.getExtension()))
					native_output = res.getCompilerOutput();
			}
			if (res.state != CompilationResult.OK && native_output.length > 0)
				res.setCompilerOutput(native_output);
		}
		else
		{
			Language lang = languages.get(LanguageID);
			// No language
			if (lang == null)
				res.state = CompilationResult.UnknownCompiler;
			// OK
			else
				res = lang.Compile(file);
		}
		
		return res;
	}
	
	public static void ShowInfo()
	{
		System.out.println("                              Languages Info");
		Object[] LangId = languages.keySet().toArray();
		for (int i = 0; i < LangId.length; i++)
		{
			System.out.println("********************************************************************************");
			((Language)languages.get(LangId[i])).ShowInfo();
		}
		System.out.println("********************************************************************************");			
		System.out.println("                              Total " + LangId.length + " Languages\n");
	}
}
