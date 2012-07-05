package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import foundation.Cell;
import foundation.CellTable;
import foundation.Truck;
import foundation.TruckTable;
import foundation.UnitTable;

public class Thesis extends JFrame {
	private UnitTable unitTable;
	private TruckTable truckTable;
	private CellTable cellTable;

	private JTabbedPane tabs = new JTabbedPane();

	public Thesis(UnitTable unitTable, TruckTable truckTable,
			CellTable cellTable) {
		this.unitTable = unitTable;
		this.truckTable = truckTable;
		this.cellTable = cellTable;
		tabs.addTab("individual", new Individual());
		tabs.addTab("statistics", new Statistics());
		tabs.addTab("map", new JScrollPane(new FactoryMap()));
		add(tabs);
	}

	class Individual extends JPanel {
		private JComboBox comboBoxTrucks;
		private JTextArea truckInformation;
		private JComboBox comboBoxCells;
		private JTextArea cellInformation;
		private JDialog cellPlot;

		private Object[] trucks = truckTable.getKeySet().toArray();
		private Object[] cells = cellTable.getKeySet().toArray();

		public Individual() {
			comboBoxTrucks = new JComboBox(trucks);
			comboBoxTrucks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// which truck is picked
					String selected = (String) comboBoxTrucks.getSelectedItem();
					Truck truck = truckTable.getTruck(selected);
					Iterator<String> iterator = truck.getPathList().iterator();
					StringBuilder sb = new StringBuilder();
					while (iterator.hasNext())
						sb.append(iterator.next() + "->");
					// it is possible that sb.length is zero, so we need to
					// check
					if (sb.length() >= 2)
						sb.delete(sb.length() - 2, sb.length());
					// total distance of this truck
					// sb.append(truck.calculateDistance(map));
					// put all the information in the text area
					truckInformation.setText(sb.toString());
				}
			});

			add(comboBoxTrucks);
			truckInformation = new JTextArea(10, 60);
			add(new JScrollPane(truckInformation));

			comboBoxCells = new JComboBox(cells);
			comboBoxCells.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// which cell is picked
					String selected = (String) comboBoxCells.getSelectedItem();
					Cell cell = cellTable.getCell(selected);
					cellInformation.setText(cell.toString());
					cellPlot = new CellPlot(null, cell);
					cellPlot.setVisible(true);
				}
			});

			add(comboBoxCells);
			cellInformation = new JTextArea(10, 60);
			add(new JScrollPane(cellInformation));
		}
	}

	class Statistics extends JPanel {
		private JButton unit;
		private UnitStatistics units;

		// private JButton truck;
		// private TruckStatistics trucks;

		public Statistics() {
			unit = new JButton("Unit");
			unit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					units = new UnitStatistics(null, unitTable);
					units.setVisible(true);
				}
			});
			add(unit);

			// truck = new JButton("Truck");
			// truck.addActionListener(new ActionListener() {
			// public void actionPerformed(ActionEvent e) {
			// trucks = new TruckStatistics(null, truckTable);
			// trucks.setVisible(true);
			// }
			// });
			// add(truck);
		}
	}

	class FactoryMap extends JPanel {
		public FactoryMap() {
			setLayout(new BorderLayout());
			add(new SectionB(), BorderLayout.NORTH);
			add(new SectionK(), BorderLayout.WEST);
		}

	}

	class SectionK extends JPanel {
		public SectionK() {
			setLayout(new GridLayout(21, 1));
			for (int i = 0; i < 20; ++i) {
				JButton tmp = new JButton("Button " + i);
				tmp.setSize(5, 5);
				add(tmp);
			}
		}
	}

	class SectionB extends JPanel {
		public SectionB() {
			setLayout(new GridLayout(1, 40));
			for (int i = 0; i < 39; ++i) {
				JButton tmp = new JButton("Button " + i);
				tmp.setSize(10, 5);
				add(tmp);
			}
		}
	}

	public static void createAndShowGUI(UnitTable unitTable,
			TruckTable truckTable, CellTable cellTable) {
		JFrame frame = new Thesis(unitTable, truckTable, cellTable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		final UnitTable unitTable = new UnitTable();
		final TruckTable truckTable = new TruckTable(unitTable);
		final CellTable cellTable = new CellTable(unitTable);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(unitTable, truckTable, cellTable);
			}
		});
	}
}
