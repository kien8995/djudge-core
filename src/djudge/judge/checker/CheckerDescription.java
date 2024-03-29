/* $Id$ */

package djudge.judge.checker;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlTools;

import djudge.common.Deployment;
import djudge.common.JudgeDirs;
import djudge.common.XMLSerializable;
import djudge.exceptions.DJudgeXmlException;
import djudge.judge.GlobalProblemInfo;

public class CheckerDescription extends XMLSerializable implements Cloneable
{
	private final static Logger log = Logger.getLogger(CheckerDescription.class);
	
	public final static String XMLRootElement = "checker";

	public CheckerTypeEnum type;
	public static String typeAttributteName = "type";

	private String executableFilename;
	public static String exeNameAttributteName = "file";

	public String param;
	public static String paramAttributteName = "param";

	public String problemID;
	public String contestID;

	public CheckerDescription()
	{
		// TODO Auto-generated constructor stub
	}

	public CheckerDescription(String contest, String problem,
			CheckerTypeEnum type, String param, String exeName)
	{
/*		System.out.println("==================================");
		System.out.println(contest + " " + problem + " " + type + " " + param + " " + exeName);
		System.out.println("==================================");*/
		contestID = contest;
		problemID = problem;
		this.type = type;
		this.param = param;
		this.executableFilename = exeName;
	}

	// v2.0 checker id's
	public static CheckerTypeEnum StringToType(String s)
	{
		s = s.toUpperCase();
		CheckerTypeEnum res = CheckerTypeEnum.Unknown;
		if (s.equalsIgnoreCase("%STR%"))
			res = CheckerTypeEnum.InternalExact;
		else if (s.equalsIgnoreCase("%INT%"))
			res = CheckerTypeEnum.InternalInt32;
		else if (s.equalsIgnoreCase("%INT32%"))
			res = CheckerTypeEnum.InternalInt32;
		else if (s.equalsIgnoreCase("%INT64%"))
			res = CheckerTypeEnum.InternalInt64;
		else if (s.equalsIgnoreCase("%FLOAT%"))
			res = CheckerTypeEnum.InternalFloatAbs;
		else if (s.equalsIgnoreCase("%TESTLIB%"))
			res = CheckerTypeEnum.ExternalTestLib;
		else if (s.equalsIgnoreCase("%TESTLIB"))
			res = CheckerTypeEnum.ExternalTestLib;
		else if (s.equalsIgnoreCase("%TESTLIB_JAVA"))
			res = CheckerTypeEnum.ExternalTestLibJava;
		else if (s.equalsIgnoreCase("%PC2%"))
			res = CheckerTypeEnum.ExternalPC2;
		else if (s.equalsIgnoreCase("%RET_VAL%"))
			res = CheckerTypeEnum.ExternalExitCode;
		else if (s.equalsIgnoreCase("%RET_VAL_EXTENDED%"))
			res = CheckerTypeEnum.ExternalExitCodeExtended;
		else if (s.equalsIgnoreCase("@STR"))
			res = CheckerTypeEnum.InternalExact;
		else if (s.equalsIgnoreCase("@TOKEN"))
			res = CheckerTypeEnum.InternalToken;
		else if (s.equalsIgnoreCase("@TOKEN_SORTED"))
			res = CheckerTypeEnum.InternalSortedToken;
		else if (s.equalsIgnoreCase("@STR_SORTED"))
			res = CheckerTypeEnum.InternalSortedExact;
		else if (s.equalsIgnoreCase("@INT"))
			res = CheckerTypeEnum.InternalInt32;
		else if (s.equalsIgnoreCase("@INT32"))
			res = CheckerTypeEnum.InternalInt32;
		else if (s.equalsIgnoreCase("@INT64"))
			res = CheckerTypeEnum.InternalInt64;
		else if (s.equalsIgnoreCase("@FLOAT"))
			res = CheckerTypeEnum.InternalFloatAbs;
		else if (s.equalsIgnoreCase("@FLOAT2"))
			res = CheckerTypeEnum.InternalFloatAbsRel;
		else if (s.equalsIgnoreCase("@FLOAT_SKIP"))
			res = CheckerTypeEnum.InternalFloatOther;
		else if (s.equalsIgnoreCase("%STDLIB"))
			res = CheckerTypeEnum.ExternalTestLib;
		else if (s.equalsIgnoreCase("%STDLIB_JAVA"))
			res = CheckerTypeEnum.ExternalTestLibJava;
		else if (s.equalsIgnoreCase("%PC2"))
			res = CheckerTypeEnum.ExternalPC2;
		else if (s.equalsIgnoreCase("%EXITCODE"))
			res = CheckerTypeEnum.ExternalExitCode;
		else if (s.equalsIgnoreCase("%EXITCODE_EXTENDED"))
			res = CheckerTypeEnum.ExternalExitCodeExtended;
		else
		{

			for (CheckerTypeEnum checkerType : CheckerTypeEnum.values())
			{
				if (checkerType.toString().equalsIgnoreCase(s))
				{
					res = checkerType;
				}
			}
		}
		return res;
	}

	public CheckerDescription(Element elem, GlobalProblemInfo pi)
	{
		contestID = pi.contestID;
		problemID = pi.problemID;
		String chType = elem.getAttribute(typeAttributteName);
		CheckerTypeEnum ttype = StringToType(chType);
		String texeFile = elem.getAttribute(exeNameAttributteName);
		String tcheckerParam = elem.getAttribute(paramAttributteName);
		init(ttype, texeFile, tcheckerParam);
	}

	private void init(CheckerTypeEnum type, String exeFile, String param)
	{
		this.type = type;
		this.param = param;
		this.executableFilename = exeFile;
		if (null != param)
		{
			if (param.endsWith(".jar"))
			{
				this.executableFilename = "check.jar";
			}
		}
	}

	public CheckerDescription(String type, String param)
	{
		this.type = StringToType(type);
		this.param = param;
		this.executableFilename = Deployment.isOSWinNT() ? "check.exe" : "check.o";
		if (null != param)
		{
			if (param.endsWith(".jar"))
			{
				this.executableFilename = "check.jar";
			}
		}
	}

	public CheckerDescription(CheckerTypeEnum type)
	{
		this.type = type;
	}

	public int compareTo(CheckerDescription checker)
	{
		if (!type.equals(checker.type))
		{
			return type.ordinal() - checker.type.ordinal();
		}
		if (type.isExternal())
		{
			return executableFilename.compareTo(checker.executableFilename);
		}
		if (type.isParametrized())
		{
			return param.compareTo(checker.param);
		}
		return 0;
	}

	public boolean equals(CheckerDescription limits)
	{
		return (compareTo(limits) == 0);
	}

	@Override
	public CheckerDescription clone()
	{
		try
		{
			return (CheckerDescription) super.clone();
		}
		catch (CloneNotSupportedException exc)
		{
			log.error("Exception occured while cloning class ValidatorDescription", exc);
		}
		return this;
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlTools.getDocument();
		Element res = doc.createElement(XMLRootElement);

		res.setAttribute(typeAttributteName, type.toString());

		if (type.isExternal())
		{
			res.setAttribute(exeNameAttributteName, executableFilename);
		}

		if (type.isParametrized())
		{
			res.setAttribute(paramAttributteName, param);
		}

		doc.appendChild(res);
		return doc;
	}

	@Override
	public boolean readXML(Element elem) throws DJudgeXmlException
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String getCheckerPath()
	{
		return JudgeDirs.getProblemsDir() + contestID + "/" + problemID + "/"
				+ executableFilename;
	}

	public void setExeName(String newExe)
	{
		executableFilename = newExe;
	}

	public String getExeName()
	{
		return executableFilename;
	}
}
