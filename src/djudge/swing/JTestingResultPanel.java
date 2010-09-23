/* $Id$ */

package djudge.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import djudge.acmcontester.admin.AdminClient;
import djudge.judge.ProblemResult;
import djudge.judge.TestResult;

public class JTestingResultPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@SuppressWarnings("unused")
	private void init()
	{
		
	}
	
	private JTable table;

	@SuppressWarnings("unused")
	private ProblemResult pd;
	
	@SuppressWarnings("unused")
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
	
	private void makeTable(ProblemResult pr)
	{
		int tests = pr.getTestsCount();
		data = new Object[tests][10];
		int k = 0;
		for (int i = 0; i < pr.getGroupsCount(); i++)
		{
			for (int j = 0; j < pr.getGroupResult(i).getTestsCount(); j++)
			{
				TestResult tRes = pr.getTestResult(i, j);
				data[k][0] = "" + (i + 1) + "." + (j + 1);
				data[k][1] = tRes.getScore();
				data[k][2] = tRes.getMaxTime();
				data[k][3] = tRes.getMaxMemory();
				data[k][4] = tRes.getMaxOutput();
				data[k][5] = tRes.getRuntimeInfo().result;
				data[k][6] = tRes.getValidationInfo().result;
				data[k][7] = tRes.getValidationInfo().validatorOutput;
				data[k][8] = tRes.getJudgement();
				data[k][9] = tRes.getValidationInfo().validatorName;
				k++;
			}
		}
	}
	
	
	public JTestingResultPanel(ProblemResult sr)
	{
		setBorder(BorderFactory.createTitledBorder("Testing details"));
		pd = sr;
		makeTable(sr);
		
		table = new JTable(data, columnNames);
		table.setRowHeight(20);
		
		TableColumnModel cm = table.getColumnModel();
		cm.getColumn(table.getColumnCount()-2).setCellRenderer(new ColorRenderer());
		
		add(spTable = new JScrollPane(table), BorderLayout.CENTER);
	}
		
	public static void main(String[] args)
	{
		new AdminClient();
	}
}
