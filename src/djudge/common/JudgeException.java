/* $Id$ */

package djudge.common;

public class JudgeException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	String majorMessage;
	
	String minorMessage;
	
	JudgeExceptionType type;
	
	private void init()
	{
		majorMessage = minorMessage = "";
		type = JudgeExceptionType.General;
	}
	
	public JudgeException()
	{
		init();
	}
	
	public JudgeException(JudgeExceptionType type)
	{
		init();
		this.type = type;
	}
	
	public JudgeException(JudgeExceptionType type, String message)
	{
		init();
		this.type = type;
		this.majorMessage = message;
	}	

	public JudgeException(String message, String message2)
	{
		init();
		this.majorMessage = message;
		this.minorMessage = message2;
	}	

	public JudgeException(String message)
	{
		init();
		this.majorMessage = message;
	}	

	public JudgeException(JudgeExceptionType type, String message, String message2)
	{
		init();
		this.type = type;
		this.majorMessage = message;
		this.minorMessage = message2;
	}	
	
	public String toString()
	{
		return type + ": '" + majorMessage + "' [" + minorMessage + "]";
	}
}
