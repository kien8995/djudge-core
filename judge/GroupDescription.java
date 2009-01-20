package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import common_data_structures.RunnerFiles;

public class GroupDescription extends AbstractDescription
{
	public final static String XMLRootElement = "group";
	int groupNumber;
	int testsCount;
	TestDescription[] tests;
	String inputMask;
	String etalonMask;
	int score = 1;
	
	public GroupDescription(int number, ProblemDescription problem)
	{
		groupNumber = number;
		problemInfo = problem.problemInfo.clone();
	}

	public GroupDescription(int number, int testsCount, GlobalProblemInfo problemInfo,
			String inputFileMask, String outputFileMask)
	{
		inputMask = inputFileMask;
		etalonMask = outputFileMask;
		this.problemInfo = problemInfo;
		groupNumber = number;
		this.testsCount = testsCount;
		tests = new TestDescription[testsCount];
		for (int i = 0; i < testsCount; i++)
			tests[i] = new TestDescription(i, problemInfo, inputFileMask, outputFileMask);
	}

	public GroupDescription(int number, GlobalProblemInfo problem, Element item)
	{
		groupNumber = number;
		problemInfo = problem;
		readXML(item);
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
		// TODO Auto-generated method stub
		return false;
	}

	public void print()
	{
		log("Group #" + groupNumber);
		log("InputMask: " + inputMask);
		log("EtalonMask: " + etalonMask);
		for (int i = 0; i < testsCount; i++)
			tests[i].print();
	}

	@Override
	public void overrideFiles(RunnerFiles newFiles)
	{
		for (int i = 0; i < testsCount; i++)
			tests[i].overrideFiles(newFiles);
	}
}
