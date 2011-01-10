/* $Id$ */

package djudge.judge;

import java.io.File;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import djudge.common.JudgeDirs;
import djudge.exceptions.DJudgeXmlException;

import utils.FileTools;
import utils.Scripts;
import utils.XmlTools;

@SuppressWarnings("unused")
public class ProblemsetTester
{	
	
	public static void main(String arg[])
	{
		Locale.setDefault(Locale.ENGLISH);
		if (arg.length == 0)
		arg = new String[] {
				"@SystemTest", "ExternalChecker+LargeOutput"
		};
		if (arg.length == 1)
		{
			Scripts.generateContestReport(arg[0].toString());
		}
		else if (arg.length == 2)
		{
			Scripts.generateProblemReport(arg[0].toString(), arg[1].toString());
		}
		else
		{
			try
			{
				Judge.generateAnswerFiles(arg[2], new ProblemDescription(arg[0], arg[1]));
			}
			catch (DJudgeXmlException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
