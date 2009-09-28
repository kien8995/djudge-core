package djudge.judge;


import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.PrintfFormat;
import utils.XmlWorks;

import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.validator.ValidatorDescription;

public class GroupDescription extends AbstractDescription
{
	public final static String XMLRootElement = "group";
	
	int groupNumber;
	
	Vector<TestDescription> tests;
	
	ProblemDescription problemDescription;
	
	{
		blockName = "group";
	}
	
	public GroupDescription(ProblemDescription problemDescription, int number, int testsCount, GlobalProblemInfo problemInfo,
			String inputFileMask, String outputFileMask)
	{
		this.problemDescription = problemDescription;
		inputMask = inputFileMask;
		outputMask = outputFileMask;
		this.problemInfo = problemInfo;
		groupNumber = number;
		tests = new Vector<TestDescription>();
		for (int i = 0; i < testsCount; i++)
		{
			tests.add(new TestDescription(this, i, problemInfo));
		}
	}

	public GroupDescription(ProblemDescription problemDescription, int number, GlobalProblemInfo problem, Element item)
	{
		this.problemDescription = problemDescription;
		groupNumber = number;
		problemInfo = problem;
		readXML(item);
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		writeOwnXML(doc, res);
		
		for (int i = 0; i < tests.size(); i++)
		{
			res.appendChild(doc.importNode(tests.get(i).getXML().getFirstChild(), true));
		}
				
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		readOwnXML(elem);
		
		NodeList list = elem.getElementsByTagName(TestDescription.XMLRootElement);
        int testsCount = list.getLength();
        tests = new Vector<TestDescription>();
        for (int i = 0; i < testsCount; i++)
        {
        	tests.add(new TestDescription(this, i, problemInfo, (Element)list.item(i)));
        }
        
		return true;
	}

	public int getTestCount()
	{
		return tests.size();
	}
	
	public String getTestInputMask(int testNumber)
	{
		return substituteMask(getInputMask(), testNumber);
	}

	public String getTestOutputMask(int testNumber)
	{
		return substituteMask(getOutputMask(), testNumber);
	}
	
	private String substituteMask(String mask, int testNumber)
	{
		String res;
		if (mask.contains("%c"))
		{
			res = new PrintfFormat(mask).sprintf(testNumber + 'a');
		}
		else
		{
			res = new PrintfFormat(mask).sprintf(testNumber + 1);
		}
		return res;
	}
	
	public ValidatorDescription getActualValidator()
	{
		if (hasOwnValidator())
		{
			return ownValidator;
		}
		else
		{
			return problemDescription.getActualValidator();
		}
	}
	
	public String getInputMask()
	{
		if (hasOwnInputMask())
		{
			return inputMask;
		}
		else
		{
			return problemDescription.getGroupInputMask(groupNumber);
		}
	}

	public String getOutputMask()
	{
		if (hasOwnOutputMask())
		{
			return outputMask;
		}
		else
		{
			return problemDescription.getGroupOutputMask(groupNumber);
		}
	}
	
	public ExecutorLimits getActualLimits()
	{
		if (hasOwnLimits())
		{
			return ownLimits;
		}
		else
		{
			return problemDescription.getActualLimits();
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
	
	public TestDescription getTest(int k)
	{
		return tests.get(k);
	}

	public int getGroupNumber()
	{
		return groupNumber;
	}
	
	public GlobalProblemInfo getGlobalProblemInfo()
	{
		if (problemInfo != null)
		{
			return problemInfo;
		}
		else
		{
			return problemDescription.getGlobalProblemInfo();
		}
	}	
}
