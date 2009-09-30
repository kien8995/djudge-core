package djudge.acmcontester;

import javax.swing.JFrame;
import javax.swing.JLabel;

import djudge.utils.xmlrpc.XmlRpcStateVisualizer;

public class RpcViziThread extends JFrame  implements XmlRpcStateVisualizer, Runnable
{
	private static final long serialVersionUID = 1L;
	
	private boolean flagVisible = false;
	
	public RpcViziThread()
	{
		setVisible(false);
		setSize(100, 100);
		setLocation(400, 400);
		add(new JLabel("<html><h1>Wait, please...</h1>"));
	}
	
	public void run()
	{
		while (true)
		{
/*			if (flagVisible)
			{
				setVisible(true);
				while (flagVisible)
				{
					try
					{
						Thread.sleep(10);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}*/
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void beforeMethodCall()
	{
	//	System.out.println("Method called");
		//flagVisible = true;
		//setVisible(true);
	}

	@Override
	public void onFailure()
	{
		flagVisible = false;
	}

	@Override
	public void onSuccess()
	{
		//System.out.println("Method returned");
		//flagVisible = false;
		//setVisible(false);
	}
}
