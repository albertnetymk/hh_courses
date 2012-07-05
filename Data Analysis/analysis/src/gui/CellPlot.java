package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.jfree.chart.*;
import org.jfree.data.time.*;

import foundation.*;

public class CellPlot extends JDialog {
	public CellPlot (JFrame parent, Cell cell) {
		super(parent, cell.getName(), true);
		setLayout(new FlowLayout());
		// dispose this dialog by pressing ok
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				dispose();
				}
		});
		add(ok);
		JFreeChart chart = createChart(cell.getName()+"Usage", cell);
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	private JFreeChart createChart(String title, Cell cell) {
		TimeSeries time = new TimeSeries("Number", null, null);
		TreeMap<Second, Integer> list;
		list = cell.getList();
		Iterator<Second> iterator = list.keySet().iterator();
		while(iterator.hasNext()) {
			Second current = iterator.next();
			time.add(current, list.get(current));
		}
		TimeSeriesCollection timeCollection = new TimeSeriesCollection(time);
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Time", "Usage", timeCollection, true, true, false);
		return chart;
	}
}
