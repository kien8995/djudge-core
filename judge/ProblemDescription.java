package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import common.settings;
import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

import utils.StringWorks;
import utils.XmlWorks;
import validator.Validator;

public class ProblemDescription extends AbstractDescription 
{
	int groupsCount;
	GroupDescription[] groups;
	
	protected String problemRoot;
	
	public ProblemDescription(String contestID, String problemID)
	{
		loadByProblemContestIDs(contestID, problemID);
	}
	
	private void loadByProblemContestIDs(String contestID, String problemID)
	{
		problemRoot = settings.getProblemsDir() + contestID + "/" + problemID + "/";
		String problemXML = problemRoot + "problem.xml";
		ParseOldXML(XmlWorks.getDocument(problemXML).getDocumentElement());
	}
	
	private void ParseOldXML(Element elem)
	{
		problemInfo = new GlobalProblemInfo();
		
		problemInfo.problemRoot = problemRoot;
		
		// Only one group
		groupsCount = 1;
		// constant type
		problemInfo.type = ProblemTypeEnum.ACM;
		// ProblemID
		problemInfo.problemID = elem.getAttribute("id");
		
		// Test-group
		int testsCount = Integer.parseInt(elem.getAttribute("test-count"));
		String inputFileMask = elem.getAttribute("input-mask");
		String outputFileMask = elem.getAttribute("output-mask");		
		
		String in = problemInfo.programInputFilename = elem.getAttribute("input-file");
		String out = problemInfo.programOutputFilename = elem.getAttribute("output-file");
		
		problemInfo.files = new RunnerFiles(
				in.length() == 0 || in.equalsIgnoreCase("stdin") ? "input.txt" : null,
				out.length() == 0 || out.equalsIgnoreCase("stdout") ? "output.txt" : null
				);
		
		// Limits
		problemInfo.limits = new RunnerLimits();
		problemInfo.limits.outputLimit = StringWorks.StrToMemoryLimit(elem.getAttribute("output-limit"));
		problemInfo.limits.memoryLimit = StringWorks.StrToMemoryLimit(elem.getAttribute("memory-limit"));
		problemInfo.limits.timeLimit = StringWorks.StrToTimeLimit(elem.getAttribute("time-limit"));
		
		// Validator
		String checker = elem.getAttribute("checker");
		String checkerParam = elem.getAttribute("checker-param");
		problemInfo.validator = new Validator(checker, checkerParam, problemInfo.problemRoot);
		
		groups = new GroupDescription[1];
		groups[0] = new GroupDescription(0, testsCount, problemInfo, inputFileMask, outputFileMask);
	}
	
	public String toString()
	{
		StringBuilder res = new StringBuilder();
		
		res.append(groupsCount); 
		
		return res.toString();
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
		problemInfo.print();
		log("Groups: " + groupsCount);
		for (int i = 0; i < groupsCount; i++)
		{
			groups[i].print();
		}
	}

	@Override
	public void overrideFiles(RunnerFiles newFiles)
	{
		for (int i = 0; i < groupsCount; i++)
			groups[i].overrideFiles(newFiles);
	}
}
