package djudge.judge.interfaces;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XmlWorks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Scanner;

import djudge.dservice.DServiceTask;
import djudge.judge.CheckParams;
import djudge.judge.Judge;
import djudge.judge.JudgeTaskDescription;
import djudge.judge.JudgeTaskResult;
import djudge.judge.dexecutor.ExecutorLimits;

public class Judge2GoogleAppEngine1Link2 extends Thread
{
	private static final Logger log = Logger
			.getLogger(Judge2GoogleAppEngine1Link2.class);
	
	private String rootUrl = "http://acm-uzh.appspot.com/";

	private String fetchXml()
	{
		String content = null;
		URLConnection connection = null;
		try
		{
			connection = new URL(rootUrl + "djudge?action=get")
					.openConnection();
			//connection.setConnectTimeout(5000);
			//connection.setReadTimeout(5000);
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		// System.out.println(content);
		return content;
	}

	private void postXml(Document xml, String id)
	{
		try
		{
			URL url;
			URLConnection urlConn;
			DataOutputStream printout;
			DataInputStream input;

			// URL of CGI-Bin script.
			url = new URL(rootUrl + "djudge");

			// URL connection channel.
			urlConn = url.openConnection();

			// Let the run-time system (RTS) know that we want input.
			urlConn.setDoInput(true);

			// Let the RTS know that we want to do output.
			urlConn.setDoOutput(true);

			// No caching, we want the real thing.
			urlConn.setUseCaches(false);

			// Specify the content type.
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// Send POST output.
			printout = new DataOutputStream(urlConn.getOutputStream());

			String content = "action=post&id=" + id 
					+ "&xml=" + URLEncoder.encode(XmlWorks.formatDoc(xml));

			printout.writeBytes(content);
			printout.flush();
			printout.close();

			input = new DataInputStream(urlConn.getInputStream());
			while (null != input.readLine());
			input.close();
		}
		catch (MalformedURLException me)
		{
			System.err.println("MalformedURLException: " + me);
		}
		catch (IOException ioe)
		{
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void run()
	{
		while (true)
		{
			String content = fetchXml();
			System.out.println(content);
			if (null != content && content.length() > 100)
			{
				try
				{
					Document doc = XmlWorks.getDocumentFromString(content);
					Element elem = doc.getDocumentElement();
					String id = elem.getAttribute("id");
					String problemId = elem.getAttribute("problem_id");
					String contestId = elem.getAttribute("contest_id");
					String timeLimit = elem.getAttribute("time_limit");
					String memoryLimit = elem.getAttribute("memory_limit");
					String languageId = elem.getAttribute("language_id");
					String firstTestOmly = elem.getAttribute("first_test_only");
					String sc = elem.getAttribute("source_code");
					String sourceCode = StringEscapeUtils.unescapeXml(sc)
							.replace((char) 234, '\n');
					System.out.println(contestId);
					System.out.println(problemId);
					System.out.println(languageId);
					System.out.println(timeLimit);
					System.out.println(memoryLimit);
					System.out.println(firstTestOmly);
					System.out.println(id);
					System.out.println(sourceCode);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", "" + id);
					map.put("contest", contestId);
					map.put("problem", problemId);
					map.put("language", languageId);
					map.put("source", sourceCode);
					map.put("clientData", id);
					DServiceTask task = new DServiceTask(map);
					CheckParams params = new CheckParams();
					params.setFlagFirstTestOnly(Boolean
							.parseBoolean(firstTestOmly));
					Integer tl = Integer.parseInt(timeLimit);
					Integer ml = Integer.parseInt(memoryLimit);
					params.setLimits(new ExecutorLimits(tl, ml));
					JudgeTaskResult res = Judge.judgeTask(
							new JudgeTaskDescription(task), params);
					postXml(res.res.getXML(), id);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			} else
			{
				try
				{
					sleep(5000);
				} catch (InterruptedException e)
				{

				}
			}
		}
	}

	public static void main(String[] args)
	{
		new Judge2GoogleAppEngine1Link2().start();
	}
}
