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


//import com.sun.org.apache.bcel.internal.util.ByteSequence;

//import sun.misc.BASE64Decoder;
import sun.font.Script;
import utils.FileWorks;
import utils.Scripts;
import utils.XmlWorks;


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
			System.out.println("Wrong arguments");
		}
	}
}