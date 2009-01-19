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
