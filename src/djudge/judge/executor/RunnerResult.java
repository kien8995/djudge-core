package djudge.judge.executor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import djudge.common.XMLSerializable;


import utils.XmlWorks;






public class RunnerResult extends XMLSerializable implements Comparable<RunnerResult>
{
	public static final String XMLRootElement = "runner"; 
	
	public int time;
	final String timeAttributeName = "time";
	
	public int memory;
	final String memoryAttributeName = "memory";
	
	public int output;
	final String outputAttributeName = "output";
	
	public int exitCode;
	final String exitCodeAttributeName = "exit-code";
	
	public RunnerResultEnum state;
	
	// initialization
	{
		time = memory = output = exitCode = -1;
		state = RunnerResultEnum.Undefined;
	}
	
	public RunnerResult()
	{
		// nothing
	}
	
	public RunnerResult(Element elem)
	{
		readXML(elem);
	}

	public void OK(int retValue, int time, int mem, int output)
	{
		exitCode = retValue;
		this.time = time;
		this.memory = mem;
		this.output = output;		
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(timeAttributeName, "" + time);
		res.setAttribute(memoryAttributeName, "" + memory);
		res.setAttribute(outputAttributeName, "" + output);
		res.setAttribute(exitCodeAttributeName, "" + exitCode);
		
		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int compareTo(RunnerResult other)
	{
		return state.ordinal() - other.state.ordinal();
	}
}
