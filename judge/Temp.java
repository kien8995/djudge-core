package judge;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import utils.FileWorks;
import utils.XmlWorks;

import common_data_structures.RunnerFiles;


@SuppressWarnings("unused")
public class Temp
{
	public static void main(String arg[])
	{
		Judge d = new Judge();
		
		String cmd = "D:\\B.exe";
		ProblemDescription desc = new ProblemDescription("uzhnu-united", "sc2008-AA");
		
		desc.print();
		
		ProblemResult res = Judge.judgeProblem(cmd, desc, true);
		
		Document doc = (Document)res.getXML();
		
		XmlWorks.saveXmlToFile(doc, "d:\\1.xml");
		
		//System.out.println();
	}
}
