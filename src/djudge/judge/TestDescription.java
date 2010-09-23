/* $Id$ */

package djudge.judge;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import utils.XmlWorks;

import djudge.judge.dexecutor.ExecutorFiles;
import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.validator.ValidatorDescription;

public class TestDescription extends AbstractDescription
{
	public final static String XMLRootElement = "test";
	
	int testNumber;

	GroupDescription groupDecription;
	
	{
		testNumber = 0;
		setScore(1);
		blockName = "test";
	}
	
	public TestDescription(GroupDescription gd, int number, GlobalProblemInfo problemInfo)
	{
		groupDecription = gd;
		testNumber = number;
		this.problemInfo = problemInfo;
	}

	public TestDescription(GroupDescription gd, int number, GlobalProblemInfo problemInfo, Element item)
	{
		groupDecription = gd;
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
		return problemInfo.programInputFilename != "" ? problemInfo.programInputFilename : null;
	}
	
	public String getAnswerFilename()
	{
		return problemInfo.programOutputFilename != "" ? problemInfo.programOutputFilename : null;
	}
	
	public String getEtalonFilename()
	{
		return "output.txt";
	}
	
	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		writeOwnXML(doc, res);
		
		doc.appendChild(res);
		return doc;
	}
	
	@Override
	public boolean readXML(Element elem)
	{
		readOwnXML(elem);
		return true;
	}

	//FIXME
	public ExecutorFiles getExecutorFiles()
	{
		ExecutorFiles res = new ExecutorFiles();
		res.errorFilename = "error.txt";
		String inputTestFilename = getInputFilename();
		if (inputTestFilename == null || inputTestFilename.length() == 0)
			res.inputFilename = "input.txt";
		String outputTestFilename = getAnswerFilename();
		if (outputTestFilename == null || outputTestFilename.length() == 0)
			res.outputFilename = "output.txt";
		return res;
	}
	
	protected ExecutorLimits getActualLimits()
	{
		if (hasOwnLimits())
		{
			return ownLimits;
		}
		else
		{
			return groupDecription.getActualLimits();
		}
	}
	
	protected ValidatorDescription getActualValidator()
	{
		if (hasOwnValidator())
		{
			return ownValidator;
		}
		else
		{
			return groupDecription.getActualValidator();
		}
	}
	
	private String substituteMask(String mask, int testNumber)
	{
		return mask;
	}
	
	public String getInputMask()
	{
		if (hasOwnInputMask())
		{
			return substituteMask(inputMask, testNumber);
		}
		else
		{
			return groupDecription.getTestInputMask(testNumber);
		}
	}

	public String getOutputMask()
	{
		if (hasOwnOutputMask())
		{
			return substituteMask(outputMask, testNumber); 
		}
		else
		{
			return groupDecription.getTestOutputMask(testNumber);
		}
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

	public GlobalProblemInfo getGlobalProblemInfo()
	{
		if (problemInfo != null)
		{
			return problemInfo;
		}
		else
		{
			return groupDecription.getGlobalProblemInfo();
		}
	}	
	
}
