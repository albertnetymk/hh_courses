package foundation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * this class is namely for the reading data from "Distance Matrix.txt"
 */
public class Matrix {
	private Map<String, Map<String, Double>> matrix;
	private Map<String, Double> row;

	private Iterator<String> iterator;

	/*
	 * constructor of matrix Robot, KAJ, LU, M
	 */
	public Matrix() {
		matrix = new HashMap<String, Map<String, Double>>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("DistanceMatrix.txt"));
			String line;

			// Conveyor
			for (int i = 1; i <= 2; ++i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", 28, 28);
				readString("H", 10, 9);
				readString("G", 8, 8);
				readString("F", 3, 3);
				readString("E", 32, 32);
				readString("B", 42, 42);
				readString("A", 31, 31);
				if (i == 1)
					matrix.put("ConveyorLeft", row);
				else
					matrix.put("ConveyorRight", row);
			}

			// A section
			for (int i = 31; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", 28, 28);
				readString("H", 10, 9);
				readString("G", 8, 8);
				readString("F", 3, 3);
				readString("E", 32, 32);
				readString("B", 42, 42);
				readString("A", i - 1, i - 1);
				if (i <= 9)
					matrix.put("A0" + i, row);
				else
					matrix.put("A" + i, row);
			}

			// B section
			for (int i = 42; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", 28, 28);
				readString("H", 10, 9);
				readString("G", 8, 8);
				readString("F", 3, 3);
				readString("E", 32, 32);
				readString("B", i - 1, i - 1);
				if (i <= 9)
					matrix.put("B0" + i, row);
				else
					matrix.put("B" + i, row);
			}

			// E section
			for (int i = 32; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", 28, 28);
				readString("H", 10, 9);
				readString("G", 8, 8);
				readString("F", 3, 3);
				readString("E", i - 1, i - 1);
				if (i <= 9)
					matrix.put("E0" + i, row);
				else
					matrix.put("E" + i, row);
			}

			// F section
			for (int i = 3; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", 28, 28);
				readString("H", 10, 9);
				readString("G", 8, 8);
				readString("F", i - 1, i - 1);
				matrix.put("F0" + i, row);
			}

			// G section
			for (int i = 8; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", 28, 28);
				readString("H", 10, 9);
				readString("G", i - 1, i - 1);
				matrix.put("G0" + i, row);
			}
			// H section
			for (int i = 9; i >= 0; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", 28, 28);
				readString("H", i, i - 1);
				matrix.put("H0" + i, row);
			}
			// K section
			for (int i = 28; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", 8, 8);
				readString("K", i - 1, i - 1);
				if (i <= 9)
					matrix.put("K0" + i, row);
				else
					matrix.put("K" + i, row);
			}
			// M section
			for (int i = 8; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", 7, 7);
				readStringLU("LU", 10, 10);
				readString("M", i - 1, i - 1);
				matrix.put("M0" + i, row);
			}
			// LU section
			for (int i = 10; i >= 1; --i) {
				if (i != 5) {
					row = new HashMap<String, Double>();
					line = in.readLine();
					iterator = Arrays.asList(line.split(",")).iterator();
					row.put("Robot", new Double(iterator.next()));
					readString("KAJ", 7, 7);
					readStringLU("LU", i - 1, i - 1);
					if (i <= 9)
						matrix.put("LU0" + i, row);
					else
						matrix.put("LU" + i, row);
				}
			}
			// KAJ section
			for (int i = 7; i >= 1; --i) {
				row = new HashMap<String, Double>();
				line = in.readLine();
				iterator = Arrays.asList(line.split(",")).iterator();
				row.put("Robot", new Double(iterator.next()));
				readString("KAJ", i - 1, i - 1);
				matrix.put("KAJ0" + i, row);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * read values into internal matrix
	 */
	private void readString(String name, int number, int index) {
		for (int i = index, j = 1; j <= number; ++j, --i) {
			if (index <= 9)
				row.put(name + "0" + number, new Double(iterator.next()));
			else
				row.put(name + "" + number, new Double(iterator.next()));
		}
	}

	private void readStringLU(String name, int number, int index) {
		for (int i = index, j = 1; j <= number; ++j, --i) {
			if (i != 5) {
				if (index <= 9)
					row.put(name + "0" + number, new Double(iterator.next()));
				else
					row.put(name + "" + number, new Double(iterator.next()));
			}
		}
	}

	public double getDistance(String source, String destination) {
		if (matrix.get(source).get(destination) != null)
			return matrix.get(source).get(destination);
		else
			return matrix.get(destination).get(source);
	}
}
