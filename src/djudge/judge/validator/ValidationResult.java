/* $Id$ */

package djudge.judge.validator;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.StringWorks;
import utils.XmlWorks;

import djudge.common.XMLSerializable;
import djudge.judge.executor.RunnerResult;

/*
 * Class that encapsulates information about single test's 
 * validation process
 */
public class ValidationResult extends XMLSerializable
{
	/* XML root element name for this structure (inherited from XMLSerializable) */
	public final static String XMLRootElement = "validator";

	/* Validation result */
	public ValidationResultEnum result;

	/* If validation failed, this consists the reason */
	public ValidationFailEnum fail;

	/* External validator's exit code */
	public int exitCode;

	/* Validator-generated text */
	public String[] validatorOutput;

	/* Runtime information (for external validators) */
	public RunnerResult runInfo;

	/* Name of validator */
	public String validatorName;

	/* Creates new empty ValidationResult structure */
	public ValidationResult(String name)
	{
		validatorName = name;
		validatorOutput = new String[0];
		result = ValidationResultEnum.Undefined;
		fail = ValidationFailEnum.Undefined;
		runInfo = new RunnerResult();
	}

	/* Creates ValidationResult instance from XML element */
	public ValidationResult(Element elem)
	{
		readXML(elem);
	}

	/* XML serialization */
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
			res.setAttribute("output", StringEscapeUtils.escapeXml(StringWorks
					.ArrayToString(validatorOutput)));
			doc.appendChild(res);
		}
		catch (Exception exc)
		{
			System.out.println("!!![ValidationResult.toXml]: " + exc);
		}
		return doc;
	}

	/* XML deserialization */
	@Override
	public boolean readXML(Element elem)
	{
		validatorName = elem.getAttribute("type");
		result = ValidationResultEnum.valueOf(elem.getAttribute("result"));
		fail = ValidationFailEnum.valueOf(elem.getAttribute("fail"));
		validatorOutput = elem.getAttribute("output").split("\n");
		return true;
	}
}
