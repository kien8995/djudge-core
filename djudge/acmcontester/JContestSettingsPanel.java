package djudge.acmcontester;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
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
		
		private JLabel lblState;
		
		private final Dimension szButton = new Dimension(120, 25);
		
		private final Dimension szEdit = new Dimension(80, 25);
		
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
			btnSetLeft.setPreferredSize(szButton);
			c.gridy = 0;
			add(btnSetLeft, c);
			
			btnSetPast = new JButton("Set time past");
			btnSetPast.addActionListener(this);
			btnSetPast.setPreferredSize(szButton);
			c.gridy = 1;
			add(btnSetPast, c);
			
			/*btnSetFreeze = new JButton("Set freeze time");
			btnSetFreeze.addActionListener(this);
			btnSetFreeze.setPreferredSize(szButton);
			c.gridy = 2;
			add(btnSetFreeze, c);*/
			
			c.gridx = 0;
			tfLeft = new JTextField("300");
			tfLeft.setPreferredSize(szEdit);
			c.gridy = 0;
			add(tfLeft, c);
			
			tfPassed = new JTextField("0");
			tfPassed.setPreferredSize(szEdit);
			c.gridy = 1;
			add(tfPassed, c);	
			
			/*tfFreeze = new JTextField("240");
			tfFreeze.setPreferredSize(szEdit);
			c.gridy = 2;
			add(tfFreeze, c);*/
			
			c.gridy = 2;
			lblState = new JLabel("Time: " + data.timePassed / 60 / 1000 + "     Left: " + data.timeLeft / 60 / 1000);
			lblState.setPreferredSize(szButton);
			add(lblState, c);
		}

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if (arg0.getSource().equals(btnSetFreeze))
			{
				//data.freezeTime = Integer.parseInt(tfFreeze.getText()) * 60 * 1000;
			}
			else if (arg0.getSource().equals(btnSetLeft))
			{
				data.timeLeft = Integer.parseInt(tfLeft.getText()) * 60 * 1000;
				lblState.setText("Time: " + data.timePassed / 60 / 1000 + "     Left: " + data.timeLeft / 60 / 1000);
			}
			else if (arg0.getSource().equals(btnSetPast))
			{
				data.timePassed = Integer.parseInt(tfPassed.getText()) * 60 * 1000;
				lblState.setText("Time: " + data.timePassed / 60 / 1000 + "     Left: " + data.timeLeft / 60 / 1000);
			}
		}
	}
	
	class ContestTimes
	{
		long timePassed, timeLeft/*, freezeTime*/;
		
		public ContestTimes()
		{
			// TODO Auto-generated constructor stub
		}
		
		public ContestTimes(long passed, long left/*, long freeze*/)
		{
			timeLeft = left;
			timePassed = passed;
		//	freezeTime = freeze;
		}
	}
	
	private static final long serialVersionUID = 1L;
	
	private final JButton jbStartContest;
	
	private final JButton jbStopContest;
	
	private final JButton btnSetTimes;
	
	private final JButton btnClearSubmissions = new JButton("Delete all submissions");
	
	private final JButton btnClearProblems = new JButton("Delete all problems");
	
	private final JButton btnClearUsers = new JButton("Delte all users");
	
	private final JButton btnClearLanguages = new JButton("Delte all languages");
	
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
		add(jbStartContest);
		
		jbStopContest = new JButton("Stop Contest");
		jbStopContest.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JContestSettingsPanel.this.serverInterface.setContestRunning(JContestSettingsPanel.this.authProvider.getUsername(), JContestSettingsPanel.this.authProvider.getPassword(), false);
			}
		});
		add(jbStopContest);
		
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
				//long freezeTime = si.getContestFreezeTime(ad.getUsername(), ad.getPassword());
				ContestTimes times = new ContestTimes(timePassed, timeLeft);
				if (JOptionPane.showConfirmDialog(null, new JSetTimesPanel(times), "Set times", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
				{
					//si.setContestFreezeTime(username, password, times.freezeTime);
					si.setContestTimeLeft(username, password, times.timeLeft);
					si.setContestTimePast(username, password, times.timePassed);
				}
			}
		});
		add(btnSetTimes);
		
		btnClearSubmissions.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JContestSettingsPanel.this.serverInterface
						.deleteAllSubmissions(
								JContestSettingsPanel.this.authProvider
										.getUsername(),
								JContestSettingsPanel.this.authProvider
										.getPassword());
			}
			
		});
		add(btnClearSubmissions);
		
		btnClearLanguages.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JContestSettingsPanel.this.serverInterface
						.deleteAllLanguages(
								JContestSettingsPanel.this.authProvider
										.getUsername(),
								JContestSettingsPanel.this.authProvider
										.getPassword());
			}
			
		});
		add(btnClearLanguages);
		
		btnClearProblems.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JContestSettingsPanel.this.serverInterface
						.deleteAllProblems(
								JContestSettingsPanel.this.authProvider
										.getUsername(),
								JContestSettingsPanel.this.authProvider
										.getPassword());
			}
			
		});
		add(btnClearProblems);

		btnClearUsers.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JContestSettingsPanel.this.serverInterface
						.deleteAllUsers(
								JContestSettingsPanel.this.authProvider
										.getUsername(),
								JContestSettingsPanel.this.authProvider
										.getPassword());
			}
			
		});
		add(btnClearUsers);
	}

	public static void main(String[] args)
	{
		new AdminClient();
	}
}
