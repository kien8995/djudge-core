/* $Id$ */

package djudge.swing;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utils.FileTools;

import djudge.acmcontester.admin.AdminClient;

public class JSourceCodePanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPopupMenu popupMenu;
	
	JTextArea code;
	
	private void initPopupMenu()
	{
		popupMenu = new JPopupMenu();
		
		// 'Save to file' item 
		JMenuItem item = new JMenuItem("Save to file...");
		item.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				FileDialog fd = new FileDialog((Dialog) null, "Save to...", FileDialog.SAVE);
				fd.setVisible(true);
				if (fd.getFile() != null)
				{
					String filename = fd.getDirectory() + fd.getFile();
					FileTools.saveToFile(code.getText(), filename);
				}			
			}
		});
		popupMenu.add(item);
	}
	
	public JSourceCodePanel(String src)
	{
		initPopupMenu();
		
		setLayout(new BorderLayout());
		
		code = new JTextArea(src);
		
		code.setEditable(false);
		
		code.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					popupMenu.show(code, e.getX(), e.getY());
				}
			}
		});
		
		code.setFont(new Font("Courier New", Font.PLAIN, 14));
		
		add(new JScrollPane(code), BorderLayout.CENTER);
	}
	
	public static void main(String[] args)
	{
		new AdminClient();
	}

}
