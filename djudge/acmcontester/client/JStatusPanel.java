package djudge.acmcontester.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JStatusPanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	public JLabel jlTime;
	public JLabel jlStatus;
	public JButton jbExit;
	
	public JStatusPanel()
	{
		jlTime = new JLabel("Time Left");
		jlStatus = new JLabel("Contest Status");
		jbExit = new JButton("Exit");
		jbExit.addActionListener(this);
		
		add(jlStatus);
		add(jlTime);
		add(jbExit);		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object src = arg0.getSource();
		if (src.equals(jbExit))
		{
			if (JOptionPane.showConfirmDialog(this,
					"Do you really want to exit?", "Confirm",
					JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
			{
				System.exit(0);
			}
		}
	}
}
