/* $Id$ */

package djudge.utils;

import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import djudge.dservice.DServiceTask;
import djudge.judge.JudgeTaskResult;
import djudge.judge.interfaces.JudgeLinkCallbackInterface;
import djudge.judge.interfaces.JudgeLinkInterface;

import utils.XmlWorks;

class JudgeConnectionInfo
{
	String url;
	String id;
	JudgeLinkInterface thread;
	Menu menu;
	
	boolean fConnected = false;
	
	int submissionsReceived = 0;
	
	int submissionsJudged = 0;
	
	int reportsSent = 0;
	
	Vector<DServiceTask> tasks = new Vector<DServiceTask>();
	
	Vector<JudgeTaskResult> reports = new Vector<JudgeTaskResult>();	
	
	void changeRunningState()
	{
		if (thread.getRunning())
			thread.stopLink();
		else
			thread.startLink();
		menu.setLabel(id + "@" + url + " - " + (thread.getRunning() ? "Working" : "Paused"));
	}
	
	@Override
	public String toString()
	{
		return id + "@" + url;
	}
}

public class JudgeConnector extends JFrame implements JudgeLinkCallbackInterface
{
	private static final long serialVersionUID = 1L;
	
	private final TrayIcon trayIcon;
	
	private final JudgeConnectionInfo[] connections;
	
	private int submissionsReceived = 0;
	
	private int submissionsJudged = 0;
	
	private int reportsSent = 0;
	
	private void updateIconText()
	{
		String txt = "DJudge.JudgeConntector status:\n";
		txt += "Total submissions received: " + submissionsReceived + "\n";
		txt += "Total submissions judged: " + submissionsJudged + "\n";
		txt += "Total reports send: " + reportsSent + "\n";
		trayIcon.setToolTip(txt);
	}
	
	public JudgeConnector(String configFilename) throws Exception
	{
		/* Connections setup */
		Document doc = XmlWorks.getDocument(configFilename);
		Element rootElem = doc.getDocumentElement();
		NodeList nodeList = rootElem.getElementsByTagName("connection");
		connections = new JudgeConnectionInfo[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			connections[i] = new JudgeConnectionInfo();
			connections[i].id = ((Element)nodeList.item(i)).getAttribute("id");
			connections[i].url = ((Element)nodeList.item(i)).getAttribute("url");
			String connectorClassName = ((Element)nodeList.item(i)).getAttribute("connector-class");
			connections[i].thread =  (JudgeLinkInterface) Class.forName(connectorClassName).newInstance();
			connections[i].thread.initLink(new String[]{connections[i].url, connections[i].id}, this, i);
			connections[i].thread.startLink();
		}
		
	    ActionListener exitListener = new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	            System.out.println("Exiting...");
	            System.exit(0);
	        }
	    };
	            
	    PopupMenu popup = new PopupMenu();
	    
	    for (int i = 0; i < connections.length; i++)
	    {
	    	Menu menu = connections[i].menu = new Menu(connections[i].id + "@" + connections[i].url + " - Running");
	    	MenuItem startStop = new MenuItem("Start/Stop");
	    	startStop.setActionCommand("" + i);
	    	ActionListener stopListener = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					Integer k = Integer.parseInt(arg0.getActionCommand());
					connections[k].changeRunningState();
				}
			};
			startStop.addActionListener(stopListener);
	    	menu.add(startStop);
		    popup.add(menu);
	    }
	    
	    MenuItem defaultItem = new MenuItem("Exit");
	    defaultItem.addActionListener(exitListener);
	    popup.add(defaultItem);
	    
	    SystemTray tray = SystemTray.getSystemTray();
	    Image image = Toolkit.getDefaultToolkit().getImage("resources/tray.gif");
	    trayIcon = new TrayIcon(image, "", popup);
	    
	    ActionListener actionListener = new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e) {
	            trayIcon.displayMessage("Action Event", 
	                "An Action Event Has Been Performed!",
	                TrayIcon.MessageType.INFO);
	        }
	    };
	    
	    trayIcon.setImageAutoSize(true);
	    trayIcon.addActionListener(actionListener);
	    
		tray.add(trayIcon);
		
	    updateIconText();
	}
	
	public static void main(String[] args) throws Exception
	{
		String settingFile = "djudge.tools.judgeconnector.xml";
		if (args.length > 0)
			settingFile = args[0];
		JudgeConnector pe = new JudgeConnector(settingFile);
		pe.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void reportConnectionLost(int judgeId, String error)
	{
		if (connections[judgeId].fConnected)
		{
			trayIcon.displayMessage(connections[judgeId].toString(), "Connection lost", MessageType.WARNING);
		}
		connections[judgeId].fConnected = false;
	}

	@Override
	public void reportConnectionRecovered(int judgeId, String error)
	{
		if (!connections[judgeId].fConnected)
		{
			trayIcon.displayMessage(connections[judgeId].toString(), "Connection recovered", MessageType.INFO);
		}
		connections[judgeId].fConnected = true;
	}

	@Override
	public void reportError(int judgeId, String error)
	{
		trayIcon.displayMessage(connections[judgeId].toString(), error, MessageType.ERROR);
	}

	@Override
	public void reportSubmissionReceived(int judgeId, DServiceTask task)
	{
		trayIcon.displayMessage(connections[judgeId].toString(), task.getClientData(), MessageType.INFO);
		connections[judgeId].submissionsReceived++;
		connections[judgeId].tasks.add(task);
		submissionsReceived++;
		updateIconText();
	}

	@Override
	public void reportSubmissionReportSent(int judgeId, JudgeTaskResult res)
	{
		trayIcon.displayMessage(connections[judgeId].toString(), res.res.getJudgement().toString(), MessageType.INFO);
		connections[judgeId].reportsSent++;
		reportsSent++;
		updateIconText();
	}

	@Override
	public void reportSubmissionJudged(int judgeId, JudgeTaskResult res)
	{
		connections[judgeId].submissionsJudged++;
		connections[judgeId].reports.add(res);
		submissionsJudged++;
		updateIconText();
	}	
}
