/* $Id$ */

package djudge.judge.checker;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
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
public class CheckerResult extends XMLSerializable
{
	private static final Logger log = Logger.getLogger(CheckerResult.class);
	
	/* XML root element name for this structure (inherited from XMLSerializable) */
	public final static String XMLRootElement = "validator";

	/* Validation result */
	private CheckerResultEnum result = CheckerResultEnum.Undefined;
	private final static String resultAttributeName = "result";

	/* If validation failed, this consists the reason */
	private CheckerFailEnum fail = CheckerFailEnum.Undefined;
	private final static String failAttributeName = "fail";

	/* External validator's exit code */
	private int exitCode = 0;

	/* Validator-generated text */
	private String[] validatorOutput = new String[] {"undefined"};
	private final static String validatorOutputAttributeName = "output";

	/* Runtime information (for external validators) */
	private RunnerResult runInfo = null;

	/* Name of validator */
	private String validatorName = "undefined";
	private final static String validatorNameAttributeName = "type";
	
	/* Detailed result of validation */
	String resultDetails = "";
	private final static String resultDetailsAttributeName = "result-details";

	/* Creates new empty ValidationResult structure */
	public CheckerResult(String name)
	{
		setValidatorName(name);
		setValidatorOutput(new String[0]);
		result = CheckerResultEnum.Undefined;
		setFail(CheckerFailEnum.Undefined);
		setRunInfo(new RunnerResult());
	}

	/* Creates ValidationResult instance from XML element */
	public CheckerResult(Element elem)
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
			Element res = doc.createElement(XMLRootElement);
			res = doc.createElement(XMLRootElement);
			
			res.setAttribute(validatorNameAttributeName, validatorName);
			res.setAttribute(resultAttributeName, getResult().toString());
			res.setAttribute(failAttributeName, getFail().toString());
			res.setAttribute(validatorOutputAttributeName, StringEscapeUtils.escapeXml(StringWorks
					.ArrayToString(getValidatorOutput())));
			res.setAttribute(resultDetailsAttributeName, resultDetails);
			
			doc.appendChild(res);
		}
		catch (Exception ex)
		{
			log.error("getXML error", ex);
		}
		return doc;
	}

	/* XML deserialization */
	@Override
	public boolean readXML(Element elem)
	{
		try
		{
    		validatorName = elem.getAttribute(validatorNameAttributeName);
    		result = CheckerResultEnum.valueOf(elem.getAttribute(resultAttributeName));
    		fail = CheckerFailEnum.valueOf(elem.getAttribute(failAttributeName));
    		validatorOutput = elem.getAttribute(validatorOutputAttributeName).split("\n");
    		resultDetails = elem.getAttribute(resultDetails);
    		return true;
		}
		catch (Exception ex)
		{
			log.error("Error parsing XML", ex);
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

	public CheckerResultEnum getResult()
	{
		return result;
	}

	public void setResult(CheckerResultEnum res)
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

	public void setFail(CheckerFailEnum fail)
	{
		this.fail = fail;
	}

	public CheckerFailEnum getFail()
	{
		return fail;
	}

	public void setExitCode(int exitCode)
	{
		this.exitCode = exitCode;
	}

	public int getExitCode()
	{
		return exitCode;
	}

	public void setRunInfo(RunnerResult runInfo)
	{
		this.runInfo = runInfo;
	}

	public RunnerResult getRunInfo()
	{
		return runInfo;
	}
}
