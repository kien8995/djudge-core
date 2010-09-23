/* $Id$ */

package djudge.acmcontester;

import java.util.Date;

public class ConnectionState
{
	
	public Date lastSuccessTime = new Date(0);
	
	public Date lastConnectionTime = new Date(0);
	
	public boolean fConnected = false;
	
}
