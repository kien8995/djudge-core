package x_djudge.contestmanager;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlWorks;


public class LocalProblemsXmlProvider implements LocalProblemsProvider
{
	
	HashMap<String, LocalProblemDescription> map = new HashMap<String, LocalProblemDescription>();
	
	ContestSettings contest;
	
	private String getFilename()
	{
		return "contests/" + contest.id + "/problems.xml";
	}
	
	public boolean loadData()
	{
		Element elem = XmlWorks.getDocument(getFilename()).getDocumentElement();
		NodeList users = elem.getElementsByTagName("Problem");
		int uc = users.getLength();
		for (int i = 0; i < uc; i++)
		{
			LocalProblemDescription ui = new LocalProblemDescription((Element)users.item(i));
			map.put(ui.sid, ui);
		}		
		return true;
	}
	
	public LocalProblemsXmlProvider(ContestSettings contest)
	{
		this.contest = contest;
		loadData();
	}
	
	@Override
	public boolean saveData()
	{
		return false;
	}

	@Override
	public boolean checkProblem(String problemSid)
	{
		return map.get(problemSid) != null;
	}

	@Override
	public LocalProblemDescription[] getProblems()
	{
		return map.values().toArray(new LocalProblemDescription[0]);
	}

	@Override
	public LocalProblemDescription getProblem(String problemSid)
	{
		return map.get(problemSid);
	}
	
}
