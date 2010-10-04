/* $Id$ */

package djudge.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import djudge.judge.CheckParams;
import djudge.judge.Judge;
import djudge.judge.ProblemDescription;
import djudge.judge.SubmissionResult;
import djudge.judge.TestResult;
import djudge.judge.dcompiler.CompilationResult;

public class SolutionTester extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JTable table;

	private ProblemDescription pd;
	
	private JScrollPane spTable;
	
	class ColorRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			setText(value.toString());
			String text = value.toString();
			if ("AC".equalsIgnoreCase(text) || "OK".equalsIgnoreCase(text))
			{
				setBackground(Color.green);
			}
			else
			{
				setBackground(Color.red);
			}
			return this;
		}
	}
	
	public void setTable()
	{
		if (spTable != null)
		{
			spTable.removeAll();
			remove(spTable);
		}
		table = new JTable(data, columnNames);
		table.setAutoCreateRowSorter(true);
		table.setRowHeight(20);
		TableColumnModel cm = table.getColumnModel();
		cm.getColumn(table.getColumnCount()-2).setCellRenderer(new ColorRenderer());
		add(spTable = new JScrollPane(table), BorderLayout.CENTER);
		setVisible(true);
	}
	
	public void setupGUI()
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(50, 50));
		JButton btn = new JButton("Do It!!!");
		btn.setActionCommand("DOIT");
		btn.addActionListener(this);
		panel.add(btn);
		add(panel, BorderLayout.NORTH);
	}
	
	public SolutionTester(ProblemDescription pd)
	{
		setLayout(new BorderLayout());
		this.pd = pd;
		setupGUI();
		setSize(640, 480);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		try
		{
			SolutionTester wnd = new SolutionTester(new ProblemDescription("WS-2009", "3G"));
			wnd.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		catch (Exception e)
		{
			
		}
	}
	
	Object[][] data;
	
	String[] columnNames = {
			"t.g. number",
			"1 score",
			"2 time",
			"3 memory",
			"4 output",
			"5 runtime",
			"6 validation",
			"7 validator_output",
			"8 summary",
			"9 val"
			
	};
	
	private void makeTable(SubmissionResult sRes)
	{
		// 0 - t.g. number
		// 1 score
		// 2 time
		// 3 memory
		// 4 output
		// 5 runtime
		// 6 validation
		// 7 validator_output
		// 8 summary
		if (sRes.getCompilationInfo().result != CompilationResult.OK)
		{
			data = new Object[0][0];
			return;
		}
		int tests = pd.getTestsCount();
		data = new Object[tests][10];
		int k = 0;
		for (int i = 0; i < pd.getGroupsCount(); i++)
		{
			for (int j = 0; j < pd.getGroup(i).getTestCount(); j++)
			{
				TestResult tRes = sRes.getTestResult(i, j);
				data[k][0] = "" + (i + 1) + "." + (j + 1);
				data[k][1] = tRes.getScore();
				data[k][2] = tRes.getMaxTime();
				data[k][3] = tRes.getMaxMemory();
				data[k][4] = tRes.getMaxOutput();
				data[k][5] = tRes.getRuntimeInfo().result;
				data[k][6] = tRes.getValidationInfo().getResult();
				data[k][7] = tRes.getValidationInfo().getCheckerOutput();
				data[k][8] = tRes.getJudgement();
				data[k][9] = tRes.getValidationInfo().getValidatorName();
				k++;
			}
		}
		setTable();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getActionCommand().equalsIgnoreCase("DOIT"))
		{
			FileDialog fd = new FileDialog(this, "Save to...", FileDialog.LOAD);
			fd.setVisible(true);
			if (fd.getFile() != null)
			{
				String filename = fd.getDirectory() + fd.getFile();
				SubmissionResult sRes = Judge.judgeSourceFile(filename, "%AUTO%", pd, new CheckParams());
				System.out.println(sRes.getResult());
				makeTable(sRes);
			}			
		}
	}
}
