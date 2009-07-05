package com.alt.djudge.judge;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alt.djudge.common.XMLSerializable;
import com.alt.djudge.judge.dexecutor.ExecutorFiles;
import com.alt.djudge.judge.dexecutor.ExecutorLimits;
import com.alt.djudge.judge.validator.Validator;
import com.alt.utils.StringWorks;



public abstract class AbstractDescription extends XMLSerializable
{
	public GlobalProblemInfo problemInfo;
		
	int score = 0;
	final String scoreAttributeName = "score";
	
	public abstract void overrideFiles(ExecutorFiles newFiles); 
	
	public final ExecutorFiles getFiles()
	{
		return problemInfo.files;
	}
	
	public abstract void generateOutput(String command); 
	
	public final void setFiles(ExecutorFiles files)
	{
		problemInfo.files = files;
	}
	
	public final ExecutorLimits getLimits()
	{
		return problemInfo.limits;
	}
	
	public final void setLimits(ExecutorLimits limits)
	{
		problemInfo.limits = limits;
	}
	
	public final Validator getValidator()
	{
		return problemInfo.validator;
	}
	
	public final void setValidator(Validator validator)
	{
		problemInfo.validator = validator;
	}
	
	protected final void readCommonXML(Element elem)
	{
		NodeList list;
		
		score = StringWorks.parseInt(elem.getAttribute(scoreAttributeName), 0);
		
		list = elem.getElementsByTagName(Validator.XMLRootElement);
        if (list.getLength() > 0)
        	problemInfo.validator = new Validator((Element)list.item(0), 2);

		list = elem.getElementsByTagName(ExecutorLimits.XMLRootElement);
        if (list.getLength() > 0)
        	problemInfo.limits = new ExecutorLimits((Element)list.item(0));
	}
}
