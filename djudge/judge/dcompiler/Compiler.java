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

package djudge.judge.dcompiler;

import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import utils.FileWorks;

import djudge.common.Loggable;

import javax.xml.parsers.*;

public class Compiler extends Loggable
{
	private static final Logger log = Logger.getLogger(Compiler.class);
	
	private static TreeMap<String, Language> languages = new TreeMap<String, Language>();
	
	private static boolean fSetUp = false; 
	
	static
	{
		setupCompiler();
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
			log.info("Setting up compiler");
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.parse(filename);
	        NodeList lang = doc.getElementsByTagName("language");
	        for (int i = 0; i < lang.getLength(); i++)
	        {
	        	Language l = new Language((Element) lang.item(i));
	        	languages.put(l.getID(), l);
	        	log.info("Language added: " + l.getID());
	        }
		}
		catch(Exception exc)
		{
			log.error("Error", exc);
		}
	}
	
	/**
	 * 
	 * @param file - Path to source file
	 * @param LanguageID - Language id (or '%AUTO%' for auto)
	 * @return {@link CompilationInfo}
	 */
	public static CompilerResult compile(String file, String LanguageID)
	{
		CompilerTask task = new CompilerTask();
		task.languageId = LanguageID;
		task.files = new DistributedFileset(file);
		return compile(task);
	}
	
	public static CompilerResult compile(CompilerTask task)
	{
		String LanguageID = task.languageId;
		CompilerResult res = new CompilerResult();
		String file = task.files.map.keySet().toArray(new String[0])[0];
		log.info("Compiling file " + file + ", language " + task.languageId);
		
		// Empty ID
		if (LanguageID == null || LanguageID == "")
			res.result = CompilationResult.UnknownCompiler;
		// Automatic language
		else if (LanguageID.equalsIgnoreCase("%AUTO%"))
		{
			String native_output[] = new String[0];
			Object[] LangId = languages.keySet().toArray();
			String Extension = FileWorks.getExtension(file);
			for (int i = 0; (i < LangId.length) && (res.result != CompilationResult.OK); i++)
			{
				Language lng = languages.get(LangId[i]);
				res = lng.compile(task);
				if (Extension.equals(lng.getExtension()))
					native_output = res.getCompilerOutput();
			}
			if (res.result != CompilationResult.OK && native_output != null && native_output.length > 0)
				res.setCompilerOutput(native_output);
		}
		else
		{
			Language lang = languages.get(LanguageID);
			// No language
			if (lang == null)
				res.result = CompilationResult.UnknownCompiler;
			// OK
			else
				res = lang.compile(task);
		}
		
		return res;
	}
	
	public static void showInfo()
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
