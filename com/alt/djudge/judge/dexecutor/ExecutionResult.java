package com.alt.djudge.judge.dexecutor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.common.XMLSerializable;
import com.alt.djudge.judge.dcompiler.DFile;
import com.alt.djudge.judge.dcompiler.DFilePack;
import com.alt.utils.XmlWorks;

public class ExecutionResult extends XMLSerializable
{
	public String stdOutputContent = "";
	
	public String stdErrorContent = "";
	
	public long timeConsumed;
	
	public long memoryConsumed;
	
	public long outputGenerated;
	
	public int exitCode;
	
	public ExecutionResultEnum result;
	
	public DFilePack files;
	
	public String runnerOutput;
	
	public byte[] getFile(String filename)
	{
		DFile file = files.map.get(filename); 
		return file != null ? file.content : new byte[0]; 
	}
	
	public DFilePack getFiles()
	{
		return files;
	}
	
	public int getExitCode()
	{
		return exitCode;
	}
	
	public ExecutionResultEnum getResult()
	{
		return result;
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		// TODO: fill me
		doc.appendChild(res);
		return doc;
	}
	
	public ExecutionResult(Element elem)
	{
		readXML(elem);
	}

	public ExecutionResult()
	{
		
	}

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
