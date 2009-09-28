package djudge.judge;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import djudge.common.Loggable;
import djudge.judge.dexecutor.ExecutorFiles;

public class GlobalProblemInfo extends Loggable implements Cloneable
{
	public String problemID;
	static final String problemIDAttributeName = "id";
	
	public String contestID;
	static final String contestIDAttributeName = "contest";
	
	ExecutorFiles files;
	
	String solutions[];
	
	String name;
	static final String nameAttributeName = "name";
	
	ProblemTypeEnum type;
	static final String typeAttributeName = "type";
	
	public String programInputFilename;
	static final String programInputFilenameAttributeName = "input-file";
	
	public String programOutputFilename;
	static final String programOutputFilenameAttributeName = "output-file";
	
	String problemRoot;
	
	String version;

	public GlobalProblemInfo()
	{
		type = ProblemTypeEnum.ACM;
		problemID = contestID = "unknown";
		files = new ExecutorFiles();
		problemRoot = "";
		solutions = new String[0];
		name = "";
	}
	
	public void appendXml(Document doc, Element res)
	{
		res.setAttribute(problemIDAttributeName, problemID);
		res.setAttribute(contestIDAttributeName, contestID);
		res.setAttribute(nameAttributeName, name);
		res.setAttribute(typeAttributeName, type.toString());
		res.setAttribute("version", "2.0");
		
		if (programInputFilename != null && programInputFilename.length() > 0)
		{
			res.setAttribute(programInputFilenameAttributeName, programInputFilename);
		}
		
		if (programOutputFilename != null && programOutputFilename.length() > 0)
		{
			res.setAttribute(programOutputFilenameAttributeName, programOutputFilename);
		}
	}
	
	public GlobalProblemInfo clone()
	{
		try
		{
			return (GlobalProblemInfo)super.clone();
		}
		catch (CloneNotSupportedException exc)
		{
			System.out.println("Exception occured while cloning class judge.GlobalProblemInfo: " + exc);
		}
		return this;
	}
	
	public String getName()
	{
		return name;
	}
}
