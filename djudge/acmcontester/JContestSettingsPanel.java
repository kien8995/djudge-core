package djudge.acmcontester;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import djudge.acmcontester.admin.AdminClient;
import djudge.acmcontester.server.interfaces.AuthentificationDataProvider;
import djudge.acmcontester.server.interfaces.ServerXmlRpcInterface;

public class JContestSettingsPanel extends JPanel
{

	class JSetTimesPanel extends JPanel implements ActionListener
	{

		private static final long serialVersionUID = 1L;
		
		private JButton btnSetPast, btnSetLeft, btnSetFreeze;
		
		private JTextField tfPassed, tfLeft, tfFreeze;
		
		private ContestTimes data;
		
		public JSetTimesPanel(ContestTimes data)
		{
			this.data = data;
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.insets = new Insets(2, 5, 2, 5);
			
			c.gridx = 1;
			
			btnSetLeft = new JButton("Set time left");
			btnSetLeft.addActionListener(this);
			c.gridy = 0;
			add(btnSetLeft, c);
			
			btnSetPast = new JButton("Set time past");
			btnSetPast.addActionListener(this);
			c.gridy = 1;
			add(btnSetPast, c);
			
			btnSetFreeze = new JButton("Set freeze time");
			btnSetFreeze.addActionListener(this);
			c.gridy = 2;
			add(btnSetFreeze, c);
			
			c.gridx = 0;
			tfLeft = new JTextField("300");
			c.gridy = 0;
			add(tfLeft, c);
			
			tfPassed = new JTextField("0");
			c.gridy = 1;
			add(tfPassed, c);	
			
			tfFreeze = new JTextField("240");
			c.gridy = 2;
			add(tfFreeze, c);
		}

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if (arg0.getSource().equals(btnSetFreeze))
			{
				data.freezeTime = Integer.parseInt(tfFreeze.getText()) * 60 * 1000;
			}
			else if (arg0.getSource().equals(btnSetLeft))
			{
				data.timeLeft = Integer.parseInt(tfLeft.getText()) * 60 * 1000;
			}
			else if (arg0.getSource().equals(btnSetPast))
			{
				data.timePassed = Integer.parseInt(tfPassed.getText()) * 60 * 1000;
			}
		}
	}
	
	class ContestTimes
	{
		long timePassed, timeLeft, freezeTime;
		
		public ContestTimes()
		{
			// TODO Auto-generated constructor stub
		}
		
		public ContestTimes(long passed, long left, long freeze)
		{
			timeLeft = left;
			timePassed = passed;
			freezeTime = freeze;
		}
	}
	
	private static final long serialVersionUID = 1L;
	
	private final JButton jbStartContest;
	
	private final JButton jbStopContest;
	
	private final JButton btnSetTimes;
	
	private final ServerXmlRpcInterface serverInterface;
	
	private final AuthentificationDataProvider authProvider;
	
	public JContestSettingsPanel(ServerXmlRpcInterface serverInterface, AuthentificationDataProvider authProvider)
	{
		this.serverInterface = serverInterface;
		this.authProvider = authProvider;
		jbStartContest = new JButton("Start Contest");
		jbStartContest.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JContestSettingsPanel.this.serverInterface.setContestRunning(JContestSettingsPanel.this.authProvider.getUsername(), JContestSettingsPanel.this.authProvider.getPassword(), true);
			}
		});
		
		jbStopContest = new JButton("Stop Contest");
		jbStopContest.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JContestSettingsPanel.this.serverInterface.setContestRunning(JContestSettingsPanel.this.authProvider.getUsername(), JContestSettingsPanel.this.authProvider.getPassword(), false);
			}
		});
		
		btnSetTimes = new JButton("Set times");
		btnSetTimes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				AuthentificationDataProvider ad = JContestSettingsPanel.this.authProvider;
				String username = ad.getUsername();
				String password = ad.getPassword();
				ServerXmlRpcInterface si = JContestSettingsPanel.this.serverInterface;
				long timePassed = si.getContestTimeElapsed(ad.getUsername(), ad.getPassword());
				long timeLeft = si.getContestTimeLeft(ad.getUsername(), ad.getPassword());
				long freezeTime = si.getContestFreezeTime(ad.getUsername(), ad.getPassword());
				ContestTimes times = new ContestTimes(timePassed, timeLeft, freezeTime);
				if (JOptionPane.showConfirmDialog(null, new JSetTimesPanel(times), "Set times", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
				{
					si.setContestFreezeTime(username, password, times.freezeTime);
					si.setContestTimeLeft(username, password, times.timeLeft);
					si.setContestTimePast(username, password, times.timePassed);
				}
			}
		});
		add(btnSetTimes);
	}

	public static void main(String[] args)
	{
		new AdminClient();
	}
}
