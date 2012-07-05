package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import foundation.UnitTable;

public class UnitStatistics extends JDialog {
	// constructor for relocation statistics
	public UnitStatistics(JFrame parent, UnitTable unitTable) {
		super(parent, "", true);
		setLayout(new FlowLayout());
		JFreeChart chart = createChart("Unit's relocation times statistics information", unitTable);
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
		// dispose this dialog by pressing ok
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(ok);
	}

	private JFreeChart createChart(String title, UnitTable unitTable) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		java.util.List<Integer> list = unitTable.relocationStatistics();
		dataset.addValue((Number) list.get(0), "Relocation",
				"Not shipped out yet");
		for (int i = 1; i < list.size(); ++i) {
			dataset.addValue(((Number) list.get(i)), "Relocation", i);
		}
		JFreeChart chart = ChartFactory.createBarChart(title,
				"Relocation times", "Number", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
}
