package com.alt.djudge.judge.dexecutor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.common.XMLSerializable;

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
	 * 
	 */
	public boolean returnDirectoryContent = true;
	
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
