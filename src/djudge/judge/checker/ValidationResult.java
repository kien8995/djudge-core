/* $Id$ */

package djudge.judge.checker;

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
	private ValidationResultEnum result = ValidationResultEnum.Undefined;

	/* If validation failed, this consists the reason */
	private ValidationFailEnum fail = ValidationFailEnum.Undefined;

	/* External validator's exit code */
	int exitCode = 0;

	/* Validator-generated text */
	private String[] validatorOutput = new String[] {"undefined"};

	/* Runtime information (for external validators) */
	RunnerResult runInfo = null;

	/* Name of validator */
	private String validatorName = "undefined";
	
	/* Detailed result of validation */
	String resultDetails = "";

	/* Creates new empty ValidationResult structure */
	public ValidationResult(String name)
	{
		setValidatorName(name);
		setValidatorOutput(new String[0]);
		result = ValidationResultEnum.Undefined;
		setFail(ValidationFailEnum.Undefined);
		runInfo = new RunnerResult();
	}

	/* Creates ValidationResult instance from XML element */
	public ValidationResult(Element elem)
	{
		super(elem);
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
			res.setAttribute("type", "" + getValidatorName());
			res.setAttribute("result", "" + getResult());
			res.setAttribute("fail", "" + getFail());
			res.setAttribute("output", StringEscapeUtils.escapeXml(StringWorks
					.ArrayToString(getValidatorOutput())));
			res.setAttribute("result-details", resultDetails);
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
		try
		{
    		setValidatorName(elem.getAttribute("type"));
    		result = ValidationResultEnum.valueOf(elem.getAttribute("result"));
    		setFail(ValidationFailEnum.valueOf(elem.getAttribute("fail")));
    		setValidatorOutput(elem.getAttribute("output").split("\n"));
    		resultDetails = elem.getAttribute("result-details");
    		return true;
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return false;
	}

	public void setResultDetails(String details)
	{
		this.resultDetails = details;
	}
	
	public String getResultDetails()
	{
		return resultDetails;
	}

	public ValidationResultEnum getResult()
	{
		return result;
	}

	public void setResult(ValidationResultEnum res)
	{
		this.result = res;
	}

	public void setValidatorOutput(String[] validatorOutput)
	{
		this.validatorOutput = validatorOutput;
	}

	public String[] getValidatorOutput()
	{
		return validatorOutput;
	}

	public void setValidatorName(String validatorName)
	{
		this.validatorName = validatorName;
	}

	public String getValidatorName()
	{
		return validatorName;
	}

	public void setFail(ValidationFailEnum fail)
	{
		this.fail = fail;
	}

	public ValidationFailEnum getFail()
	{
		return fail;
	}
}
