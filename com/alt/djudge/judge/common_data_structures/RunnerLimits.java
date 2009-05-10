package com.alt.djudge.judge.common_data_structures;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alt.djudge.common.XMLSerializable;
import com.alt.utils.StringWorks;
import com.alt.utils.XmlWorks;




public class RunnerLimits extends XMLSerializable implements Comparable<RunnerLimits>
{
	public final static String XMLRootElement = "limits"; 
	
	public int timeLimit;
	private final String timeLimitAttributeName = "time-limit";
	
	public int memoryLimit;
	private final String memoryLimitAttributeName = "memory-limit";
	
	public int outputLimit;
	private final String outputLimitAttributeName = "output-limit";
	
	public RunnerSecurityLimits securityLimits;

	private void init(int time, int memory, int output, RunnerSecurityLimits security)
	{
		timeLimit = time;
		memoryLimit = memory;
		outputLimit = output;
		securityLimits = security;
	}
	
	private void initEmpty()
	{
		init(-1, -1, -1, new RunnerSecurityLimits());
	}
	
	public RunnerLimits(Element element)
	{
		readXML(element);
	}
	
	public RunnerLimits(int time, int memory, int output)
	{
		init(time, memory, output, new RunnerSecurityLimits());
	}
	
	public RunnerLimits(int time, int memory)
	{
		init(time, memory, -1, new RunnerSecurityLimits());
	}
	
	public RunnerLimits()
	{
		init(-1, -1, -1, new RunnerSecurityLimits());
	}

	public void setSecurityLimits(RunnerSecurityLimits securityLimits)
	{
		this.securityLimits = securityLimits;
	}
	
	public void setTimeLimit(int timeLimit)
	{
		this.timeLimit = timeLimit;
	}
	
	public void setMemoryLimit(int memoryLimit)
	{
		this.memoryLimit = memoryLimit;
	}
	
	public void setOutputLimit(int outputLimit)
	{
		this.outputLimit = outputLimit;
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(timeLimitAttributeName, "" + timeLimit);
		res.setAttribute(memoryLimitAttributeName, "" + memoryLimit);
		res.setAttribute(outputLimitAttributeName, "" + outputLimit);
		
		if (securityLimits != null)
			res.appendChild(doc.importNode(securityLimits.getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		String tempStr;

		initEmpty();
		
		tempStr = elem.getAttribute(timeLimitAttributeName);
		if (tempStr != "")
			timeLimit = StringWorks.StrToTimeLimit(tempStr);

		tempStr = elem.getAttribute(memoryLimitAttributeName);
		if (tempStr != "")
			memoryLimit = StringWorks.StrToMemoryLimit(tempStr);
		
		tempStr = elem.getAttribute(outputLimitAttributeName);
		if (tempStr != "")
			outputLimit = StringWorks.StrToMemoryLimit(tempStr);
			
		NodeList securityElement = elem.getElementsByTagName(RunnerSecurityLimits.XMLRootElement);
        if (securityElement.getLength() > 0)
        	securityLimits = new RunnerSecurityLimits((Element)securityElement.item(0));
		
		return true;
	}

	@Override
	public int compareTo(RunnerLimits arg)
	{
		int k;
		k = timeLimit - arg.timeLimit;
		if (k != 0) return k;
		k = memoryLimit - arg.memoryLimit;
		if (k != 0) return k;
		k = outputLimit - arg.outputLimit;
		return k;
	}
}
