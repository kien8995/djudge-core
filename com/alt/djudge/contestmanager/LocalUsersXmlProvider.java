package com.alt.djudge.contestmanager;

import java.util.Date;
import java.util.HashMap;

class LocalUserData
{
	String userSid;
	
	boolean fEntered;
	
	Date startDate;
}

public class LocalUsersXmlProvider implements LocalUsersProvider
{
	
	HashMap<String, LocalUserData> map = new HashMap<String, LocalUserData>();
	
	ContestSettings contest;
	
	public boolean loadData()
	{
		return true;
	}
	
	public LocalUsersXmlProvider(ContestSettings contest)
	{
		this.contest = contest;
		loadData();
	}
	
	public boolean startContest(String userSid)
	{
		LocalUserData data = map.get(userSid);
		if (data == null)
			return false;
		if (data.fEntered) return false;
		data.fEntered = true;
		data.startDate = new Date();
		return true;
	}
	
	@Override
	public Date getStartTime(String userSid)
	{
		LocalUserData data = map.get(userSid);
		if (data == null)
			return null;
		if (!data.fEntered) return null;
		return data.startDate;
	}

	@Override
	public boolean isUserRegistered(String userSid)
	{
		return map.get(userSid) != null;
	}

	@Override
	public boolean registerUserOnContest(String userSid)
	{
		if (isUserRegistered(userSid)) return true;
		LocalUserData data = new LocalUserData();
		data.fEntered = false;
		data.userSid = userSid;
		map.put(userSid, data);
		return true;
	}

	@Override
	public boolean saveData()
	{
		return false;
	}
	
}
