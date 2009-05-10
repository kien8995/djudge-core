package com.alt.djudge.judge.validator;

import java.util.Calendar;

import com.alt.utils.StringWorks;




class ValidatorTest
{
	final static String rootDir = "D:\\Temp\\Work\\eJudge\\jTester\\tests\\validator\\";
	
	private String inputFile, outputFile, answerFile;
	
	ValidatorTest(String input, String output, String answer) 
	{
		inputFile = rootDir + input;
		outputFile = rootDir + output;
		answerFile = rootDir + answer;
	}
	
	boolean runTest(Validator val, ValidationResultEnum expectedResult)
	{
		boolean res = false;
		ValidationResult rres = val.Validate(inputFile, outputFile, answerFile);
		ValidationResultEnum tres = rres.Result;
		System.out.println("Test [" + inputFile + ", " + outputFile + ", " + answerFile + "]");
		if (tres != expectedResult)
		{
			System.out.println("!!!Fail!!!");	
			System.out.println("" + rres.Fail + "" + StringWorks.ArrayToString(rres.ValidatorOutput));
			System.out.println();
		}
		else
		{			
			System.out.println("OK " + StringWorks.ArrayToString(rres.ValidatorOutput) + "");
			res = true;
		}
		return res;
	}
	
}


public class UnitTest 
{
	
	
	static public void main(String arg[])
	{
		long time_before = Calendar.getInstance().getTimeInMillis();
		
		ValidatorTest Exact_01_OK = new ValidatorTest("input.in", "Exact_01_OK.out", "Exact_01_OK.ans");
		Exact_01_OK.runTest(new Validator(ValidatorType.InternalExact), ValidationResultEnum.OK);

		ValidatorTest Exact_02 = new ValidatorTest("input.in", "Exact_02_EmptyLineAtBeginning.out", "Exact_02_EmptyLineAtBeginning.ans");
		Exact_02.runTest(new Validator(ValidatorType.InternalExact), ValidationResultEnum.WrongAnswer);
		
		ValidatorTest Exact_03 = new ValidatorTest("input.in", "Exact_03_EmptyLineAtEnd.out", "Exact_03_EmptyLineAtEnd.ans");
		Exact_03.runTest(new Validator(ValidatorType.InternalExact), ValidationResultEnum.WrongAnswer);

		ValidatorTest Exact_04 = new ValidatorTest("input.in", "Exact_04_OK_Stress.out", "Exact_04_OK_Stress.ans");
		Exact_04.runTest(new Validator(ValidatorType.InternalExact), ValidationResultEnum.OK);

		ValidatorTest Int32_01 = new ValidatorTest("input.in", "Int32_01_OK_Stress.out", "Int32_01_OK_Stress.ans");
		Int32_01.runTest(new Validator(ValidatorType.InternalInt32), ValidationResultEnum.OK);
		
		ValidatorTest Int32_02 = new ValidatorTest("input.in", "Int32_02_WA_Stress.out", "Int32_02_WA_Stress.ans");
		Int32_02.runTest(new Validator(ValidatorType.InternalInt32), ValidationResultEnum.WrongAnswer);
		
		// FIXME
		//Exact_01_OK.runTest(new Validator(ValidatorType.InternalInt32), ValidationResultEnum.OK);
		
		long time_after = Calendar.getInstance().getTimeInMillis();
		
		System.out.println("" + (time_after - time_before) + " ms passed");
	}
}
