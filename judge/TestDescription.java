package judge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestDescription extends AbstractDescription
{
	int testNumber = 0;
	String judgeInput;
	String judgeOutput;
	
	{
		testNumber = 0;
		judgeInput = judgeOutput = "";
	}
	
	public TestDescription(int number, GroupDescription group)
	{
		testNumber = number;
		problemInfo = group.problemInfo.clone();
	}
	
	public void setTestNumber(int num)
	{
		this.testNumber = num;
	}
	
	public int getTestNumber()
	{
		return testNumber;
	}
	
	public String getInputFilename()
	{
		return "input.txt";
	}
	
	public String getEtalonFilename()
	{
		return "output.txt";		
	}
	
	@Override
	public Document getXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean readXML(Element elem)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
