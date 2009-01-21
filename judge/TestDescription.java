package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import common_data_structures.RunnerFiles;

import utils.PrintfFormat;
import utils.StringWorks;

public class TestDescription extends AbstractDescription
{
	public final static String XMLRootElement = "test";
	
	int testNumber;
	String judgeInput;
	final String judgeInputAttributeName = "input-mask";

	String judgeOutput;
	final String judgeOutputAttributeName = "output-mask";
	
	{
		testNumber = 0;
		judgeInput = judgeOutput = "";
		score = 1;
	}
	
/*	public TestDescription(int number, GroupDescription group)
	{
		testNumber = number;
		problemInfo = group.problemInfo.clone();
	}*/
	
	public TestDescription(int number, GlobalProblemInfo problemInfo, String inputFileMask, String outputFileMask)
	{
		testNumber = number;
		this.problemInfo = problemInfo;
		judgeInput = new PrintfFormat(inputFileMask).sprintf(number + 1);
		judgeOutput = new PrintfFormat(outputFileMask).sprintf(number + 1);
	}

	public TestDescription(int number, GlobalProblemInfo problemInfo, Element item)
	{
		testNumber = number;
		this.problemInfo = problemInfo;
		readXML(item);
	}

	public void setTestNumber(int num)
	{
		this.testNumber = num;
	}
	
	public int getTestNumber()
	{
		return testNumber;
	}
	
	public String getInputFilename()
	{
		return "input.txt";
	}
	
	public String getEtalonFilename()
	{
		return "output.txt";		
	}
	
	@Override
	public Document getXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean readXML(Element elem)
	{
		readCommonXML(elem);
		judgeInput = elem.getAttribute(judgeInputAttributeName);
		judgeOutput = elem.getAttribute(judgeOutputAttributeName);
		return true;
	}

	public void print()
	{
		log("Test #" + testNumber);
		log("judge_input: " + judgeInput);
		log("judge_output: " + judgeOutput);
	}

	@Override
	public void overrideFiles(RunnerFiles newFiles)
	{
		problemInfo.files = newFiles;
	}
}
