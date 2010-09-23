package djudge.judge.dexecutor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import djudge.common.XMLSerializable;



public class ExecutorTask extends XMLSerializable
{
	/*
	 * Program to execute
	 */
	public ExecutorProgram program;
	
	/*
	 * Runtime limits
	 */
	public ExecutorLimits limits;
	
	/*
	 * I/O Redirection
	 */
	public ExecutorFiles files;
	
	/*
	 * Additional parameter
	 */
	public ExecutorParameters params;
	
	/*
	 * 
	 */
	public boolean returnDirectoryContent = true;
	
	public ExecutorTask()
	{
		// TODO Auto-generated constructor stub
	}
	
	public ExecutorTask(ExecutorProgram pr, ExecutorLimits limits, ExecutorFiles files)
	{
		this.program = pr;
		this.files = files;
		this.limits = limits;
	}
	
	public ExecutorTask(ExecutorProgram pr, ExecutorLimits limits, ExecutorFiles files, ExecutorParameters params)
	{
		this.program = pr;
		this.files = files;
		this.limits = limits;
		this.params = params;
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

}
