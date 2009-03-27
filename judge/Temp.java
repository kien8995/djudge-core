package judge;

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

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.bcel.internal.util.ByteSequence;

import sun.misc.BASE64Decoder;
import utils.FileWorks;
import utils.XmlWorks;

import common.settings;
import common_data_structures.RunnerFiles;

@SuppressWarnings("unused")
public class Temp
{
	public static void checkFile(String inputXML, String outputXML)
	{
		Document doc = XmlWorks.getDocument(inputXML);
		Element elem = (Element)doc.getDocumentElement().getElementsByTagName("submission").item(0);
		
		String contestID = elem.getAttribute("contest-id");
		String problemID = elem.getAttribute("problem-id");
		String languageID = elem.getAttribute("language-id");
		String sourceCode = elem.getAttribute("source-code");
		String trial = elem.getAttribute("trial-testing");
		System.out.println("Trial: '" + trial + "'" + "  " + ((trial.equals("1")) || (trial.equals("true"))));
		
		try
		{
			BASE64Decoder dec = new BASE64Decoder();
			byte[] bytes = dec.decodeBuffer(sourceCode);
			StringBuilder b = new StringBuilder();
			char[] chars = new char[bytes.length];
			for (int i = 0; i < bytes.length; i++)
				chars[i] = (char)bytes[i];
			b.append(chars);
			sourceCode = b.toString();
		}
		catch (Exception ex)
		{
			System.out.println("Exception:" + ex);
		}
		ProblemDescription desc = new ProblemDescription(contestID, problemID);
		String file = settings.getArchiveDir() + "submissions/" + FileWorks.getFileName(inputXML);
		FileWorks.saveToFile(sourceCode, file);
		ProblemDescription problem = new ProblemDescription(contestID, problemID);
		SubmissionResult res = Judge.judgeSourceFile(file, languageID, problem, (trial.equals("1")) || (trial.equals("true")));
		XmlWorks.saveXmlToFile(res.getXML(), outputXML);
	}
	
	public static void checkDirectory(String inputDir, String outputDir)
	{
		String[] file = FileWorks.getDirectoryListing(inputDir);
		for (int i = 0; i < file.length; i++)
		{
			File f = new File(inputDir + file[i]);
			if (!f.isFile()) continue;
			if (file[i].startsWith("_")) continue;
			checkFile(inputDir + file[i], outputDir + file[i]);
			f.delete();	
		}
		
	}
	
	public static void main(String arg[])
	{
		ProblemDescription desc = new ProblemDescription("NEERC-2001", "B");
		
		desc.generateOutput("d:/a.exe");
		
/*		ProblemResult res = Judge.judgeSourceFile("d:/B-alt.cpp", "GCC342", desc, false).getProblemResult();
		
		Document doc = (Document)res.getXML();
		
		XmlWorks.saveXmlToFile(doc, "d:\\1.xml");*/
	}
	
	
	
/*	public static void generateProblemReport(String contestId, String problemId, RunLimits limits)
	{
		ProblemDescription desc = new ProblemDescription(contestId, problemId);
		if (limits.areEmpty())
			desc.setExternalLimits(limits);
			
		JudgeDirectory j = new JudgeDirectory(desc);
		
		DirectoryResult res = j.judge(problemsRoot + contestId + "\\" + problemId +"\\solutions");
		String html = "<h1>Problem " + problemId + " (" + contestId + ") [ " + Calendar.getInstance().getTime() +  "]</h1>"
						+ HtmlWorks.directoryResultToHtml(res);
		FileWorks.saveToFile(html, problemsRoot + contestId + "\\" + problemId +"\\report.html");
	}
	
	public static void generateContestReport(String contestId)
	{
		generateContestReport(contestId, new RunLimits());
	}
	
	public static void generateContestReport(String contestId, RunLimits limits)
	{
		StringBuffer s = new StringBuffer();
		String contestPath = problemsRoot + contestId + "\\";
		s.append("<h1> Contest \"" + contestId + "\" report [ " + Calendar.getInstance().getTime() + "]</h1>");
		for (char c = 'A'; c <= 'Z'; c++)
		{
			if (!(new File(contestPath + c).exists())) continue;
			s.append("<h4><a href='./"+c+"/report.html'>Problem " + c + "<a></h4>");
			String str = "" + c;
			generateProblemReport(contestId, str, limits);
		}
		FileWorks.saveToFile(s.toString(), contestPath + "report.html");
	}*/
	
}
