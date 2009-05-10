/* $Id: CompilationInfo.java, v 0.1 2008/07/16 05:13:08 alt Exp $ */

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

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.common.XMLSerializable;
import com.alt.utils.XmlWorks;




public class CompilationInfo extends XMLSerializable implements Serializable 
{
	public final static String XMLRootElement = "compilation";

	private static final long serialVersionUID = 1L;
	
	CompilationResult state;
	final String stateAttributeName = "state";
	
	String command;

	ArrayList<String> compilerOutput;
	final String compilerOutputAttributeName = "compiler";
	
	int compilerExitCode;
	
	public CompilationInfo()
	{
		state = CompilationResult.Undefined;
		compilerOutput = new ArrayList<String>();
	}
	
	public void setCompilerOutput(String[] data)
	{
		compilerOutput.clear();
		for (int i = 0; i < data.length; i++)
			compilerOutput.add(data[i]);
	}
	
	public String[] getCompilerOutput()
	{
		return compilerOutput.toArray(new String[0]);
	}
	
	public boolean isSuccessfull()
	{
		return state == CompilationResult.OK;
	}
	
	public String getCommand()
	{
		return command;
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(stateAttributeName, "" + state);
		res.setAttribute(compilerOutputAttributeName, "" + compilerOutput.toString());
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
