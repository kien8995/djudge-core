package djudge.acmcontester.client;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import djudge.acmcontester.Admin;
import djudge.acmcontester.ContestCore;

public class JContestSettingsPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	public JButton jbStartContest;
	
	public JButton jbStopContest;
	
	public JTextField jtfTimeLeft;
	
	public JContestSettingsPanel()
	{
		jbStartContest = new JButton("Start Contest");
		jbStartContest.addActionListener(this);
		
		jbStopContest = new JButton("Stop Contest");
		jbStopContest.addActionListener(this);
		
		jtfTimeLeft = new JTextField("05:00:00");
		jtfTimeLeft.setPreferredSize(new Dimension(80, 25));
		
		add(jbStartContest);
		add(jtfTimeLeft);
		add(jbStopContest);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object src = arg0.getSource();
		if (src.equals(jbStartContest))
		{
			String timeLeft = jtfTimeLeft.getText();
			String t[] = timeLeft.split(":");
			long left = ((Long.parseLong(t[0]) * 60 + Long.parseLong(t[1])) * 60 + Long.parseLong(t[2])) * 1000;
			ContestCore.startContest(left);
		}
		else if (src.equals(jbStopContest))
		{
			ContestCore.stopContest();
		}
	}
	
	public static void main(String[] args)
	{
		new Admin();
	}
}
