package djudge.judge.dcompiler;

import org.apache.commons.lang.StringEscapeUtils;
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
	
	public String[] compilerOutput;
	
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
		return compilerOutput;
	}

	public void setCompilerOutput(String[] native_output)
	{
		compilerOutput = native_output;
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
		res.setAttribute("compiler-output", StringEscapeUtils.escapeXml(compilerOutput.length > 0 && compilerOutput[0] != null ? compilerOutput[0] : ""));
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
