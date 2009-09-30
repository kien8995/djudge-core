package djudge.judge.dcompiler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlWorks;


import djudge.common.XMLSerializable;
import djudge.judge.dexecutor.ExecutionResult;


public class CompilerResult extends XMLSerializable
{
	public final static String XMLRootElement = "compilation";
	
	public CompiledProgram program;
	
	public CompilationResult result = CompilationResult.Undefined;
	
	public ExecutionResult compilerExecution;
	
	public CompilerResult(Element item)
	{
		readXML(item);
	}

	public CompilerResult()
	{
		// TODO Auto-generated constructor stub
	}

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
