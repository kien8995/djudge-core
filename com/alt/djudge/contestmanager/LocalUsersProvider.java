package com.alt.djudge.contestmanager;

import java.util.Date;

public interface LocalUsersProvider
{
	public boolean registerUserOnContest(String userSid);
	
	public boolean isUserRegistered(String userSid);
	
	public Date getStartTime(String userSid);
	
	public boolean saveData();
	
	public boolean startContest(String userSid);
}
