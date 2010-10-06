/* $Id$ */

package djudge.judge.executor;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlTools;

import djudge.common.ImplementMe;
import djudge.common.XMLSerializable;
import djudge.judge.dcompiler.DistributedFileset;

public class ExecutionResult extends XMLSerializable
{
	public final static String XMLRootElement = "executor";
	
	public long timeConsumed;
	public final static String timeAttributeName = "time";
	
	public long memoryConsumed;
	public final static String memoryAttributeName = "memory";
	
	public long outputGenerated;
	public final static String outputAttributeName = "output";
	
	public int exitCode;
	public final static String exitCodeAttributeName = "exit-code";
	
	public ExecutionResultEnum result;
	public final static String resultAttributeName = "result";

	//TODO: XMLize this
	public DistributedFileset files;
	
	public String runnerOutput;
	public final static String runnerOutputAttributeName = "executor-output";
	
	public String tempDir;
	public final static String tempDirAttributeName = "temp-dir";
	
	public String resultDetails = "";
	public final static String resultDetailsAttributeName = "result-details";
	
	@ImplementMe public byte[] getFile(String filename)
	{
		//DistributedFile file = files.map.get(filename);
		return new byte[0]; 
	}
	
	public DistributedFileset getFiles()
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
		Document doc = XmlTools.getDocument();
		Element res = doc.createElement(XMLRootElement);

		res.setAttribute(timeAttributeName, "" + timeConsumed);
		res.setAttribute(memoryAttributeName, "" + memoryConsumed);
		res.setAttribute(outputAttributeName, "" + outputGenerated);
		res.setAttribute(exitCodeAttributeName, "" + exitCode);
		res.setAttribute(resultAttributeName, result.toString());
		res.setAttribute(resultDetailsAttributeName, resultDetails);
		res.setAttribute(runnerOutputAttributeName, StringEscapeUtils.escapeXml(runnerOutput));
		res.setAttribute(tempDirAttributeName, StringEscapeUtils.escapeXml(tempDir));
		
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
		timeConsumed = Long.parseLong(elem.getAttribute(timeAttributeName));
		memoryConsumed = Long.parseLong(elem.getAttribute(memoryAttributeName));
		outputGenerated = Long.parseLong(elem.getAttribute(outputAttributeName));
		exitCode = Integer.parseInt(elem.getAttribute(exitCodeAttributeName));
		result = ExecutionResultEnum.valueOf(elem.getAttribute(resultAttributeName));
		runnerOutput = elem.getAttribute(runnerOutputAttributeName);
		tempDir = elem.getAttribute(tempDirAttributeName);
		resultDetails = elem.getAttribute(resultDetailsAttributeName);
		return true;
	}

	public String getResultDetails()
	{
		return resultDetails;
	}
}
