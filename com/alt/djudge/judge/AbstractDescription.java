package com.alt.djudge.judge;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alt.djudge.common.XMLSerializable;
import com.alt.djudge.judge.common_data_structures.RunnerFiles;
import com.alt.djudge.judge.common_data_structures.RunnerLimits;
import com.alt.djudge.judge.validator.Validator;
import com.alt.utils.StringWorks;



public abstract class AbstractDescription extends XMLSerializable
{
	public GlobalProblemInfo problemInfo;
		
	int score = 0;
	final String scoreAttributeName = "score";
	
	public abstract void overrideFiles(RunnerFiles newFiles); 
	
	public final RunnerFiles getFiles()
	{
		return problemInfo.files;
	}
	
	public abstract void generateOutput(String command); 
	
	public final void setFiles(RunnerFiles files)
	{
		problemInfo.files = files;
	}
	
	public final RunnerLimits getLimits()
	{
		return problemInfo.limits;
	}
	
	public final void setLimits(RunnerLimits limits)
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

		list = elem.getElementsByTagName(RunnerLimits.XMLRootElement);
        if (list.getLength() > 0)
        	problemInfo.limits = new RunnerLimits((Element)list.item(0));
	}
}
