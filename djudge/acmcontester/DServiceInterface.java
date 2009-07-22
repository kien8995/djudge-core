package djudge.acmcontester;

import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import db.SubmissionsDataModel;
import djudge.acmcontester.structures.SubmissionData;
import djudge.dservice.DServiceTaskResult;

public class DServiceInterface extends Thread
{
	XmlRpcClient client;
	
	private void updateSubmissionResult(DServiceTaskResult tr)
	{
		SubmissionsDataModel sdm = new SubmissionsDataModel();
		sdm.setWhere("`id` = " + tr.getClientData());
		sdm.fill();
		SubmissionData sd = sdm.getRows().get(0);
		sd.judgement = tr.getJudgement();
		sd.xml = tr.getXml();
		sdm.setRowData(0, sdm.toRow(sd).data);
	}
	
	public void run()
	{
		try
		{
    		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    		config.setServerURL(new URL("http://127.0.0.1:8001/xmlrpc"));
    		config.setEnabledForExtensions(true);
    		config.setConnectionTimeout(5 * 1000);
    		config.setReplyTimeout(5 * 1000);
    
    		client = new XmlRpcClient();
    
    		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
    		
    		client.setConfig(config);
    		
    		System.out.println("DService client started");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		while (true)
		{
			/* Sending submissions to judge */
			SubmissionsDataModel sdm = new SubmissionsDataModel();
			sdm.setWhere("`djudge_flag` = 0");
			sdm.fill();
			Vector<SubmissionData> vsd = sdm.getRows();
			
			for (int i = 0; i < vsd.size(); i++)
			{
				SubmissionData sd = vsd.get(i);
    			Vector <Object> t = new Vector<Object>();
    			t.add("simpleacm");
    			
    			//FIXME
    			t.add("NEERC-1998"); t.add("A");
    			
    			t.add("GCC342");
    			t.add(new String(Base64.decodeBase64(sd.sourceCode.getBytes())));
    			t.add(sd.id);
    			try
    			{
    				System.out.println(t);
    				int result = (Integer) client.execute("DService.submitSolution", t.toArray());
    				if (result > 0)
    				{
    					//FIXME: Hardcode (#15)
    					sdm.setValueAt(-1, i, 15);
    				}
    			}
    			catch (Exception ex)
    			{
    				ex.printStackTrace();
    			}
			}
			
			/* Fetching results from judge */
			
			try
			{
				Object obj = client.execute("DService.fetchResults", new Object[] {"simpleacm"});
				Object[] array = (Object[]) obj;
				if (array != null)
				{
					for (int i = 0; i < array.length; i++)
					{
						@SuppressWarnings("unchecked")
						HashMap<String, String> map = (HashMap<String, String>) array[i];
						DServiceTaskResult tr = new DServiceTaskResult(map);
						updateSubmissionResult(tr);
					}
				}
			}
			catch (XmlRpcException ex)
			{
				System.out.println("Failed connect to DService");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			try
			{
				sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		new DServiceInterface().start();
	}

}
