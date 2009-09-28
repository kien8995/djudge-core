package x_djudge.contestmanager;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.FileWorks;
import utils.XmlWorks;


public class LocalSubmissionsXmlProvider implements LocalSubmissionsProvider
{
	
	HashMap<String, LocalSubmissionDescription> map = new HashMap<String, LocalSubmissionDescription>();
	
	ContestSettings contest;
	
	private int count;
	
	private String getFilename()
	{
		return "contests/" + contest.id + "/submissions.xml";
	}
	
	public boolean loadData()
	{
		Element elem = XmlWorks.getDocument(getFilename()).getDocumentElement();
		NodeList users = elem.getElementsByTagName("Submission");
		int uc = users.getLength();
		count = uc;
		for (int i = 0; i < uc; i++)
		{
			LocalSubmissionDescription ui = new LocalSubmissionDescription(contest, (Element)users.item(i));
			map.put(ui.sid, ui);
		}		
		return true;
	}
	
	public LocalSubmissionsXmlProvider(ContestSettings contest)
	{
		this.contest = contest;
		loadData();
	}
	
	public boolean saveData()
	{
		Document doc = XmlWorks.getDocument();
		Element res = doc.createElement("Submissions");
		
		LocalSubmissionDescription[] s = map.values().toArray(new LocalSubmissionDescription[0]);
		
		for (int i = 0; i < s.length; i++)
			res.appendChild(doc.importNode(s[i].getXML().getFirstChild(), true));
		
		doc.appendChild(res);
		XmlWorks.saveXmlToFile(doc, getFilename());
		return false;
	}

	@Override
	public String addSubmission(String userSid, String problemSid,
			String languageSid, String source, long contestTime)
	{
		LocalSubmissionDescription desc = new LocalSubmissionDescription();
		desc.active = true;
		desc.checked = false;
		desc.judgement = "N/A";
		desc.languageSid = languageSid;
		desc.problemSid = problemSid;
		desc.sid = "" + ++count;
		desc.userSid = userSid;
		desc.source = source;
		desc.submissionTime = contestTime;
		desc.submissionDate = new Date();
		desc.xml = "";
		map.put(desc.sid, desc);
		FileWorks.saveToFile(source, LocalSubmissionDescription.getSrcFilename(contest, desc.sid));
		return desc.sid;
	}

	@Override
	public LocalSubmissionDescription[] getUserSubmissions(String userSid)
	{
		Vector <LocalSubmissionDescription> res = new Vector<LocalSubmissionDescription>();
		for (LocalSubmissionDescription sbm : map.values())
		{
			if (sbm.userSid.equals(userSid))
				res.add(sbm);
		}
		return res.toArray(new LocalSubmissionDescription[0]);
	}

	@Override
	public boolean setSubmissionResult(String submissionId, String judgement, String xml)
	{
		LocalSubmissionDescription s = map.get(submissionId);
		if (s == null) return false;
		s.checked = true;
		s.judgement = judgement;
		s.xml = xml;
		FileWorks.saveToFile(s.xml, LocalSubmissionDescription.getXmlFilename(contest, s.sid));
		saveData();
		return true;
	}
}
