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

package djudge.judge.validator;

public class Validator
{
	public final static String XMLRootElement = "validator";
	
	ValidatorDescription desc;
	
	public Validator()
	{
		// TODO Auto-generated constructor stub
	}
	
	public Validator(ValidatorDescription desc)
	{
		this.desc = desc;
	}
	
	public ValidationResult Validate(String input, String output, String answer)
	{
		ValidationResult res = new ValidationResult("" + desc.type);
		switch (desc.type)
		{
		case ExternalExitCode:
			res = (new ValidatorExitCode(desc.getCheckerPath())).Validate(input, output, answer);
			break;

		case ExternalExitCodeExtended:
			res = (new ValidatorExitCodeExtended(desc.getCheckerPath())).Validate(input, output, answer);
			break;

		case ExternalTestLib:
			res = (new ValidatorTestLib(desc.getCheckerPath())).Validate(input, output, answer);
			break;

		case InternalExact:	
			res = (new Validator_String()).Validate(input, output, answer);
			break;

		case InternalInt32:
			res = (new Validator_Int32()).Validate(input, output, answer);
			break;

		case InternalFloatAbs:
			double eps = Double.parseDouble(desc.param);
			res = (new Validator_FloatAbs(eps).Validate(input, output, answer));
			break;
		
		case InternalFloatRel:
			double eps3 = Double.parseDouble(desc.param);
			res = (new Validator_FloatRel(eps3).Validate(input, output, answer));
			break;
		
		case InternalFloatOther:
			double eps4 = Double.parseDouble(desc.param);
			res = (new Validator_FloatOther(eps4).Validate(input, output, answer));
			break;
		
		case InternalFloatAbsRel:
			double eps2 = Double.parseDouble(desc.param);
			res = (new Validator_FloatAbsRel(eps2).Validate(input, output, answer));
			break;

		case ExternalPC2:
//			break;

		case InternalInt64:
//			break;

		case InternalToken:
			res = (new Validator_Token()).Validate(input, output, answer);
			break;

		case ExternalCustom:
//			break;

		case Unknown:
		default:
			res.result = ValidationResultEnum.InternalError;
			res.fail = ValidationFailEnum.ValidatorNotFounded;
			System.out.println("Unknown validator: " + desc.type);
		}

		return res;
	}	
}
