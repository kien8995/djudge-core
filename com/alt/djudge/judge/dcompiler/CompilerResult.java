package com.alt.djudge.judge.dcompiler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.djudge.common.XMLSerializable;
import com.alt.djudge.judge.dexecutor.ExecutionResult;
import com.alt.utils.XmlWorks;


public class CompilerResult extends XMLSerializable
{
	
	public CompiledProgram program;
	
	public CompilationResult result = CompilationResult.Undefined;
	
	public ExecutionResult compilerExecution;
	
	public boolean compilationSuccessfull()
	{
		return result == CompilationResult.OK;
	}

	public String[] getCompilerOutput()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCompilerOutput(String[] native_output)
	{
		// TODO Auto-generated method stub
		
	}

	public boolean isSuccessfull()
	{
		return result == CompilationResult.OK;
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

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
