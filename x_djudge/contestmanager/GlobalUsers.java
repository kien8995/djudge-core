package x_djudge.contestmanager;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlWorks;




public class GlobalUsers
{
	public static HashMap<String, GlobalUserInfo> map = new HashMap<String, GlobalUserInfo>(); 
	
	static
	{
		Element elem = XmlWorks.getDocument("contests/users.xml").getDocumentElement();
		NodeList users = elem.getElementsByTagName("User");
		int uc = users.getLength();
		for (int i = 0; i < uc; i++)
		{
			GlobalUserInfo ui = new GlobalUserInfo((Element)users.item(i));
			map.put(ui.sid, ui);
		}
	}
	
	public static boolean checkUser(String userSid, String password)
	{
		GlobalUserInfo info = map.get(userSid);
		if (info == null) return false;
		return info.password.equals(password);
	}
	
	public static boolean saveData()
	{
		//TODO: implement me
		return true;
	}
}
