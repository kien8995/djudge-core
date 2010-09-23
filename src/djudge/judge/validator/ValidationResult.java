/* $Id$ */

package djudge.judge.validator;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import utils.StringWorks;
import utils.XmlWorks;


import djudge.common.XMLSerializable;
import djudge.judge.executor.RunnerResult;




public class ValidationResult extends XMLSerializable 
{
	public final static String XMLRootElement = "validator";
	
	public ValidationResultEnum result;

	public ValidationFailEnum fail;
		
	public int exitCode;
	
	public String[] validatorOutput;
	
	public RunnerResult runInfo;
	
	public String validatorName;
	
	public ValidationResult(String name)
	{
		validatorName = name;
		validatorOutput = new String[0];
		result = ValidationResultEnum.Undefined;
		fail = ValidationFailEnum.Undefined;
		runInfo = new RunnerResult();
	}
	
	public ValidationResult(Element elem)
	{
		readXML(elem);
	}

	@Override
	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		try
		{
			Element res = doc.createElement("val");
			res = doc.createElement(XMLRootElement);
			res.setAttribute("type", "" + validatorName);
			res.setAttribute("result", "" + result);
			res.setAttribute("fail", "" + fail);
			res.setAttribute("output", StringEscapeUtils.escapeXml(StringWorks.ArrayToString(validatorOutput)));
			doc.appendChild(res);
		}
		catch (Exception exc)
		{
			System.out.println("!!![ValidationResult.toXml]: " + exc);
		}
		return doc;
	}

	@Override
	public boolean readXML(Element elem)
	{
		validatorName = elem.getAttribute("type");
		result = ValidationResultEnum.valueOf(elem.getAttribute("result"));
		fail = ValidationFailEnum.valueOf(elem.getAttribute("fail"));
		validatorOutput = elem.getAttribute("output").split("\n");
		System.out.println(validatorName);
		return true;
	}
	
	
}
