package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.time.*;
import org.jfree.data.category.*;

import foundation.*;

public class TruckStatistics extends JDialog {
	public TruckStatistics (JFrame parent, TruckTable truckTable) {
		super(parent, "", true);
		setLayout(new FlowLayout());
		// dispose this dialog by pressing ok
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				dispose();
				}
		});
		add(ok);
		JFreeChart chart = createChart("Truck statistics information", truckTable);
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	private JFreeChart createChart(String title, TruckTable truckTable) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		java.util.List<Integer> list = truckTable.relocationTimes();
		for ( int i = 0; i <= 4; ++i) { 
			int j = i+1;
			dataset.addValue(((Number)list.get(i)), "Relocation", j); 
		}
		JFreeChart chart = ChartFactory.createBarChart(title, "Relocation times", "Number", dataset, PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
}
