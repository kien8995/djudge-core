package x_djudge.contestmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

import utils.XmlWorks;




public class ContestSettings
{
	/*
	 * Contest start time 
	 */
	public Date startTime;
	
	/*
	 * Contest duration in seconds
	 */
	public long duration;
	
	/*
	 * Time when monitor is frozen (in seconds) 
	 */
	public long freezeTime;
	
	/*
	 * Time when monitor is unfrozen (in seconds)  
	 */
	public long unfreezeTime;
	
	/*
	 * Pauses during contests ([i][0] = pause_start, [i][1] = pause_end, times are relative from startTime)
	 */
	public long[][] delays;
	
	/*
	 * Contest duration for single user
	 */
	public long userContestDuration;
	
	/*
	 * 
	 */
	public boolean fVirtual;
	
	/*
	 * 
	 */
	public boolean fOnline;
	
	/*
	 * 
	 */
	public String id;
	
	/*
	 * 
	 */
	public String name;
	
	public ContestSettings(String filename)
	{
		Element elem = XmlWorks.getDocument(filename).getDocumentElement();
		id = elem.getAttribute("id");
		name = elem.getAttribute("name");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
		try
		{
			startTime = df.parse(elem.getAttribute("start-time"));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		duration = Long.parseLong(elem.getAttribute("duration-total")) * 1000;
		freezeTime = Long.parseLong(elem.getAttribute("freeze-time")) * 1000;
		unfreezeTime = Long.parseLong(elem.getAttribute("unfreeze-time")) * 1000;
		delays = new long[0][2];
		userContestDuration = Long.parseLong(elem.getAttribute("duration-user")) * 1000;
		fOnline = Integer.parseInt(elem.getAttribute("online")) > 0;
		fVirtual = Integer.parseInt(elem.getAttribute("virtual")) > 0;
	}
	
	public boolean saveData()
	{
		return true;
	}
}
