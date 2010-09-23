/* $Id$ */

package djudge.judge.dcompiler;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import djudge.common.XMLSerializable;

import utils.XmlWorks;

public class CompilationInfo extends XMLSerializable implements Serializable 
{
	public final static String XMLRootElement = "compilation";

	private static final long serialVersionUID = 1L;
	
	CompilationResult state;
	final String stateAttributeName = "state";
	
	String command;

	ArrayList<String> compilerOutput;
	final String compilerOutputAttributeName = "compiler";
	
	int compilerExitCode;
	
	DistributedFileset files;
	
	public CompilationInfo()
	{
		state = CompilationResult.Undefined;
		compilerOutput = new ArrayList<String>();
	}
	
	public void setCompilerOutput(String[] data)
	{
		compilerOutput.clear();
		for (int i = 0; i < data.length; i++)
			compilerOutput.add(data[i]);
	}
	
	public String[] getCompilerOutput()
	{
		return compilerOutput.toArray(new String[0]);
	}
	
	public boolean isSuccessfull()
	{
		return state == CompilationResult.OK;
	}
	
	public String getCommand()
	{
		return command;
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);
		
		res.setAttribute(stateAttributeName, "" + state);
		res.setAttribute(compilerOutputAttributeName, "" + compilerOutput.toString());
		
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
