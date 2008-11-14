// TODO: review this class (old version used)

package runner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import common.XMLSerializable;

public class RunnerResult extends XMLSerializable implements Comparable<RunnerResult>
{
	public static final String XMLRootElement = "RunnerResult"; 
	
	public int time;
	public int memory;
	public int output;
	public int exitCode;
	
	public RunnerResultEnum state;

	private void init(int exitCode, int time, int memory, int output, RunnerResultEnum state)
	{
		this.exitCode = exitCode;
		this.time = time;
		this.memory = memory;
		this.output = output;
		this.state = state;
	}
	
	private void initEmpty()
	{
		init(-1, -1, -1, -1, RunnerResultEnum.Undefined);
	}
	
	public RunnerResult()
	{
		initEmpty();
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
		// TODO Auto-generated method stub
		return null;
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

/*
package runner;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlWorks;

public class RunResult implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int exitCode;
	public int memorySize;
	public int timeConsumed;
	public int outputSize;
	
	public RunResultEnum state = RunResultEnum.Undefined;

	public RunResult()
	{
		exitCode = memorySize = timeConsumed = outputSize = -1;
		state = RunResultEnum.Undefined;
	}
	
	public void OK(int retVal, int time, int mem, int output)
	{
		exitCode = retVal;
		memorySize = mem;
		timeConsumed = time;
		outputSize = output;
	}
	
	public Document getXml()
	{
		Document doc = XmlWorks.getDocument();
		try
		{
			Element res = doc.createElement("RunResult");
			res.setAttribute("result", "" + state);
			res.setAttribute("time-used", "" + timeConsumed);
			res.setAttribute("memory-size", "" + memorySize);
			res.setAttribute("output-size", "" + outputSize);
			res.setAttribute("exit-code", "" + exitCode);
			doc.appendChild(res);
		}
		catch (Exception exc)
		{
			// FIXME
			System.out.println("!!![RunResult.toXml]: " + exc);
		}
		return doc;
	}
	
	public void saveXml(String file)
	{
		XmlWorks.saveXmlToFile(getXml(), file);
	}
	
}
*/