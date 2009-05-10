package com.alt.djudge.contestmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alt.utils.FileWorks;
import com.alt.utils.XmlWorks;

public class LocalSubmissionDescription
{
	public String sid;
	public String problemSid;
	public String languageSid;
	public Date submissionDate;
	public long submissionTime;
	public String source;
	public String userSid;
	
	public boolean active;
	public boolean checked;
	public String judgement;
	public String xml;
	
	public LocalSubmissionDescription()
	{		
	}
	
	public static String getXmlFilename(ContestSettings contest, String id)
	{
		return "contests/" + contest.id + "/submissions/" + id + ".xml";
	}
	
	public static String getSrcFilename(ContestSettings contest, String id)
	{
		return "contests/" + contest.id + "/submissions/" + id + ".txt";
	}
	
	public LocalSubmissionDescription(ContestSettings contest, Element item)
	{
		sid = item.getAttribute("id"); 
		problemSid = item.getAttribute("problem");
		languageSid = item.getAttribute("language");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try
		{
			submissionDate = df.parse(item.getAttribute("real-time"));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		submissionTime = Long.parseLong(item.getAttribute("contest-time"));
		userSid = item.getAttribute("user");
		active = Integer.parseInt(item.getAttribute("active")) > 0;
		checked = Integer.parseInt(item.getAttribute("checked")) > 0;
		judgement = item.getAttribute("judgement");
		
		xml = FileWorks.readFile(getXmlFilename(contest, sid));
		source = FileWorks.readFile(getSrcFilename(contest, sid));
	}

	public Document getXML()
	{
		Document doc = XmlWorks.getDocument();
		Element elem = doc.createElement("Submission");
		
		elem.setAttribute("id", sid);
		elem.setAttribute("problem", problemSid);
		elem.setAttribute("language", languageSid);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		elem.setAttribute("real-time", df.format(submissionDate));
		
		elem.setAttribute("contest-time", "" + submissionTime);
		
		elem.setAttribute("user", userSid);
		elem.setAttribute("active", active ? "1" : "0");
		elem.setAttribute("checked", checked ? "1" : "0");
		elem.setAttribute("judgement", judgement);
		
		doc.appendChild(elem);
		return doc;
	}
	
}
