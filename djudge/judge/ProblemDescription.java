package djudge.judge;

import java.io.FileNotFoundException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import utils.PrintfFormat;
import utils.StringWorks;
import utils.XmlWorks;
import djudge.common.JudgeDirs;
import djudge.exceptions.DJudgeXmlCorruptedException;
import djudge.exceptions.DJudgeXmlException;
import djudge.exceptions.DJudgeXmlNotFoundException;
import djudge.judge.dexecutor.ExecutorFiles;
import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.validator.ValidatorDescription;


public class ProblemDescription extends AbstractDescription 
{
	public final static String XMLRootElement = "problem";
	
	int groupsCount;
	GroupDescription[] groups;
	
	protected String problemRoot;
	
	{
		blockName = "problem";
		problemInfo = new GlobalProblemInfo();
	}
	
	public ProblemDescription(String contestID, String problemID) throws DJudgeXmlException
	{
		this.problemInfo.contestID = contestID;
		this.problemInfo.problemID = problemID;
		loadByProblemContestIDs(contestID, problemID);
	}
	
	private void loadByProblemContestIDs(String contestID, String problemID) throws DJudgeXmlException
	{
		try
		{
			problemRoot = JudgeDirs.getProblemsDir() + contestID + "/" + problemID + "/";
			String problemXML = problemRoot + "problem.xml";
			readXML(XmlWorks.getDocumentE(problemXML).getDocumentElement());
		} catch (SAXParseException e)
		{
			throw new DJudgeXmlCorruptedException();
		}catch (FileNotFoundException e)
		{
			throw new DJudgeXmlNotFoundException();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void parseXML20(Element elem) throws DJudgeXmlException
	{
		NodeList list;
		
		problemInfo.problemRoot = problemRoot;
		
		problemInfo.type = ProblemTypeEnum.IOI;
		
		readCommonXML(elem);
		readOwnXML(elem);

		String in = problemInfo.programInputFilename = elem.getAttribute("input-file");
		String out = problemInfo.programOutputFilename = elem.getAttribute("output-file");
		
		problemInfo.files = new ExecutorFiles(
				in.length() == 0 || in.equalsIgnoreCase("stdin") ? "input.txt" : null,
				out.length() == 0 || out.equalsIgnoreCase("stdout") ? "output.txt" : null
				);
		
        list = elem.getElementsByTagName(GroupDescription.XMLRootElement);
        groupsCount = list.getLength();
        groups = new GroupDescription[groupsCount];
        for (int i = 0; i < groupsCount; i++)
        {   		
        	groups[i] = new GroupDescription(this, i, problemInfo, (Element)list.item(i));
        	if (ownValidator == null)
        	{
        		ownValidator = groups[i].getActualValidator();
        	}
        }
	}
	
	
	
	private void ParseOldXML(Element elem) throws DJudgeXmlException
	{
		problemInfo.problemRoot = problemRoot;
		
		// Only one group
		groupsCount = 1;
		// constant type
		problemInfo.type = ProblemTypeEnum.ACM;
		
		// Test-group
		int testsCount = Integer.parseInt(elem.getAttribute("test-count"));
		String inputFileMask = elem.getAttribute("input-mask");
		String outputFileMask = elem.getAttribute("output-mask");		

		String in = problemInfo.programInputFilename = elem.getAttribute("input-file");
		String out = problemInfo.programOutputFilename = elem.getAttribute("output-file");
		
		problemInfo.files = new ExecutorFiles(
				in.length() == 0 || in.equalsIgnoreCase("stdin") ? "input.txt" : null,
				out.length() == 0 || out.equalsIgnoreCase("stdout") ? "output.txt" : null
				);
		
		// Limits
		ownLimits = new ExecutorLimits();
		ownLimits.outputLimit = StringWorks.StrToMemoryLimit(elem.getAttribute("output-limit"));
		ownLimits.memoryLimit = StringWorks.StrToMemoryLimit(elem.getAttribute("memory-limit"));
		ownLimits.timeLimit = StringWorks.StrToTimeLimit(elem.getAttribute("time-limit"));
		
		// Validator
		String checker = elem.getAttribute("checker");
		String checkerParam = elem.getAttribute("checker-param");
		String checkerExe = elem.getAttribute("check.exe");
		if (checkerExe == null || "".equals(checkerExe))
		{
			checkerExe = "check.exe";
		}
		ownValidator = new ValidatorDescription(problemInfo.contestID,
				problemInfo.problemID, ValidatorDescription.StringToType(checker), checkerParam, checkerExe);
		
		groups = new GroupDescription[1];
		groups[0] = new GroupDescription(this, 0, testsCount, problemInfo, inputFileMask, outputFileMask, elem.getAttribute("score"));
	}
	
	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		problemInfo.appendXml(doc, res);
		
		writeOwnXML(doc, res);
		
		// groups data
		for (int i = 0; i < groupsCount; i++)
		{
			res.appendChild(doc.importNode(groups[i].getXML().getFirstChild(), true));
		}
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem) throws DJudgeXmlException
	{
		try
		{
			String version = elem.getAttribute("version");
			
			if (version.startsWith("2.0"))
			{
				parseXML20(elem);
			}
			else
			{
				ParseOldXML(elem);
			}
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	public int getGroupsCount()
	{
		return groupsCount;
	}
	
	public int getTestsCount()
	{
		int res = 0;
		for (int i = 0; i < groupsCount; i++)
		{
			res += groups[i].getTestCount();
		}
		return res;
	}
	
	public GroupDescription getGroup(int k)
	{
		return groups[k];
	}
	
	public String getJudgeInputFilepath(int groupNumber, int testNumber)
	{
		return problemRoot + "tests/" + groups[groupNumber].tests.get(testNumber).getInputMask();
	}
	
	public String getJudgeOutputFilepath(int groupNumber, int testNumber)
	{
		return problemRoot + "tests/" + groups[groupNumber].tests.get(testNumber).getOutputMask();
	}
	
	public String getName()
	{
		return problemInfo.name;
	}
	
	public ExecutorLimits getTestLimits(int groupNumber, int testNumber)
	{
		return groups[groupNumber].tests.get(testNumber).getActualLimits();
	}
	
	public ExecutorLimits getGroupLimits(int groupNumber)
	{
		return groups[groupNumber].getActualLimits();
	}
	
	public ValidatorDescription getTestValidator(int groupNumber, int testNumber)
	{
		return groups[groupNumber].tests.get(testNumber).getActualValidator();
	}
	
	public ValidatorDescription getGroupValidator(int groupNumber)
	{
		return groups[groupNumber].getActualValidator();
	}
	
	public String getInputMask(int groupNumber)
	{
		return groups[groupNumber].getInputMask();
	}

	public String getOutputMask(int groupNumber)
	{
		return groups[groupNumber].getOutputMask();
	}
	
	public String getInputMask(int groupNumber, int testNumber)
	{
		return groups[groupNumber].tests.get(testNumber).getInputMask();
	}

	public String getOutputMask(int groupNumber, int testNumber)
	{
		return groups[groupNumber].tests.get(testNumber).getOutputMask();
	}
	
	protected String substituteMask(String mask, int groupNumber)
	{
		int cnt = 0;
		char[] c = {0, 0};
		int[] pos = {0, 0};
		for (int k = 0; k < mask.length(); k++)
		{
			if (mask.charAt(k) == '%')
			{
				int t = k + 1;
				while (t < mask.length() && mask.charAt(t) != 'd' && mask.charAt(t) != 'c') t++;
				if (t >= mask.length()) continue;
				c[cnt] = mask.charAt(t);
				pos[cnt] = k;
				cnt++;
			}
		}
		String p1 = new PrintfFormat(mask.substring(0, pos[1])).sprintf(groupNumber + 1);
		String res = p1 + mask.substring(pos[1]);
		return res;
	}
	
	protected String getGroupInputMask(int groupNumber)
	{
		return substituteMask(getInputMask(), groupNumber);
	}
	
	protected String getGroupOutputMask(int groupNumber)
	{
		return substituteMask(getOutputMask(), groupNumber);
	}
	
	public ValidatorDescription getActualValidator()
	{
		return ownValidator;
	}
	
	public ExecutorLimits getActualLimits()
	{
		return ownLimits;
	}

	@Override
	public String getWorkInputMask()
	{
		return getInputMask();
	}

	@Override
	public ExecutorLimits getWorkLimits()
	{
		return getActualLimits();
	}

	@Override
	public String getWorkOutputMask()
	{
		return getOutputMask();
	}

	@Override
	public ValidatorDescription getWorkValidator()
	{
		return getActualValidator();
	}
	
	public boolean save()
	{
		String problemRoot = JudgeDirs.getProblemsDir() + problemInfo.contestID + "/" + problemInfo.problemID + "/";
		String problemXML = problemRoot + "problem.xml";
		saveXML(problemXML);
		return true;
	}
	
	public GlobalProblemInfo getGlobalProblemInfo()
	{
		return problemInfo;
	}
	
}
