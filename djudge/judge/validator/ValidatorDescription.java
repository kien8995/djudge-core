package djudge.judge.validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import utils.XmlWorks;

import djudge.common.XMLSerializable;
import djudge.exceptions.DJudgeXmlException;
import djudge.judge.GlobalProblemInfo;

public class ValidatorDescription extends XMLSerializable implements Cloneable
{
	public final static String XMLRootElement = "validator";	
	
	public ValidatorType type;
	public static String typeAttributteName = "type";
	
	private String exeName;
	public static String exeNameAttributteName = "file";
	
	public String param;
	public static String paramAttributteName = "param";
	
	public String problemID;
	public String contestID;
	
	public ValidatorDescription()
	{
		// TODO Auto-generated constructor stub
	}
	
	public ValidatorDescription(String contest, String problem, ValidatorType type, String param, String exeName)
	{
		contestID = contest;
		problemID = problem;
		this.type = type;
		this.param = param;
		this.exeName = exeName;
	}
	
	// v2.0 validator id's
	public static ValidatorType StringToType(String s)
	{
		s = s.toUpperCase();
		ValidatorType res = ValidatorType.Unknown;
		if (s.equalsIgnoreCase("%STR%")) res = ValidatorType.InternalExact;
		else if (s.equalsIgnoreCase("%INT32%")) res = ValidatorType.InternalInt32;
		else if (s.equalsIgnoreCase("%INT64%")) res = ValidatorType.InternalInt64;
		else if (s.equalsIgnoreCase("%FLOAT%")) res = ValidatorType.InternalFloatAbs;
		else if (s.equalsIgnoreCase("%TESTLIB%")) res = ValidatorType.ExternalTestLib;
		else if (s.equalsIgnoreCase("%TESTLIB")) res = ValidatorType.ExternalTestLib;
		else if (s.equalsIgnoreCase("%TESTLIB_JAVA")) res = ValidatorType.ExternalTestLibJava;
		else if (s.equalsIgnoreCase("%PC2%")) res = ValidatorType.ExternalPC2;
		else if (s.equalsIgnoreCase("%RET_VAL%")) res = ValidatorType.ExternalExitCode;
		else if (s.equalsIgnoreCase("%RET_VAL_EXTENDED%")) res = ValidatorType.ExternalExitCodeExtended;
		else if (s.equalsIgnoreCase("@STR")) res = ValidatorType.InternalExact;
		else if (s.equalsIgnoreCase("@TOKEN")) res = ValidatorType.InternalToken;
		else if (s.equalsIgnoreCase("@TOKEN_SORTED")) res = ValidatorType.InternalSortedToken;
		else if (s.equalsIgnoreCase("@INT32")) res = ValidatorType.InternalInt32;
		else if (s.equalsIgnoreCase("@INT64")) res = ValidatorType.InternalInt64;
		else if (s.equalsIgnoreCase("@FLOAT")) res = ValidatorType.InternalFloatAbs;
		else if (s.equalsIgnoreCase("@FLOAT2")) res = ValidatorType.InternalFloatAbsRel;
		else if (s.equalsIgnoreCase("@FLOAT_SKIP")) res = ValidatorType.InternalFloatOther;
		else if (s.equalsIgnoreCase("%STDLIB")) res = ValidatorType.ExternalTestLib;
		else if (s.equalsIgnoreCase("%STDLIB_JAVA")) res = ValidatorType.ExternalTestLibJava;
		else if (s.equalsIgnoreCase("%PC2")) res = ValidatorType.ExternalPC2;
		else if (s.equalsIgnoreCase("%EXITCODE")) res = ValidatorType.ExternalExitCode;
		else if (s.equalsIgnoreCase("%EXITCODE_EXTENDED")) res = ValidatorType.ExternalExitCodeExtended;
		else
		{
			
			for (ValidatorType vtype : ValidatorType.values())
			{
				if (vtype.toString().equalsIgnoreCase(s))
				{
					res = vtype;
				}
			}
		}
		return res;
	}	
	
	public ValidatorDescription(Element elem, GlobalProblemInfo pi)
	{
		contestID = pi.contestID;
		problemID = pi.problemID;
		String SType = elem.getAttribute(typeAttributteName);
		ValidatorType ttype = StringToType(SType);
		String texeFile = elem.getAttribute(exeNameAttributteName);
		String tvalidatorParam = elem.getAttribute(paramAttributteName);
		init(ttype, texeFile, tvalidatorParam);
	}

	private void init(ValidatorType type, String exeFile, String param)
	{
		this.type = type;
		this.param = param;
		this.exeName = exeFile;
	}
	
	public ValidatorDescription(String type, String param)
	{
		this.type = StringToType(type);
		this.param = param;
		this.exeName = "check.exe";
	}
	
	public ValidatorDescription(ValidatorType type)
	{
		this.type = type;
	}	
	
	public int compareTo(ValidatorDescription val)
	{
		if (!type.equals(val.type))
		{
			return type.ordinal() - val.type.ordinal();
		}
		if (type.isExternal())
		{
			return exeName.compareTo(val.exeName);
		}
		if (type.isParametrized())
		{
			return param.compareTo(val.param);
		}
		return 0;
	}
	
	public boolean equals(ValidatorDescription limits)
	{
		return (compareTo(limits) == 0);
	}	
	
	@Override
	public ValidatorDescription clone()
	{
		try
		{
			return (ValidatorDescription) super.clone();
		} catch (CloneNotSupportedException exc)
		{
			System.out.println("Exception occured while cloning class ValidatorDescription: " + exc);
		}
		return this;
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement(XMLRootElement);

		res.setAttribute(typeAttributteName, type.toString());
		
		if (type.isExternal())
		{
			res.setAttribute(exeNameAttributteName, exeName);
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
		return "./problems/" + contestID + "/" + problemID + "/" + exeName;
	}
	
	public void setExeName(String newExe)
	{
		exeName = newExe;
	}
	
	public String getExeName()
	{
		return exeName;
	}
}
