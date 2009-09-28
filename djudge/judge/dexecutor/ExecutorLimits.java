package djudge.judge.dexecutor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import utils.StringWorks;
import utils.XmlWorks;

import djudge.common.Review;
import djudge.common.XMLSerializable;


public class ExecutorLimits extends XMLSerializable implements Comparable<ExecutorLimits>, Cloneable
{
	public final static String XMLRootElement = "limits";
	
	public int timeLimit;
	private final String timeLimitAttributeName = "time-limit";
	
	public int memoryLimit;
	private final String memoryLimitAttributeName = "memory-limit";
	
	public int outputLimit;
	private final String outputLimitAttributeName = "output-limit";
	
	public ExecutorSecurityLimits securityLimits;

	private void init(int time, int memory, int output, ExecutorSecurityLimits security)
	{
		timeLimit = time;
		memoryLimit = memory;
		outputLimit = output;
		securityLimits = security;
	}
	
	private void initEmpty()
	{
		init(-1, -1, -1, new ExecutorSecurityLimits());
	}
	
	public ExecutorLimits(Element element)
	{
		readXML(element);
	}
	
	public ExecutorLimits(int time, int memory, int output)
	{
		init(time, memory, output, new ExecutorSecurityLimits());
	}
	
	public ExecutorLimits(int time, int memory)
	{
		init(time, memory, -1, new ExecutorSecurityLimits());
	}
	
	public ExecutorLimits(int time)
	{
		init(time, -1, -1, new ExecutorSecurityLimits());
	}
	
	public ExecutorLimits()
	{
		init(-1, -1, -1, new ExecutorSecurityLimits());
	}

	public void setSecurityLimits(ExecutorSecurityLimits securityLimits)
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
	@Review
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		if (isActiveLimit(timeLimit))
		{
			res.setAttribute(timeLimitAttributeName, "" + timeLimit);
		}
		
		if (isActiveLimit(memoryLimit))
		{
			res.setAttribute(memoryLimitAttributeName, "" + memoryLimit);
		}
		
		if (isActiveLimit(outputLimit))
		{
			res.setAttribute(outputLimitAttributeName, "" + outputLimit);
		}
		
		if (securityLimits != null)
		{
			//res.appendChild(doc.importNode(securityLimits.getXML().getFirstChild(), true));
		}
		
		doc.appendChild(res);
		return doc;
	}
	
	@Review
	public static boolean isActiveLimit(long limit)
	{
		return limit > 0;
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
			
		NodeList securityElement = elem.getElementsByTagName(ExecutorSecurityLimits.XMLRootElement);
        if (securityElement.getLength() > 0)
        	securityLimits = new ExecutorSecurityLimits((Element)securityElement.item(0));
		
		return true;
	}

	@Override
	public int compareTo(ExecutorLimits arg)
	{
		int k;
		k = timeLimit - arg.timeLimit;
		if (k != 0) return k;
		k = memoryLimit - arg.memoryLimit;
		if (k != 0) return k;
		k = outputLimit - arg.outputLimit;
		return k;
	}
	
	public boolean equals(ExecutorLimits limits)
	{
		return (compareTo(limits) == 0);
	}

	@Override
	public ExecutorLimits clone()
	{
		try
		{
			return (ExecutorLimits) super.clone();
		}
		catch (CloneNotSupportedException exc)
		{
			System.out.println("Exception occured while cloning class ExecutorLimits: " + exc);
		}
		return this;
	}	
}
