/* $Id: Validator.java, v 0.1 2008/08/06 05:13:08 alt Exp $ */

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

import org.w3c.dom.Element;

import com.alt.utils.FileWorks;



public class Validator
{
	public final static String XMLRootElement = "validator";
	
	private ValidatorType type;
	
	private String validatorParam;
	
	private String exeFile;
	
	@SuppressWarnings("unused")
	private int verbose;
	
	public void setVerbose(int vVerbose)
	{
		verbose = vVerbose;
	}
	
	public void printInfo()
	{

	}
	
	private ValidatorType OldStringToType(String s)
	{
		s = s.toUpperCase();
		ValidatorType res = ValidatorType.Unknown;
		if (s.equals("@STR")) res = ValidatorType.InternalExact;
		else if (s.equals("@TOKEN")) res = ValidatorType.InternalToken;
		else if (s.equals("@INT32")) res = ValidatorType.InternalInt32;
		else if (s.equals("@INT64")) res = ValidatorType.InternalInt64;
		else if (s.equals("@FLOAT")) res = ValidatorType.InternalFloatAbs;
		else if (s.equals("@FLOAT2")) res = ValidatorType.InternalFloatAbsRel;
		else if (s.equals("%STDLIB")) res = ValidatorType.ExternalTestLib;
		else if (s.equals("%PC2")) res = ValidatorType.ExternalPC2;
		else if (s.equals("%EXITCODE")) res = ValidatorType.ExternalExitCode;
		else if (s.equals("%EXITCODE_EXTENDED")) res = ValidatorType.ExternalExitCodeExtended;
		return res;
	}

	// v2.0 validator id's
	private ValidatorType StringToType(String s)
	{
		s = s.toUpperCase();
		ValidatorType res = ValidatorType.Unknown;
		if (s.equals("%STR%")) res = ValidatorType.InternalExact;
		else if (s.equals("%INT32%")) res = ValidatorType.InternalInt32;
		else if (s.equals("%INT64%")) res = ValidatorType.InternalInt64;
		else if (s.equals("%FLOAT%")) res = ValidatorType.InternalFloatAbs;
		else if (s.equals("%TESTLIB%")) res = ValidatorType.ExternalTestLib;
		else if (s.equals("%PC2%")) res = ValidatorType.ExternalPC2;
		else if (s.equals("%RET_VAL%")) res = ValidatorType.ExternalExitCode;
		else if (s.equals("%RET_VAL_EXTENDED%")) res = ValidatorType.ExternalExitCodeExtended;
		return res;
	}

	public ValidationResult Validate(String input, String output, String answer)
	{
		ValidationResult res = new ValidationResult("" + type);
		switch (type)
		{
		case ExternalExitCode:
			res = (new Validator_ExitCode(exeFile)).Validate(input, output, answer);
			break;

		case ExternalExitCodeExtended:
			res = (new Validator_ExitCodeExtended(exeFile)).Validate(input, output, answer);
			break;

		case ExternalTestLib:
			res = (new Validator_TestLib(exeFile)).Validate(input, output, answer);
			break;

		case InternalExact:
			res = (new Validator_String()).Validate(input, output, answer);
			break;

		case InternalInt32:
			res = (new Validator_Int32()).Validate(input, output, answer);
			break;

		case InternalFloatAbs:
			double eps = Double.parseDouble(validatorParam);
			res = (new Validator_FloatAbs(eps).Validate(input, output, answer));
			break;
		
		case InternalFloatAbsRel:
			double eps2 = Double.parseDouble(validatorParam);
			res = (new Validator_FloatAbsRel(eps2).Validate(input, output, answer));
			break;

		case ExternalPC2:
//			break;

		case InternalInt64:
//			break;

		case InternalToken:
//			break;

		case ExternalCustom:
//			break;

		case Unknown:
		default:
			res.Result = ValidationResultEnum.InternalError;
			res.Fail = ValidationFailEnum.ValidatorNotFounded;
			System.out.println("Unknown validator");
		}

		return res;
	}
	
	private void init(ValidatorType type, String param, String exeFile)
	{
		this.type = type;
		this.validatorParam = param;
		this.exeFile = exeFile;
	}

	public Validator(ValidatorType type)
	{	
		init(type, "", "");
	}	
	
	public Validator(ValidatorType type, String param, String exe)
	{
		init(type, param, exe);
	}
	
	public Validator(String checker_old, String param, String path)
	{
		path = path.substring(0, path.length() - 1);
		init(OldStringToType(checker_old), param, FileWorks.ConcatPaths(path, "check.exe"));
	}

	public Validator(Element elem)
	{
		String SType = elem.getAttribute("type");
		ValidatorType ttype = StringToType(SType);
		String texeFile = elem.getAttribute("file");
		String tvalidatorParam = elem.getAttribute("param");
		init(ttype, texeFile, tvalidatorParam);
	}

	public Validator(Element elem, int flag)
	{
		String SType = elem.getAttribute("type");
		ValidatorType ttype = OldStringToType(SType);
		String texeFile = elem.getAttribute("file");
		String tvalidatorParam = elem.getAttribute("param");
		init(ttype, texeFile, tvalidatorParam);
	}

}
