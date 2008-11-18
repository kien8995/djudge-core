package judge;

import org.w3c.dom.Element;

import common.settings;
import common_data_structures.RunnerFiles;
import common_data_structures.RunnerLimits;

import utils.StringWorks;
import utils.XmlWorks;
import validator.Validator;

public class ProblemDescription 
{
	GlobalProblemInfo globalInfo;

	protected int groupsCount;
	protected GroupDescription[] groups;
	
	protected String[] solutions;
	
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
		globalInfo = new GlobalProblemInfo();
		
		globalInfo.problemRoot = problemRoot;
		
		// Only one group
		groupsCount = 1;
		// constant type
		globalInfo.type = ProblemTypeEnum.ACM;
		// ProblemID
		globalInfo.problemID = elem.getAttribute("id");
		
		// Test-group
		int testsCount = Integer.parseInt(elem.getAttribute("test-count"));
		String inputFileMask = elem.getAttribute("input-mask");
		String outputFileMask = elem.getAttribute("output-mask");		
		
		String in = globalInfo.programInputFilename = elem.getAttribute("input-file");
		String out = globalInfo.programOutputFilename = elem.getAttribute("output-file");
		
		globalInfo.files = new RunnerFiles(
				in.length() == 0 || in.equalsIgnoreCase("stdin") ? "input.txt" : null,
				out.length() == 0 || out.equalsIgnoreCase("stdout") ? "output.txt" : null
				);
		
		// Limits
		globalInfo.limits = new RunnerLimits();
		globalInfo.limits.outputLimit = StringWorks.StrToMemoryLimit(elem.getAttribute("output-limit"));
		globalInfo.limits.memoryLimit = StringWorks.StrToMemoryLimit(elem.getAttribute("memory-limit"));
		globalInfo.limits.timeLimit = StringWorks.StrToTimeLimit(elem.getAttribute("time-limit"));
		
		// Validator
		String checker = elem.getAttribute("checker");
		String checkerParam = elem.getAttribute("checker-param");
		globalInfo.val = new Validator(checker, checkerParam, globalInfo.problemRootDirectory);
		
		groups = new GroupDescription[1];
		groups[0] = new GroupDescription(0, testsCount, globalInfo.clone(), inputFileMask, outputFileMask);
	}
	
}
