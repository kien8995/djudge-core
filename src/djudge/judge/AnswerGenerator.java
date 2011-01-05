/* $Id$ */

package djudge.judge;

import java.io.File;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import sun.font.Script;
import utils.FileTools;
import utils.Scripts;
import utils.XmlTools;

@SuppressWarnings("unused")
public class AnswerGenerator
{

	public static void main(String arg[])
	{
		if (arg.length == 3)
		{
			try
			{
				Judge.generateAnswerFiles(arg[2], new ProblemDescription(arg[0], arg[1]));
			} catch (DJudgeXmlException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
//				Judge.generateAnswerFiles("/home/alt//work/java/djudge/problems/NEERC-2003-Northern/K/solutions/k_as.dpr", new ProblemDescription("NEERC-2003-Northern", "K"));
				Judge.generateAnswerFiles("/home/alt/work/java/djudge/problems/NEERC-2003-Northern/D/solutions/d_al.dpr", new ProblemDescription("NEERC-2003-Northern", "D"));
			}
			catch (DJudgeXmlException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			System.out.println("Wrong arguments");
		}
	}
}
