/* $Id: ValidationResult.java, v 0.1 2008/07/23 05:13:08 alt Exp $ */

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

package com.alt.djudge.judge.validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.common.XMLSerializable;
import com.alt.djudge.judge.executor.RunnerResult;
import com.alt.utils.StringWorks;
import com.alt.utils.XmlWorks;




public class ValidationResult extends XMLSerializable 
{
	
	public ValidationResultEnum Result;

	public ValidationFailEnum Fail;
		
	public int ValidatorExitCode;
	
	public String[] ValidatorOutput;
	
	public RunnerResult RunInfo;
	
	public String ValidatorName;
	
	public ValidationResult(String Name)
	{
		ValidatorName = Name;
		ValidatorOutput = new String[0];
		Result = ValidationResultEnum.Undefined;
		Fail = ValidationFailEnum.Undefined;
		RunInfo = new RunnerResult();
	}
	
	public ValidationResult(Element elem)
	{
		readXML(elem);
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		try
		{
			Element res = doc.createElement("val");
			res = doc.createElement("validator");
			res.setAttribute("validator", "" + ValidatorName);
			res.setAttribute("result", "" + Result);
			res.setAttribute("fail", "" + Fail);
			res.setAttribute("output", StringWorks.ArrayToString(ValidatorOutput));
			doc.appendChild(res);
		}
		catch (Exception exc)
		{
			// FIXME
			System.out.println("!!![ValidationResult.toXml]: " + exc);
		}
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		// FIXME Auto-generated method stub
		return false;
	}
	
	
}
