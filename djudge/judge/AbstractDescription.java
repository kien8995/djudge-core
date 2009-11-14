package djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import utils.StringWorks;

import djudge.common.XMLSerializable;
import djudge.judge.dexecutor.ExecutorFiles;
import djudge.judge.dexecutor.ExecutorLimits;
import djudge.judge.validator.Validator;
import djudge.judge.validator.ValidatorDescription;

public abstract class AbstractDescription extends XMLSerializable
{
	protected GlobalProblemInfo problemInfo;
	
	protected ExecutorLimits ownLimits;
	
	protected ValidatorDescription ownValidator;
	
	protected String inputMask;
	final String inputMaskAttributeName = "input-mask";
	
	protected String outputMask;
	final String outputMaskAttributeName = "output-mask";
	
	int score = 0;
	final String scoreAttributeName = "score";
	
	String blockName; 
	
	public String getProblemID()
	{
		return getGlobalProblemInfo().problemID;
	}
	
	public String getContestID()
	{
		return getGlobalProblemInfo().contestID;
	}
	
	public final ExecutorFiles getFiles()
	{
		return problemInfo.files;
	}
	
	public final void setFiles(ExecutorFiles files)
	{
		problemInfo.files = files;
	}
	
	protected final void readCommonXML(Element elem)
	{
		problemInfo.name = elem.getAttribute("name");
	}
	
	protected final void readOwnXML(Element elem)
	{
		NodeList list;
		
		score = StringWorks.parseInt(elem.getAttribute(scoreAttributeName), 0);
		
		inputMask = elem.getAttribute(inputMaskAttributeName);
		
		outputMask = elem.getAttribute(outputMaskAttributeName);
		
		list = elem.getElementsByTagName(Validator.XMLRootElement);
        if (list.getLength() > 0)
        {
        	for (int i = 0; i < list.getLength(); i++)
        	{
            	if (list.item(i).getParentNode().equals(elem))
            	{
            		ownValidator = new ValidatorDescription((Element)list.item(i), problemInfo);
            	}
        	}
        }

		list = elem.getElementsByTagName(ExecutorLimits.XMLRootElement);
        if (list.getLength() > 0)
        {
        	for (int i = 0; i < list.getLength(); i++)
        	{
            	if (list.item(i).getParentNode().equals(elem))
            	{
            		ownLimits = new ExecutorLimits((Element)list.item(i));
            	}
        	}
        }
	}
	
	protected final void writeOwnXML(Document doc, Element res)
	{
		res.setAttribute(scoreAttributeName, "" + score);
		
		if (hasOwnInputMask())
		{
			res.setAttribute(inputMaskAttributeName, inputMask);
		}
		
		if (hasOwnOutputMask())
		{
			res.setAttribute(outputMaskAttributeName, outputMask);
		}
		
		if (hasOwnLimits())
		{
			res.appendChild(doc.importNode(ownLimits.getXML().getFirstChild(), true));
		}
		
        if (hasOwnValidator())
        {
        	res.appendChild(doc.importNode(ownValidator.getXML().getFirstChild(), true));
        }
	}
	
	protected boolean hasOwnLimits()
	{
		return ownLimits != null;
	}
	
	protected boolean hasOwnValidator()
	{
		return ownValidator != null;
	}
	
	protected boolean hasOwnInputMask()
	{
		return inputMask != null && inputMask.length() > 0;
	}
	
	protected boolean hasOwnOutputMask()
	{
		return outputMask != null && outputMask.length() > 0;
	}
	
	public String getInputMask()
	{
		return inputMask;
	}

	public String getOutputMask()
	{
		return outputMask;
	}
	
	public abstract String getWorkOutputMask();

	public abstract String getWorkInputMask();
	
	public abstract ExecutorLimits getWorkLimits();
	
	public abstract ValidatorDescription getWorkValidator();

	public void setValidator(ValidatorDescription vd)
	{
		ownValidator = vd;
	}

	public void setLimits(ExecutorLimits newLimits)
	{
		ownLimits = newLimits;
	}
	
	public GlobalProblemInfo getGlobalProblemInfo()
	{
		return problemInfo;
	}
}
