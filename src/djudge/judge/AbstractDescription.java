/* $Id$ */

package djudge.judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import utils.StringTools;

import djudge.common.XMLSerializable;
import djudge.judge.checker.Checker;
import djudge.judge.checker.CheckerDescription;
import djudge.judge.executor.ExecutorFiles;
import djudge.judge.executor.ExecutorLimits;

public abstract class AbstractDescription extends XMLSerializable
{
	protected GlobalProblemInfo problemInfo;
	
	protected ExecutorLimits ownLimits;
	
	protected CheckerDescription ownChecker;
	
	protected String inputMask;
	final String inputMaskAttributeName = "input-mask";
	
	protected String outputMask;
	final String outputMaskAttributeName = "output-mask";
	
	private int score = 0;
	final String scoreAttributeName = "score";
	
	private String comment = "";
	final String commentAttributeName = "comment";
	
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
		
		setScore(StringTools.parseInt(elem.getAttribute(scoreAttributeName), 0));
		
		inputMask = elem.getAttribute(inputMaskAttributeName);
		
		outputMask = elem.getAttribute(outputMaskAttributeName);
		
		comment = elem.getAttribute(commentAttributeName);
		
		list = elem.getElementsByTagName(Checker.XMLRootElement);
        if (list.getLength() > 0)
        {
        	for (int i = 0; i < list.getLength(); i++)
        	{
            	if (list.item(i).getParentNode().equals(elem))
            	{
            		ownChecker = new CheckerDescription((Element)list.item(i), problemInfo);
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
		res.setAttribute(scoreAttributeName, "" + getScore());
		
		if (hasOwnComment())
		{
			res.setAttribute(commentAttributeName, comment);
		}
		
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
        	res.appendChild(doc.importNode(ownChecker.getXML().getFirstChild(), true));
        }
	}
	
	protected boolean hasOwnLimits()
	{
		return ownLimits != null;
	}
	
	protected boolean hasOwnValidator()
	{
		return ownChecker != null;
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
	
	public abstract CheckerDescription getWorkValidator();

	public void setValidator(CheckerDescription vd)
	{
		ownChecker = vd;
	}

	public void setLimits(ExecutorLimits newLimits)
	{
		ownLimits = newLimits;
	}
	
	public GlobalProblemInfo getGlobalProblemInfo()
	{
		return problemInfo;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getScore()
	{
		return score;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getComment()
	{
		return comment;
	}
	
	public boolean hasOwnComment()
	{
		return comment != null && comment.length() > 0;
	}
}
