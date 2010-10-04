/* $Id$ */

package djudge.judge;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.PrintfFormat;
import utils.XmlWorks;

import djudge.judge.checker.CheckerDescription;
import djudge.judge.dexecutor.ExecutorLimits;

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
			String inputFileMask, String outputFileMask, String score)
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
        if (score != null && score.length() > 0)
        {
        	String[] scoreA = score.split(" ");
        	for (int i = 0; i < scoreA.length; i++)
        		tests.get(i).setScore(Integer.parseInt(scoreA[i]));
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
		
		String testsCountStr = elem.getAttribute("tests-count");
		if (null != testsCountStr && testsCountStr.length() > 0)
		{
			int testsCount = Integer.parseInt(testsCountStr);
			tests = new Vector<TestDescription>();
			for (int i = 0; i < testsCount; i++)
			{
				tests.add(new TestDescription(this, i, problemInfo));
			}
		}
		else
		{
    		NodeList list = elem.getElementsByTagName(TestDescription.XMLRootElement);
            int testsCount = list.getLength();
            tests = new Vector<TestDescription>();
            for (int i = 0; i < testsCount; i++)
            {
            	tests.add(new TestDescription(this, i, problemInfo, (Element)list.item(i)));
            }
		}
        String score = elem.getAttribute("tests-score");
        if (score != null && score.length() > 0)
        {
        	String[] scoreA = score.split(" ");
        	for (int i = 0; i < scoreA.length; i++)
        	{
        		tests.get(i).setScore(Integer.parseInt(scoreA[Math.min(i, scoreA.length - 1)]));
        	}
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
	
	public CheckerDescription getActualValidator()
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
	public CheckerDescription getWorkValidator()
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
