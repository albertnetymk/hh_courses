package matrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DistanceMatrix {
	private Map<String, Map<String, Double>> matrix; // the one that comprises double layers of map
	private double[][] result; // the one that is an array
	private Map<String, Double> row;
	private SectionTable pool;

	private double base;
	private int i, j;
	private int k, l;

	public DistanceMatrix() {
		matrix = new HashMap<String, Map<String, Double>>();
		pool = new SectionTable();

		// sectionBMap contains all the values from B01 to all cells in B section so that we can just get what we want
		// by subtracting the extra distance
		Map<String, Double> sectionBMap = new HashMap<String, Double>();
		SectionB sectionB = ((SectionB) pool.getSection("B"));
		for (j = 1; j <= 9; ++j)
			sectionBMap.put("B0" + j, sectionB.getWidth_1() * (j - 1));

		sectionBMap.put("B" + 10, sectionB.getWidth_1() * 9);

		for (j = 11; j <= 21; ++j)
			sectionBMap.put("B" + j, sectionB.getWidth_1() * 9 + sectionB.getInterval_1() + sectionB.getWidth_2() * (j - 11));

		for (j = 22; j <= 31; ++j)
			sectionBMap.put("B" + j, sectionB.getWidth_1() * 9 + sectionB.getInterval_1() + sectionB.getWidth_2() * 10
					+ sectionB.getInterval_2() + sectionB.getWidth_3() * (j - 22));

		for (j = 32; j <= 42; ++j)
			sectionBMap.put("B" + j, sectionB.getWidth_1() * 9 + sectionB.getInterval_1() + sectionB.getWidth_2() * 10
					+ sectionB.getInterval_2() + sectionB.getWidth_3() * 9 + sectionB.getInterval_3() + sectionB.getWidth_4()
					* (j - 32));

		// the reason why source is not a global variable is that the type of source is uncertain
		// Source ConveyorLeft section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("ConveyorLeft"));
			{
				row = new HashMap<String, Double>();
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					for (j = 1; j <= destination.getNumber(); ++j) {
						base = 46.23; // the distance between ConveyorLeft and A01
						fillRow(source, destination, base, 1, row);
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 152.67; // the distance between B(40,41) and B01
					for (j = 1; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, Math
								.abs(base - sectionBMap.get(destination.getName() + "0" + j))
								+ source.getHeight() + destination.getHeight());
					for (j = 10; j <= 42; ++j)
						row.put(destination.getName() + "" + j, Math.abs(base - sectionBMap.get(destination.getName() + "" + j))
								+ source.getHeight() + destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 159.62; // the distance between ConveyorLeft and E01
					fillRow(source, destination, base, 1, row);
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					// the distance between F and ConveyorLeft
					double a, b, c;
					a = 178.62;
					b = 162.37;
					c = 157.16;
					fillRowF(source, destination, a, b, c, row);

				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 6.1; // the distance between ConveyorLeft and G01
					fillRow(source, destination, base, 1, row);
				}

				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 42.08; // the distance between ConveyorLeft and H00
					fillRow(source, destination, base, 0, row);
				}

				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 10.34; // the distance between ConveyorLeft and K(6,7)
					for (j = 7; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (j - 7)
									+ destination.getHeight());
						else
							row.put("K" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (j - 7)
									+ destination.getHeight());
					}

					for (j = 6; j >= 1; --j)
						row.put("K0" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (6 - j)
								+ destination.getHeight());

				}

				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 32.31; // the distance between ConveyorLeft and M08
					fillRow(source, destination, base, 8, row);
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, 70.26, 254.67, row);
				}
				// KAJ section
				{
					// This is dealt in KAJ-ConveyorLeft.
				}
				// Robot section
				{
					// This is dealt in Robot-ConveyorLeft.
				}

				// after filling out this row, we put it in the matrix
				matrix.put(source.getName(), row);
			}
		}
		// Source ConveyorRight section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("ConveyorRight"));
			{
				row = new HashMap<String, Double>();
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					for (j = 1; j <= destination.getNumber(); ++j) {
						base = 2.79; // the distance between ConveyorRight and A31
						fillRow(source, destination, base, 31, row);
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 10; // the distance between B(03,04) and B01
					for (j = 1; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, Math
								.abs(base - sectionBMap.get(destination.getName() + "0" + j))
								+ source.getHeight() + destination.getHeight());
					for (j = 10; j <= 42; ++j)
						row.put(destination.getName() + "" + j, Math.abs(base - sectionBMap.get(destination.getName() + "" + j))
								+ source.getHeight() + destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 16.95; // the distance between ConveyorRight and E01
					fillRow(source, destination, base, 1, row);
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					// the distance between F and ConveyorLeft
					double a, b, c;
					a = 98.99;
					b = 19.71;
					c = 14.5;
					fillRowF(source, destination, a, b, c, row);
				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 116.24; // the distance between ConveyorRight and G08
					fillRow(source, destination, base, 8, row);
				}

				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 184.75; // the distance between ConveyorRight and H00
					fillRow(source, destination, base, 0, row);
				}

				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 153; // the distance between ConveyorRight and K(6,7)
					for (j = 7; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (j - 7)
									+ destination.getHeight());
						else
							row.put("K" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (j - 7)
									+ destination.getHeight());
					}

					for (j = 6; j >= 1; --j)
						row.put("K0" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (6 - j)
								+ destination.getHeight());

				}

				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 115; // the distance between ConveyorRight and M08
					fillRow(source, destination, base, 8, row);
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, 212.93, 112, row);
				}
				// KAJ section
				{
					// This is dealt in KAJ-ConveyorRight.
				}
				// Robot section
				{
					// This is dealt in Robot-ConveyorRight.
				}

				// after filling out this row, we put it in the matrix
				matrix.put(source.getName(), row);
			}
		}

		// Source A section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("A"));
			for (i = 1; i <= source.getNumber(); ++i) {
				// we are working on one particular line inside the outer loop
				row = new HashMap<String, Double>();
				// A section
				{
					SectionSimple destination = source;
					base = 0;

					for (j = i + 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							// put values ranging from A02 to A09
							row.put(destination.getName() + "0" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
						else
							// put values ranging from A10 to A31
							row.put(destination.getName() + "" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 106.44 - source.getWidth() * (i - 1); // the distance between Ai and B01
					for (j = 1; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, Math
								.abs(base - sectionBMap.get(destination.getName() + "0" + j))
								+ source.getHeight() + destination.getHeight());
					for (j = 10; j <= 42; ++j)
						row.put(destination.getName() + "" + j, Math.abs(base - sectionBMap.get(destination.getName() + "" + j))
								+ source.getHeight() + destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 113.39 - source.getWidth() * (i - 1); // the distance between Ai and E01
					fillRow(source, destination, base, 1, row);
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					// the distance between F and A21,A22
					double a, b, c;
					if (i <= 21) {
						a = 69.97;
						b = 53.71;
						c = 48.51;
						base = -source.getWidth() * (i - 1); // the distance
						// between Ai
						// and A21;
						fillRowF(source, destination, a + base, b + base, c + base, row);
					} else {
						a = 68.99;
						b = 50.59;
						c = 45.38;
						base = source.getWidth() * (i - 22); // the distance
						// between Ai
						// and A21;
						fillRowF(source, destination, a + base, b - base, c - base, row);
					}

				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 20 + source.getWidth() * (i - 1);
					fillRow(source, destination, base, 8, row);
				}

				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 88.37 + source.getWidth() * (i - 1);
					fillRow(source, destination, base, 0, row);
				}

				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 56.56 + source.getWidth() * (i - 1);
					for (j = 7; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (j - 7)
									+ destination.getHeight());
						else
							row.put("K" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (j - 7)
									+ destination.getHeight());
					}

					for (j = 6; j >= 1; --j)
						row.put("K0" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (6 - j)
								+ destination.getHeight());

				}

				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 19.16 + source.getWidth() * (i - 1);
					fillRow(source, destination, base, 8, row);
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, 116.49 + source.getWidth() * (i - 1) + source.getHeight(), (208.44 - source
							.getWidth()
							* (i - 1))
							+ source.getHeight(), row);
				}
				// KAJ section
				{
					// This is dealt in KAJ-A section
				}
				// Robot section
				{
					base = 124.19 - source.getWidth() * (i - 1);
					row.put("Robot", base+source.getHeight());
				}

				// after filling out this row, we put it in the matrix
				if (i < 10)
					matrix.put(source.getName() + "0" + i, row);
				else
					matrix.put(source.getName() + "" + i, row);
			}
		}

		// Source B section
		{
			SectionB source = ((SectionB) pool.getSection("B"));

			// because of the complexity of B section, it is really not a good idea to
			// put B in the source position. Therefore, I will reverse the order
			// so that B is in the destination position
			for (i = 1; i <= source.getNumber(); ++i) {
				// we are working on one particular line inside the outer loop
				row = new HashMap<String, Double>();
				// B section
				{
					SectionB destination = source;

					for (j = i + 1; j <= destination.getNumber(); ++j) {
						if (j <= 9) {
							// if j is less than 9, i should be less than 9 as
							// well
							row.put(destination.getName() + "0" + j, source.getHeight()
									+ (sectionBMap.get(destination.getName() + "0" + j) - sectionBMap.get(source.getName() + "0"
											+ i)) + destination.getHeight());
						} else {
							// j is larger than 9
							// now we need to determine whether i is less than 9
							// or not
							if (i <= 9) {
								row.put(destination.getName() + "" + j, source.getHeight()
										+ (sectionBMap.get(destination.getName() + "" + j) - sectionBMap.get(source.getName()
												+ "0" + i)) + destination.getHeight());
							} else
								row.put(destination.getName() + "" + j, source.getHeight()
										+ sectionBMap.get(destination.getName() + "" + j)
										- sectionBMap.get(source.getName() + "" + i) + destination.getHeight());

						}
					}
				}
				// E section
				{
					// This is dealt in E-B section
				}
				// F section
				{
					// This is dealt in F-B section
				}

				// G section
				{
					// This is dealt in G-B section
				}

				// H section
				{
					// This is dealt in H-B section
				}
				// K section
				{
					// This is dealt in K-B section
				}
				// M section
				{
					// This is dealt in M-B section
				}

				// LU section
				{
					// This is dealt in LU-B section
				}
				// KAJ section
				{
					// This is dealt in KAJ-B section
				}
				// Robot section
				{
					// This is dealt in Robot-B section
				}
				if (i < 10)
					matrix.put(source.getName() + "0" + i, row);
				else
					matrix.put(source.getName() + "" + i, row);

			}
		}

		// Source E section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("E"));
			// B section
			// This was in B-E section, and it transfered here for simplifying reason
			for (i = 1; i <= source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				base = 6.95 + source.getWidth() * (i - 1);
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, base + sectionBMap.get(destination.getName() + "0" + j)
							+ source.getHeight() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, base + sectionBMap.get(destination.getName() + "" + j)
							+ source.getHeight() + destination.getHeight());

				// Now this row is filled out, and this row is from G + i to all
				// the B cells
				// we need to distribute these values to the corresponding rows
				regroup(source, destination, row);
			}

			for (i = 1; i <= source.getNumber(); ++i) {
				// we are working on one particular line inside the outer loop
				row = new HashMap<String, Double>();
				// E section
				{
					SectionSimple destination = source;
					base = 0;
					for (j = i + 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					base = source.getWidth() * (i - 3);
					switch (i) {
					case 1:
						fillRowF(source, destination, 115.92, 2.75, 2.45, row);
						break;
					case 2:
						fillRowF(source, destination, 118.62, 0.05, 5.15, row);
						break;
					default:
						fillRowF(source, destination, 121.32 + base, 2.65 + base, 7.85 + base, row);
						break;
					}

				}

				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 133.39 + source.getWidth() * (i - 1);
					fillRow(source, destination, base, 8, row);
				}

				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 201.70 + source.getWidth() * (i - 1);
					fillRow(source, destination, base, 0, row);
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 169.95 + source.getWidth() * (i - 1);
					for (j = 7, k = 1; j <= 28; ++j, ++k) {
						if (j <= 9)
							row.put("K0" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
						else
							row.put("K" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
					}

					for (j = 6, k = 1; j >= 1; --j, ++k) {
						if (j <= 9)
							row.put("K0" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
						else
							row.put("K" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 132.55 + source.getWidth() * (i - 1);
					fillRow(source, destination, base, 8, row);
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, 229.88 + source.getWidth() * (i - 1) + source.getHeight(), (95.05 - source
							.getWidth()
							* (i - 1))
							+ source.getHeight(), row);
				}
				// KAJ section
				{
					// This is dealt in KAJ-E section
				}
				// Robot section
				{
					// This is dealt in Robot-E section
				}

				if (i < 10)
					matrix.put(source.getName() + "0" + i, row);
				else
					matrix.put(source.getName() + "" + i, row);
			}
		}
		// Source F Section
		{
			SectionF source = ((SectionF) pool.getSection("F"));

			// B section This was in B-F section, and it transfered here for simplifying reason
			{
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				// F01
				i = 1;
				base = 72.97; // the distance between F01 and B10
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, base + destination.getWidth_1() * (10 - j) + source.getHeight_1()
							+ destination.getHeight());

				row.put(destination.getName() + "" + 10, base + source.getHeight_1() + destination.getHeight());

				base = 67.46;
				for (j = 11; j <= 42; ++j)
					row.put(destination.getName() + "" + j, base + (sectionBMap.get(destination.getName() + "" + j) - 41.5)
							+ source.getHeight_1() + destination.getHeight());

				// Now this row is filled out, and this row is from F01 to all the B cells we need to regroup all these rows
				regroup(source, destination, row);

				// F02
				i = 2;
				row = new HashMap<String, Double>();

				base = 9.7; // distance between F02 and B01

				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, base + sectionBMap.get(destination.getName() + "0" + j)
							+ source.getHeight_2() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, base + sectionBMap.get(destination.getName() + "" + j)
							+ source.getHeight_2() + destination.getHeight());

				// Now this row is filled out, and this row is from F02 to all the B cells we need to regroup all these rows
				regroup(source, destination, row);

				// F03
				i = 3;
				row = new HashMap<String, Double>();

				base = 4.5;// distance between F03 and B01
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, base + sectionBMap.get(destination.getName() + "0" + j)
							+ source.getHeight_2() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, base + sectionBMap.get(destination.getName() + "" + j)
							+ source.getHeight_2() + destination.getHeight());
				// Now this row is filled out, and this row is from F02 to all the B cells we need to regroup all these rows
				regroup(source, destination, row);
			}
			{
				// F01
				{
					i = 1;
					row = new HashMap<String, Double>();

					// F Section
					{
						SectionF destination = source;
						j = 2;
						row.put(source.getName() + "0" + j, 117.67);
						j = 3;
						row.put(source.getName() + "0" + j, 113.47);
					}

					// K section
					{
						SectionSimple destination = ((SectionSimple) pool.getSection("K"));
						base = 172.7; // the distance between F01 and K01
						for (j = 1; j <= 28; ++j) {
							if (j <= 9)
								row.put("K0" + j, base + source.getHeight_1() + destination.getWidth() * (j - 1)
										+ destination.getHeight());
							else
								row.put("K" + j, base + source.getHeight_1() + destination.getWidth() * (j - 1)
										+ destination.getHeight());
						}
					}
					// LU section
					{
						Section destination = pool.getSection("LU");
						fillRowLU(source, destination, 248.95 + source.getHeight_1(), 210.97 + source.getHeight_1(), row);
					}
					matrix.put(source.getName() + "0" + i, row);

				}
				// F02
				{
					i = 2;
					row = new HashMap<String, Double>();

					// F Section
					{
						SectionF destination = source;
						j = 3;
						row.put(source.getName() + "0" + j, 5.21);
					}
					// K section
					{
						SectionSimple destination = ((SectionSimple) pool.getSection("K"));
						base = 172.7; // the distance between F02 and K(6,7)
						for (j = 7, k = 1; j <= 28; ++j, ++k) {
							if (j <= 9)
								row.put("K0" + j, base + 2.55 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
							else
								row.put("K" + j, base + 2.55 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
						}

						for (j = 6, k = 1; j >= 1; --j, ++k) {
							if (j <= 9)
								row.put("K0" + j, base + 0.64 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
							else
								row.put("K" + j, base + 0.64 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
						}
					}
					// LU section
					{
						Section destination = pool.getSection("LU");
						fillRowLU(source, destination, 232.63 + source.getHeight_2(), 92.3 + source.getHeight_2(), row);
					}
					matrix.put(source.getName() + "0" + i, row);
				}
				// F03
				{
					i = 3;
					row = new HashMap<String, Double>();

					// K section
					{
						SectionSimple destination = ((SectionSimple) pool.getSection("K"));
						base = 167.5; // the distance between F03 and K(6,7)
						for (j = 7, k = 1; j <= 28; ++j, ++k) {
							if (j <= 9)
								row.put("K0" + j, base + 2.55 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
							else
								row.put("K" + j, base + 2.55 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
						}

						for (j = 6, k = 1; j >= 1; --j, ++k) {
							if (j <= 9)
								row.put("K0" + j, base + 0.64 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
							else
								row.put("K" + j, base + 0.64 + source.getHeight_2() + destination.getWidth() * (k - 1)
										+ destination.getHeight());
						}
					}
					// LU section
					{
						Section destination = pool.getSection("LU");
						fillRowLU(source, destination, 227.43 + source.getHeight_3(), 97.5 + source.getHeight_3(), row);
					}
					matrix.put(source.getName() + "0" + i, row);
				}
				// KAJ section
				{
					// This is dealt in F-KAJ section
				}
				// Robot section
				{
					// This is dealt in F-Robot section
				}
			}
		}

		// Source G section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("G"));

			// B section
			// This was in B-G section, and it transfered here for simplifying
			// reason
			for (i = 1; i <= source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				base = 146.56 - source.getWidth() * (i - 1);
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, Math.abs(base - sectionBMap.get(destination.getName() + "0" + j))
							+ source.getHeight() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, Math.abs(base - sectionBMap.get(destination.getName() + "" + j))
							+ source.getHeight() + destination.getHeight());

				// Now this row is filled out, and this row is from G + i to all
				// the B cells
				// we need to regroup all these rows
				regroup(source, destination, row);
			}
			// F section
			// This was in F-G section, and it transfered here for simplifying reason
			for (i = 1; i <= source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionF destination = ((SectionF) pool.getSection("F"));
				base = -source.getWidth() * (i - 1);
				row.put(destination.getName() + "01", source.getHeight() + 172.53 + base + destination.getHeight_1());
				row.put(destination.getName() + "02", source.getHeight() + 156.27 + base + destination.getHeight_2());
				row.put(destination.getName() + "03", source.getHeight() + 151.06 + base + destination.getHeight_3());
				// Now this row is filled out, and this row is from Gi to all the F cells
				// we need to regroup all these rows
				regroup(source, destination, row);
			}

			for (i = 1; i <= source.getNumber(); ++i) {
				// we are working on one particular line inside the outer loop
				row = new HashMap<String, Double>();

				// G section
				{
					SectionSimple destination = source;
					base = 0;
					for (j = i + 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
					}
				}

				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 48.18 + source.getWidth() * (i - 1);
					fillRow(source, destination, base, 0, row);
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 16.44 + source.getWidth() * (i - 1);
					for (j = 7, k = 1; j <= 28; ++j, ++k) {
						if (j <= 9)
							row.put("K0" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
						else
							row.put("K" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
					}

					for (j = 6, k = 1; j >= 1; --j, ++k) {
						if (j <= 9)
							row.put("K0" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
						else
							row.put("K" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
									+ destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 26.20 - source.getWidth() * (i - 1);
					fillRow(source, destination, base, 8, row);
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, 76.36 + source.getWidth() * (i - 1) + source.getHeight(), (248.56 - source
							.getWidth()
							* (i - 1))
							+ source.getHeight(), row);
				}
				// KAJ section
				{
					// This is dealt in KAJ-G section
				}
				// Robot section
				{
					base = 164.32 - source.getWidth() * (i - 1); // the distance between Gi and Robot 
					row.put("Robot", base+source.getHeight());
				}

				matrix.put(source.getName() + "0" + i, row);
			}
		}

		// Source H section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("H"));
			// B section
			// This was in B-H section, and it transfered here for simplifying
			// reason
			for (i = 0; i < source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				base = 194.75 + source.getWidth() * i;
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, (base - sectionBMap.get(destination.getName() + "0" + j))
							+ source.getHeight() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, (base - sectionBMap.get(destination.getName() + "" + j))
							+ source.getHeight() + destination.getHeight());

				// Now this row is filled out, and this row is from Hi to all
				// the B cells
				// we need to regroup all these rows
				regroup(source, destination, row);
			}
			// F section
			// This was in F-H section, and it transfered here for simplifying
			// reason
			for (i = 0; i < source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionF destination = ((SectionF) pool.getSection("F"));
				base = -source.getWidth() * (i - 1);
				double a, b, c;
				a = 204.45 + base;
				b = 204.25 + base;
				c = 199.25 + base;
				row.put(destination.getName() + "01", source.getHeight() + a + destination.getHeight_1());
				row.put(destination.getName() + "02", source.getHeight() + b + destination.getHeight_2());
				row.put(destination.getName() + "03", source.getHeight() + c + destination.getHeight_3());
				regroup(source, destination, row);
			}
			for (i = 0; i < source.getNumber(); ++i) {
				// we are working on one particular line inside the outer loop
				row = new HashMap<String, Double>();
				// H section
				{
					SectionSimple destination = source;
					base = 0;
					for (j = i + 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
					}
				}

				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					// tmpMap contains all the values from K01 to the rest of
					// all cells
					// in K section so that we can just get what we want by
					// subtracting the extra distance
					// here we are using the same technique as we did for
					// section B
					Map<String, Double> tmpMap = new HashMap<String, Double>();
					for (j = 1; j <= 9; ++j)
						tmpMap.put("K0" + j, destination.getWidth() * (j - 1));
					for (j = 10; j <= destination.getNumber(); ++j)
						tmpMap.put("K" + j, destination.getWidth() * (j - 1));

					base = 48.32 + source.getWidth() * i;
					for (j = 1; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, Math.abs(base - tmpMap.get(destination.getName() + "0" + j))
								+ source.getHeight() + destination.getHeight());
					for (j = 10; j <= destination.getNumber(); ++j)
						row.put(destination.getName() + "" + j, Math.abs(base - tmpMap.get(destination.getName() + "" + j))
								+ source.getHeight() + destination.getHeight());
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 74.38 + source.getWidth() * i;
					fillRow(source, destination, base, 8, row);
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, (28.18 - source.getWidth() * i) + source.getHeight(), 296.75
							+ source.getWidth() * i + source.getHeight(), row);
				}
				// KAJ section
				{
					// This is dealt in KAJ-H section
				}
				// Robot section
				{
					base = 212.50 + source.getWidth() * i; // the distance between Hi and Robot
					row.put("Robot", base+source.getHeight());
				}
				if (i < 10)
					matrix.put(source.getName() + "0" + i, row);
				else
					matrix.put(source.getName() + "" + i, row);

			}
		}
		// Source K Section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("K"));

			// B section
			// This was in B-K section, and it transfered here for simplifying reason
			for (i = 7; i <= source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				base = 163 + 2.55 + source.getWidth() * (i - 7);
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, (base - sectionBMap.get(destination.getName() + "0" + j))
							+ source.getHeight() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, Math.abs(base - sectionBMap.get(destination.getName() + "" + j))
							+ source.getHeight() + destination.getHeight());

				// Now this row is filled out, and this row is from Ki to all
				// the B cells
				// we need to regroup all these rows
				regroup(source, destination, row);
			}
			for (i = 6; i >= 1; --i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				base = 163 + 0.64 + source.getWidth() * (6 - i);
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, Math.abs(base - sectionBMap.get(destination.getName() + "0" + j))
							+ source.getHeight() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, Math.abs(base - sectionBMap.get(destination.getName() + "" + j))
							+ source.getHeight() + destination.getHeight());

				// Now this row is filled out, and this row is from Ki to all
				// the B cells
				// we need to regroup all these rows
				regroup(source, destination, row);
			}

			for (i = 1; i <= source.getNumber(); ++i) {
				// we are working on one particular line inside the outer loop
				row = new HashMap<String, Double>();
				// K section
				{
					SectionSimple destination = source;
					base = 0;
					for (j = i + 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
					}
				}

				// M section
				{
					// This is dealt in M-K section.
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					if (i <= 6)
						fillRowLU(source, destination, 59.93 + 0.64 + source.getHeight() + source.getWidth() * (6 - i), 265
								+ 0.64 + source.getHeight() + source.getWidth() * (6 - i), row);
					else
						fillRowLU(source, destination, (59.93 - 2.55) + (source.getHeight() - source.getWidth() * (i - 7)), 265
								+ 2.55 + source.getHeight() + source.getWidth() * (i - 7), row);
				}
				// KAJ section
				{
					// This is dealt in KAJ-K section
				}
				// Robot section
				{
					// This is dealt in Robot-K section
				}
				if (i < 10)
					matrix.put(source.getName() + "0" + i, row);
				else
					matrix.put(source.getName() + "" + i, row);
			}
		}
		// Source M section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("M"));

			// B section
			// This was in B-M section, and it transfered here for simplifying reason
			for (i = 1; i <= source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				base = 122.98;
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, Math.abs(base - sectionBMap.get(destination.getName() + "0" + j))
							+ 2.62 + source.getWidth() * (8 - i) + source.getHeight() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, Math.abs(base - sectionBMap.get(destination.getName() + "" + j))
							+ 2.62 + source.getWidth() * (8 - i) + source.getHeight() + destination.getHeight());

				// Now this row is filled out, and this row is from Mi to all
				// the B cells
				// we need to regroup all these rows
				regroup(source, destination, row);
			}
			// F section
			// This was in F-M section, and it transfered here for simplifying reason
			for (i = 1; i <= source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionF destination = ((SectionF) pool.getSection("F"));
				base = -source.getWidth() * (i - 1);
				double a, b, c;
				a = 168.21 + base;
				b = 151.75 + base;
				c = 146.75 + base;
				row.put(destination.getName() + "01", source.getHeight() + a + destination.getHeight_1());
				row.put(destination.getName() + "02", source.getHeight() + b + destination.getHeight_2());
				row.put(destination.getName() + "03", source.getHeight() + c + destination.getHeight_3());
				regroup(source, destination, row);
			}
			// K section
			// This was in K-M section, and it transfered here for simplifying reason
			for (i = 1; i <= source.getNumber(); ++i) {
				Map<String, Double> row = new HashMap<String, Double>();
				SectionSimple destination = ((SectionSimple) pool.getSection("K"));
				base = 59.29 - source.getWidth() * (i - 1);
				for (j = 7, k = 1; j <= 28; ++j, ++k) {
					if (j <= 9)
						row.put("K0" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
					else
						row.put("K" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
				}

				for (j = 6, k = 1; j >= 1; --j, ++k) {
					if (j <= 9)
						row.put("K0" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
					else
						row.put("K" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
				}
				regroup(source, destination, row);
			}
			for (i = 1; i <= source.getNumber(); ++i) {
				// we are working on one particular line inside the outer loop
				row = new HashMap<String, Double>();
				// M section
				{
					SectionSimple destination = source;
					base = 0;
					for (j = i + 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + source.getHeight() + destination.getWidth() * (j - i)
									+ destination.getHeight());
					}
				}

				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, (119.22 - source.getWidth() * (i - 1)) + source.getHeight(), (244.28 - source
							.getWidth()
							* (i - 1))
							+ source.getHeight(), row);
				}

				// KAJ section
				{
					// This is dealt in KAJ-M section
				}
				// Robot section
				{
					base = 160 - source.getWidth() * (i - 1); // the distance between Mi and Robot
					row.put("Robot", base+source.getHeight());
				}
				if (i < 10)
					matrix.put(source.getName() + "0" + i, row);
				else
					matrix.put(source.getName() + "" + i, row);
			}
		}
		// Source LU section
		{
			Section source = pool.getSection("LU");

			// B section
			// This was in B-LU section, and it transfered here for simplifying
			// reason
			for (i = 1; i <= source.getNumber(); ++i) {
				if (i != 5) {
					Map<String, Double> row = new HashMap<String, Double>();
					SectionB destination = ((SectionB) pool.getSection("B"));
					if (i <= 4)
						base = 222.93;
					else
						base = 102;
					for (j = 1; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, (base - sectionBMap.get(destination.getName() + "0" + j))
								+ destination.getHeight());
					for (j = 10; j <= 42; ++j)
						row.put(destination.getName() + "" + j, (base - sectionBMap.get(destination.getName() + "" + j))
								+ destination.getHeight());

					// Now this row is filled out, and this row is from LU to
					// all
					// the B cells
					// we need to regroup all these rows
					regroup(source, destination, row);
				}
			}
			for (i = 1; i <= source.getNumber(); ++i) {
				if (i != 5) {
					row = new HashMap<String, Double>();
					// LU section
					{
						Section destination = source;
						double left, right;
						if (i <= 4) {
							left = 0;
							right = 324.93;
						} else {
							left = 324.93;
							right = 0;
						}
						for (j = 1; j <= 4; ++j)
							row.put(destination.getName() + "0" + j, left);
						for (j = 6; j <= 9; ++j)
							row.put(destination.getName() + "0" + j, right);
						row.put(destination.getName() + "" + 10, right);
					}

					// KAJ section
					{
						// This is dealt in KAJ-LU section
					}

					// Robot section
					{
						// This is dealt in Robot-LU section
					}

					if (i < 10)
						matrix.put(source.getName() + "0" + i, row);
					else
						matrix.put(source.getName() + "" + i, row);
				}
			}
		}
		// Source KAJ01 section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("KAJ01"));
			{
				Map<String, Double> row = new HashMap<String, Double>();
				// Conveyor section
				{
					base = 56.69; // the distance between KAJ01 and ConveyorLeft
					row.put("ConveyorLeft", base);

					base = 139.21; // the distance between KAJ01 and ConveyorRight
					row.put("ConveyorRight", base);
				}
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					base = 42.77;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 26.23; // distance between KAJ01 and B(31,32)
					for (j = 1; j <= 31; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base
									+ (122.98 - sectionBMap.get(destination.getName() + "0" + j)) + destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base
									+ (122.98 - sectionBMap.get(destination.getName() + "" + j)) + destination.getHeight());
					}
					base = 36.22; // distance between KAJ01 and B32
					for (j = 32; j <= 40; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_4() * (j - 32)
								+ destination.getHeight());
					row.put(destination.getName() + "" + 41, 55.29 + destination.getHeight());
					row.put(destination.getName() + "" + 42, 52.5 + destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 156.16; // the distance between KAJ01 and E01
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					fillRowF(source, destination, 142.67, 158.91, 153.70, row);
				}

				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 29.68;
					for (j = 1, k = 8; j <= destination.getNumber(); ++j, --k) {
						row.put(destination.getName() + "0" + k, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
				}
				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 78.1;
					for (j = 0; j < destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * j + destination.getHeight());
					}
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 30.03;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
						else
							row.put("K" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 28.84;
					for (j = 1; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
				}
				// LU section
				{
					Section destination = pool.getSection("LU");
					fillRowLU(source, destination, 106.28, 251.23, row);
				}

				regroupKAJ(source, row);
			}
			{
				row = new HashMap<String, Double>();
				// KAJ section
				{
					Section destination;
					destination = pool.getSection("KAJ0" + 7);
					row.put(destination.getName(), 120.07);
					destination = pool.getSection("KAJ0" + 6);
					row.put(destination.getName(), 99.98);
					destination = pool.getSection("KAJ0" + 5);
					row.put(destination.getName(), 79.94);
					destination = pool.getSection("KAJ0" + 4);
					row.put(destination.getName(), 59.97);
					destination = pool.getSection("KAJ0" + 3);
					row.put(destination.getName(), 39.91);
					destination = pool.getSection("KAJ0" + 2);
					row.put(destination.getName(), 19.95);
				}
				// Robot section
				{
					base = 166.96; // the distance between KAJ01 and Robot
					row.put("Robot", base);
				}
				matrix.put(source.getName(), row);
			}
		}
		// Source KAJ02 section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("KAJ02"));
			{
				Map<String, Double> row = new HashMap<String, Double>();
				// Conveyor section
				{
					base = 55.88; // the distance between KAJ01 and ConveyorLeft
					row.put("ConveyorLeft", base);

					base = 119.27; // the distance between KAJ01 and ConveyorRight
					row.put("ConveyorRight", base);
				}
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					base = 49.02;
					for (j = 1; j <= 5; ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
					base = 46.27;
					for (j = 6; j <= 8; ++j) {
						row.put(destination.getName() + "0" + j, base + 1.59 + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
					for (j = 9; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 46.27; // distance between KAJ02 and B(21,22)
					for (j = 1; j <= 21; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base
									+ (83 - sectionBMap.get(destination.getName() + "0" + j)) + destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + (83 - sectionBMap.get(destination.getName() + "" + j))
									+ destination.getHeight());
					}
					row.put(destination.getName() + "" + 22, 52.77 + destination.getHeight());
					row.put(destination.getName() + "" + 23, 55.77 + destination.getHeight());
					base = 32.67; // distance between KAJ02 and B31
					for (j = 24; j <= 31; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_3() * (31 - j)
								+ destination.getHeight());

					base = 32.54; // distance between KAJ02 and B32
					for (j = 32; j <= 42; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_4() * (j - 32)
								+ destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 136.24;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					fillRowF(source, destination, 122.72, 138.97, 133.77, row);
				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 29.65;
					for (j = 1, k = 8; j <= destination.getNumber(); ++j, --k) {
						row.put(destination.getName() + "0" + k, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
				}
				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 98.05;
					for (j = 0; j < destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * j + destination.getHeight());
					}
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 49.98;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
						else
							row.put("K" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 28.81;
					for (j = 1; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
				}
				// LU section
				{
					Section destination = pool.getSection("LU");
					for (j = 1; j <= 4; ++j)
						row.put(destination.getName() + "0" + j, 126.23);
					for (j = 6; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, 231.28);
					row.put(destination.getName() + "" + 10, 231.28);
				}
				regroupKAJ(source, row);
			}
			{
				// KAJ section
				{
					row = new HashMap<String, Double>();
					Section destination;
					destination = pool.getSection("KAJ0" + 7);
					row.put(destination.getName(), 102.12);
					destination = pool.getSection("KAJ0" + 6);
					row.put(destination.getName(), 82.03);
					destination = pool.getSection("KAJ0" + 5);
					row.put(destination.getName(), 61.99);
					destination = pool.getSection("KAJ0" + 4);
					row.put(destination.getName(), 42.02);
					destination = pool.getSection("KAJ0" + 3);
					row.put(destination.getName(), 19.97);
				}
				// Robot section
				{
					base = 105.53; // the distance between KAJ02 and Robot
					row.put("Robot", base);
				}
				matrix.put(source.getName(), row);
			}
		}
		// Source KAJ03 section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("KAJ03"));
			{
				Map<String, Double> row = new HashMap<String, Double>();
				// Conveyor section
				{
					base = 75.84; // the distance between KAJ01 and ConveyorLeft
					row.put("ConveyorLeft", base);

					base = 99.30; // the distance between KAJ01 and ConveyorRight
					row.put("ConveyorRight", base);
				}
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					base = 26.30;
					for (j = 1; j <= 8; ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
					for (j = 9; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 26.3; // distance between KAJ03 and B(21,22)
					for (j = 1; j <= 21; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base
									+ (83 - sectionBMap.get(destination.getName() + "0" + j)) + destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + (83 - sectionBMap.get(destination.getName() + "" + j))
									+ destination.getHeight());
					}
					base = 32.8; // distance between KAJ03 and B22
					for (j = 22; j <= 29; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_3() * (j - 22)
								+ destination.getHeight());

					row.put(destination.getName() + "" + 30, 55.64 + destination.getHeight());
					row.put(destination.getName() + "" + 31, 52.64 + destination.getHeight());

					base = 52.5; // distance between KAJ03 and B32
					for (j = 32; j <= 42; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_4() * (j - 32)
								+ destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 116.27;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					fillRowF(source, destination, 102.76, 119.01, 113.8, row);
				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 49.62;
					for (j = 1, k = 8; j <= destination.getNumber(); ++j, --k) {
						row.put(destination.getName() + "0" + k, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
				}
				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 118.02;
					for (j = 0; j < destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * j + destination.getHeight());
					}
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 69.95;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
						else
							row.put("K" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 48.78;
					for (j = 1; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
				}
				// LU section
				{
					Section destination = pool.getSection("LU");
					for (j = 1; j <= 4; ++j)
						row.put(destination.getName() + "0" + j, 146.19);
					for (j = 6; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, 211.32);
					row.put(destination.getName() + "" + 10, 211.32);
				}
				regroupKAJ(source, row);
			}
			{
				// KAJ section
				{
					row = new HashMap<String, Double>();
					Section destination;
					destination = pool.getSection("KAJ0" + 7);
					row.put(destination.getName(), 80.16);
					destination = pool.getSection("KAJ0" + 6);
					row.put(destination.getName(), 60.07);
					destination = pool.getSection("KAJ0" + 5);
					row.put(destination.getName(), 40.02);
					destination = pool.getSection("KAJ0" + 4);
					row.put(destination.getName(), 20.05);
				}
				// Robot section
				{
					base = 127.06; // the distance between KAJ03 and Robot
					row.put("Robot", base);
				}
				matrix.put(source.getName(), row);
			}
		}
		// Source KAJ04 section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("KAJ04"));
			{
				Map<String, Double> row = new HashMap<String, Double>();
				// Conveyor section
				{
					base = 95.9; // the distance between KAJ01 and ConveyorLeft
					row.put("ConveyorLeft", base);

					base = 79.27; // the distance between KAJ01 and ConveyorRight
					row.put("ConveyorRight", base);
				}
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					base = 26.25;
					for (j = 1; j <= 8; ++j) {
						row.put(destination.getName() + "0" + j, base + 1.59 + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
					for (j = 9; j <= 18; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
					}
					base = 47.76;
					for (j = 19; j <= 21; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 2.58 + destination.getWidth() * (j - 19)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 2.58 + destination.getWidth() * (j - 19)
									+ destination.getHeight());
					}
					for (j = 22; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 0.62 + destination.getWidth() * (j - 22)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 0.62 + destination.getWidth() * (j - 22)
									+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 47.76; // distance between KAJ04 and B(10,11)
					for (j = 1; j <= 10; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "0" + j)) + destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "" + j)) + destination.getHeight());
					}
					row.put(destination.getName() + "" + 11, 52.76 + destination.getHeight());
					row.put(destination.getName() + "" + 12, 55.76 + destination.getHeight());
					base = 32.75; // distance between KAJ04 and B21
					for (j = 13; j <= 21; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_3() * (21 - j)
								+ destination.getHeight());

					base = 32.75; // distance between KAJ04 and B22
					for (j = 22; j <= 31; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_4() * (j - 22)
								+ destination.getHeight());

					base = 72.57; // distance between KAJ04 and B32
					for (j = 32; j <= 42; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_4() * (j - 32)
								+ destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 96.22;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					fillRowF(source, destination, 82.70, 98.97, 93.76, row);
				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 69.67;
					for (j = 1, k = 8; j <= destination.getNumber(); ++j, --k) {
						row.put(destination.getName() + "0" + k, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
				}
				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 138.07;
					for (j = 0; j < destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * j + destination.getHeight());
					}
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 90;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
						else
							row.put("K" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 68.83;
					for (j = 1; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
				}
				// LU section
				{
					Section destination = pool.getSection("LU");
					for (j = 1; j <= 4; ++j)
						row.put(destination.getName() + "0" + j, 166.25);
					for (j = 6; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, 191.26);
					row.put(destination.getName() + "" + 10, 191.26);
				}
				regroupKAJ(source, row);
			}
			{
				// KAJ section
				{
					row = new HashMap<String, Double>();
					Section destination;
					destination = pool.getSection("KAJ0" + 7);
					row.put(destination.getName(), 60.1);
					destination = pool.getSection("KAJ0" + 6);
					row.put(destination.getName(), 40.01);
					destination = pool.getSection("KAJ0" + 5);
					row.put(destination.getName(), 19.97);
				}
				// Robot section
				{
					base = 107.02; // the distance between KAJ04 and Robot
					row.put("Robot", base);
				}
				matrix.put(source.getName(), row);
			}
		}
		// Source KAJ05 section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("KAJ05"));
			{
				Map<String, Double> row = new HashMap<String, Double>();
				// Conveyor section
				{
					base = 115.87; // the distance between KAJ01 and ConveyorLeft
					row.put("ConveyorLeft", base);

					base = 59.29; // the distance between KAJ01 and ConveyorRight
					row.put("ConveyorRight", base);
				}
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					base = 46.22; //  the distance between KAJ05 and A(8,9)
					for (j = 1; j <= 8; ++j) {
						row.put(destination.getName() + "0" + j, base + 1.59 + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
					for (j = 9; j <= 13; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 1.54 + destination.getWidth() * (j - 9)
									+ destination.getHeight());
					}
					base = 27.79; // the distance between KAJ05 and A(21,22)
					for (j = 14; j <= 21; ++j) {
						row.put(destination.getName() + "" + j, base + 2.50 + destination.getWidth() * (21 - j)
								+ destination.getHeight());
					}
					for (j = 22; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "" + j, base + 0.62 + destination.getWidth() * (j - 22)
								+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 27.79; // distance between KAJ05 and B(10,11)
					for (j = 1; j <= 10; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "0" + j)) + destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "" + j)) + destination.getHeight());
					}
					base = 32.78; // distance between KAJ05 and B11
					for (j = 11; j <= 19; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_3() * (j - 11)
								+ destination.getHeight());

					row.put(destination.getName() + "" + 20, 55.72 + destination.getHeight());
					row.put(destination.getName() + "" + 21, 52.72 + destination.getHeight());
					base = 52.72; // distance between KAJ05 and B22
					for (j = 22; j <= 31; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_4() * (j - 22)
								+ destination.getHeight());

					base = 92.53; // distance between KAJ05 and B32
					for (j = 32; j <= 42; ++j)
						row.put(destination.getName() + "" + j, base + destination.getWidth_4() * (j - 32)
								+ destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 76.25;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					fillRowF(source, destination, 62.74, 78.99, 73.79, row);
				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 89.64;
					for (j = 1, k = 8; j <= destination.getNumber(); ++j, --k) {
						row.put(destination.getName() + "0" + k, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
				}
				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 158.04;
					for (j = 0; j < destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * j + destination.getHeight());
					}
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 109.97;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
						else
							row.put("K" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 88.8;
					for (j = 1; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
				}
				// LU section
				{
					Section destination = pool.getSection("LU");
					for (j = 1; j <= 4; ++j)
						row.put(destination.getName() + "0" + j, 186.21);
					for (j = 6; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, 171.29);
					row.put(destination.getName() + "" + 10, 171.29);
				}
				regroupKAJ(source, row);
			}
			{
				// KAJ section
				{
					row = new HashMap<String, Double>();
					Section destination;
					destination = pool.getSection("KAJ0" + 7);
					row.put(destination.getName(), 40.14);
					destination = pool.getSection("KAJ0" + 6);
					row.put(destination.getName(), 20.05);
				}
				// Robot section
				{
					base = 87.04; // the distance between KAJ05 and Robot
					row.put("Robot", base);
				}
				matrix.put(source.getName(), row);
			}
		}
		// Source KAJ06 section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("KAJ06"));
			{
				Map<String, Double> row = new HashMap<String, Double>();
				// Conveyor section
				{
					base = 135.91; // the distance between KAJ01 and ConveyorLeft
					row.put("ConveyorLeft", base);

					base = 56.3; // the distance between KAJ01 and ConveyorRight
					row.put("ConveyorRight", base);
				}
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					base = 24.78;
					for (j = 1; j <= 21; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 2.50 + destination.getWidth() * (21 - j)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 2.50 + destination.getWidth() * (21 - j)
									+ destination.getHeight());
					}
					for (j = 22; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 0.62 + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 0.62 + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 24.78; // distance between KAJ06 and B(10,11)
					for (j = 1; j <= 10; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "0" + j)) + destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "" + j)) + destination.getHeight());
					}
					for (j = 11; j <= 42; ++j)
						row.put(destination.getName() + "" + j, base + (sectionBMap.get(destination.getName() + "" + j) - 41.5)
								+ destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 73.23;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					fillRowF(source, destination, 42.69, 75.99, 70.78, row);
				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 109.68;
					for (j = 1, k = 8; j <= destination.getNumber(); ++j, --k) {
						row.put(destination.getName() + "0" + k, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
				}
				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 178.08;
					for (j = 0; j < destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * j + destination.getHeight());
					}
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 130.01;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
						else
							row.put("K" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 108.84;
					for (j = 1; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
				}
				// LU section
				{
					Section destination = pool.getSection("LU");
					for (j = 1; j <= 4; ++j)
						row.put(destination.getName() + "0" + j, 206.26);
					for (j = 6; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, 168.28);
					row.put(destination.getName() + "" + 10, 168.28);
				}
				regroupKAJ(source, row);
			}
			{
				// KAJ section
				{
					row = new HashMap<String, Double>();
					Section destination;
					destination = pool.getSection("KAJ0" + 7);
					row.put(destination.getName(), 20.09);
				}
				// Robot section
				{
					base = 84.04; // the distance between KAJ06 and Robot
					row.put("Robot", base);
				}
				matrix.put(source.getName(), row);
			}
		}
		// Source KAJ07 section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("KAJ07"));
			{
				Map<String, Double> row = new HashMap<String, Double>();
				// Conveyor section
				{
					base = 155.96; // the distance between KAJ01 and ConveyorLeft
					row.put("ConveyorLeft", base);

					base = 76.39; // the distance between KAJ01 and ConveyorRight
					row.put("ConveyorRight", base);
				}
				// A section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("A"));
					base = 44.86;
					for (j = 1; j <= 21; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 2.50 + destination.getWidth() * (21 - j)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 2.50 + destination.getWidth() * (21 - j)
									+ destination.getHeight());
					}
					for (j = 22; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + 0.62 + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + 0.62 + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// B section
				{
					SectionB destination = ((SectionB) pool.getSection("B"));
					base = 44.87; // distance between KAJ07 and B(10,11)
					for (j = 1; j <= 10; ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "0" + j)) + destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base
									+ (41.5 - sectionBMap.get(destination.getName() + "" + j)) + destination.getHeight());
					}
					for (j = 11; j <= 42; ++j)
						row.put(destination.getName() + "" + j, base + (sectionBMap.get(destination.getName() + "" + j) - 41.5)
								+ destination.getHeight());
				}
				// E section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("E"));
					base = 93.32;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put(destination.getName() + "0" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
						else
							row.put(destination.getName() + "" + j, base + destination.getWidth() * (j - 1)
									+ destination.getHeight());
					}
				}
				// F section
				{
					SectionF destination = ((SectionF) pool.getSection("F"));
					fillRowF(source, destination, 22.60, 96.07, 90.87, row);
				}
				// G section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("G"));
					base = 129.77;
					for (j = 1, k = 8; j <= destination.getNumber(); ++j, --k) {
						row.put(destination.getName() + "0" + k, base + destination.getWidth() * (j - 1)
								+ destination.getHeight());
					}
				}
				// H section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("H"));
					base = 198.17;
					for (j = 0; j < destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * j + destination.getHeight());
					}
				}
				// K section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("K"));
					base = 150.10;
					for (j = 1; j <= destination.getNumber(); ++j) {
						if (j <= 9)
							row.put("K0" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
						else
							row.put("K" + j, base + destination.getWidth() * (j - 1) + destination.getHeight());
					}
				}
				// M section
				{
					SectionSimple destination = ((SectionSimple) pool.getSection("M"));
					base = 128.93;
					for (j = 1; j <= destination.getNumber(); ++j) {
						row.put(destination.getName() + "0" + j, base + destination.getWidth() * (8 - j)
								+ destination.getHeight());
					}
				}
				// LU section
				{
					Section destination = pool.getSection("LU");
					for (j = 1; j <= 4; ++j)
						row.put(destination.getName() + "0" + j, 226.35);
					for (j = 6; j <= 9; ++j)
						row.put(destination.getName() + "0" + j, 188.37);
					row.put(destination.getName() + "" + 10, 188.37);
				}
				regroupKAJ(source, row);
			}
			{
				// KAJ section
				{
					row = new HashMap<String, Double>();
				}
				// Robot section
				{
					base = 106.13; // the distance between KAJ07 and Robot
					row.put("Robot", base);
				}
				matrix.put(source.getName(), row);
			}
		}
		// Source Robot section
		{
			SectionSimple source = ((SectionSimple) pool.getSection("Robot"));
			// Conveyor section
			{
				base = 170.42; // the distance between Robot and ConveyorLeft
				matrix.get("ConveyorLeft").put("Robot", base);

				base = 27.75; // the distance between Robot and ConveyorRight
				matrix.get("ConveyorRight").put("Robot", base);
			}
		// B section
			// This was in B-Robot section, and it transfered here for simplifying reason
			{
				Map<String, Double> row = new HashMap<String, Double>();
				SectionB destination = ((SectionB) pool.getSection("B"));
				base = 17.75; // the distance between Robot and B01
				for (j = 1; j <= 9; ++j)
					row.put(destination.getName() + "0" + j, base + sectionBMap.get(destination.getName() + "0" + j)
							+ source.getHeight() + destination.getHeight());
				for (j = 10; j <= 42; ++j)
					row.put(destination.getName() + "" + j, base + sectionBMap.get(destination.getName() + "" + j)
							+ source.getHeight() + destination.getHeight());

				// Now this row is filled out, and this row is from Robot to all the B cells
				// we need to distribute these values to the corresponding rows
				regroupRobot(source, destination, row);
			}
			// E section
			// This was in E-Robot section, and it transfered here for simplifying reason
			{
				Map<String, Double> row = new HashMap<String, Double>();
				SectionSimple destination = ((SectionSimple)pool.getSection("E"));
				for (j=1;j<=5;++j)
					row.put(destination.getName() + "0" + j, destination.getWidth() * (5-j)+destination.getHeight() );
				for (j=6;j<=9;++j)
					row.put(destination.getName() + "0" + j, destination.getWidth() * (j-5)+destination.getHeight() );
				for (j=10;j<=destination.getNumber();++j)
					row.put(destination.getName() + "" + j, destination.getWidth() * (j-5)+destination.getHeight());

				// Now this row is filled out, and this row is from Robot to all the B cells
				// we need to distribute these values to the corresponding rows
				regroupRobot(source, destination, row);
			}
			// F section
			// This was in F-Robot section, and it transfered here for simplifying reason
			{
				Map<String, Double> row = new HashMap<String, Double>();
				SectionF destination = ((SectionF) pool.getSection("F"));
				fillRowF(source,destination, 126.72,8.05,13.25, row);

				// Now this row is filled out, and this row is from Robot to all the B cells
				// we need to distribute these values to the corresponding rows
				regroupRobot(source, destination, row);
			}
			// K section
			// This was in K-Robot section, and it transfered here for simplifying reason
			{
				Map<String, Double> row = new HashMap<String, Double>();
				SectionSimple destination = ((SectionSimple) pool.getSection("K"));
				base = 180.75;
				for (j = 7, k = 1; j <= 28; ++j, ++k) {
					if (j <= 9)
						row.put("K0" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
					else
						row.put("K" + j, base + 2.55 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
				}

				for (j = 6, k = 1; j >= 1; --j, ++k) {
					if (j <= 9)
						row.put("K0" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
					else
						row.put("K" + j, base + 0.64 + source.getHeight() + destination.getWidth() * (k - 1)
								+ destination.getHeight());
				}
				regroupRobot(source, destination, row);
			}
			// LU section
			// This was in LU-Robot section, and it transfered here for simplifying reason
			{
				Map<String, Double> row = new HashMap<String, Double>();
				Section destination = pool.getSection("LU");
				fillRowLU(source, destination, 240.68, 84.25, row);
				// Now this row is filled out, and this row is from Robot to all the LU cells
				// we need to distribute these values to the corresponding rows
				for (j = 1; j <= 4; ++j)
					matrix.get(destination.getName() + "0" + j).put(source.getName(), row.get(destination.getName() + "0" + j));
				for (j = 6; j <= 9; ++j)
					matrix.get(destination.getName() + "0" + j).put(source.getName(), row.get(destination.getName() + "0" + j));
				j=10;
				matrix.get(destination.getName() + "" + j).put(source.getName(), row.get(destination.getName() + "" + j));
			}
		}
		// now the matrix is full, we need to convert it to a two dimensions array, result
		// the size of this array is 181 x 181
		// Conveyor x 2
		// A		x 31
		// B		x 42
		// E		x 32
		// F		x 3
		// G		x 8
		// H		x 10
		// K		x 28
		// M		x 8
		// LU		x 9
		// KAJ		x 7
		// Robot 	x 1
		{
			result = new double[181][181];
			Section source, destination;

			// Source ConveyorLeft
			source = pool.getSection("ConveyorLeft");
			{
				i=0; // indicates which row we are on
				j=180;
				row = matrix.get(source.getName());

				// Robot section
				fillResultSingle(pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle(destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU(destination);

				// M section
				destination = pool.getSection("M");
				fillResult(destination,8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResult( destination, 9);

				// G section
				destination = pool.getSection("G");
				fillResult( destination, 8);

				// F section
				destination = pool.getSection("F");
				fillResult( destination, 3);

				// E section
				destination = pool.getSection("E");
				fillResult( destination, 32);

				// B section
				destination = pool.getSection("B");
				fillResult( destination, 42);

				// A section
				destination = pool.getSection("A");
				fillResult( destination, 31);
			}

			// Source ConveyorRight
			source =  pool.getSection("ConveyorRight");
			{
				i=1;
				j=180;
				row = matrix.get(source.getName());

				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle(destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResult( destination, 9);

				// G section
				destination = pool.getSection("G");
				fillResult( destination, 8);

				// F section
				destination = pool.getSection("F");
				fillResult( destination, 3);

				// E section
				destination = pool.getSection("E");
				fillResult( destination, 32);

				// B section
				destination = pool.getSection("B");
				fillResult( destination, 42);

				// A section
				destination = pool.getSection("A");
				fillResult( destination, 31);

			}
			// Source A section
			source =  pool.getSection("A");
			for (k = 1; k <= source.getNumber(); ++k) {
				i++;
				j = 180;
				if (k <= 9)
					row = matrix.get(source.getName() + "0" + k);
				else
					row = matrix.get(source.getName() + "" + k);

				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle(destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResult( destination, 9);

				// G section
				destination = pool.getSection("G");
				fillResult( destination, 8);

				// F section
				destination = pool.getSection("F");
				fillResult( destination, 3);

				// E section
				destination = pool.getSection("E");
				fillResult( destination, 32);

				// B section
				destination = pool.getSection("B");
				fillResult( destination, 42);

				// A section
				destination = pool.getSection("A");
				fillResultSelf(destination, 31);

				
			}
			// Source B section
			source = pool.getSection("B");
			for (k = 1; k <= source.getNumber(); ++k) {
				i++;
				j = 180;
				if (k <= 9)
					row = matrix.get(source.getName() + "0" + k);
				else
					row = matrix.get(source.getName() + "" + k);

				// Robot section
				fillResultSingle(pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle( destination);
				}
				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResult( destination, 9);

				// G section
				destination = pool.getSection("G");
				fillResult( destination, 8);

				// E section
				destination = pool.getSection("E");
				fillResult( destination, 32);

				// F section
				destination = pool.getSection("F");
				fillResult( destination, 3);

				// B section
				destination = pool.getSection("B");
				fillResultSelf(destination, 42);

			}
			// Source E section
			source =  pool.getSection("E");
			for (k = 1; k <= source.getNumber(); ++k) {
				i++;
				j = 180;
				if (k <= 9)
					row = matrix.get(source.getName() + "0" + k);
				else
					row = matrix.get(source.getName() + "" + k);
				// comment the subsequent line when we want to write the output to
				// Distance Matrix.txt
				
				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle( destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResult( destination, 9);

				// G section
				destination = pool.getSection("G");
				fillResult( destination, 8);

				// F section
				destination = pool.getSection("F");
				fillResult( destination, 3);
				// E section
				destination = pool.getSection("E");
				fillResultSelf(destination, 32);

				
			}
			// Source F section
			source =  pool.getSection("F");
			for (k = 1; k <= source.getNumber(); ++k) {
				i++;
				j = 180;
				row = matrix.get(source.getName() + "0" + k);
				
				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle( destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResult( destination, 9);

				// G section
				destination = pool.getSection("G");
				fillResult( destination, 8);

				// F section
				destination = pool.getSection("F");
				fillResultSelf(destination, 3);

				
			}

			// Source G section
			source =  pool.getSection("G");
			for (k = 1; k <= source.getNumber(); ++k) {
				i++;
				j = 180;
				if (k <= 9)
					row = matrix.get(source.getName() + "0" + k);
				else
					row = matrix.get(source.getName() + "" + k);
				
				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle( destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResult( destination, 9);

				// G section
				destination = pool.getSection("G");
				fillResultSelf(destination, 8);

				
			}
			// Source H section
			source =  pool.getSection("H");
			for (k = 0; k < source.getNumber(); ++k) {
				i++;
				j = 180;
				if (k <= 9)
					row = matrix.get(source.getName() + "0" + k);
				else
					row = matrix.get(source.getName() + "" + k);
				
				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle( destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResult( destination, 28);

				// H section
				destination = pool.getSection("H");
				fillResultSelf(destination, 9);

				
			}

			// Source K Section
			source =  pool.getSection("K");
			for (k = 1; k <= source.getNumber(); ++k) {
				i++;
				j = 180;
				if (k <= 9)
					row = matrix.get(source.getName() + "0" + k);
				else
					row = matrix.get(source.getName() + "" + k);
				
				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (int j = 7; j >= 1; --j) {
					destination = pool.getSection("KAJ0" + j);
					fillResultSingle( destination);
				}
				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);

				// M section
				destination = pool.getSection("M");
				fillResult( destination, 8);

				// K section
				destination = pool.getSection("K");
				fillResultSelf(destination, 28);

				
			}
			// Source M section
			source =  pool.getSection("M");
			for (k = 1; k <= source.getNumber(); ++k) {
				i++;
				j = 180;
				row = matrix.get(source.getName() + "0" + k);
				
				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l >= 1; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle( destination);
				}

				// LU section
				destination = pool.getSection("LU");
				fillResultLU( destination);
				// M section
				destination = pool.getSection("M");
				fillResultSelf(destination, 8);

				
			}

			// Source LU section
			source = pool.getSection("LU");
			for (k = 1; k <= source.getNumber(); ++k) {
				if (k != 5) {
				i++;
				j = 180;
					if (k <= 9)
						row = matrix.get(source.getName() + "0" + k);
					else
						row = matrix.get(source.getName() + "" + k);

					// Robot section
					fillResultSingle( pool.getSection("Robot"));

					// KAJ section
					for (l = 7; l >= 1; --l) {
						destination = pool.getSection("KAJ0" + l);
						fillResultSingle( destination);
					}

					// LU section
					destination = pool.getSection("LU");
					for (j = 10; j > i; --j) {
						if (j != 5) {
							if (j <= 9)
								result[i][j--] = row.get(destination.getName() + "0" + j);
							else
								result[i][j--] = row.get(destination.getName() + "" + j);
						}
					}
				}
			}

			// Source KAJ section
			for (k = 1; k <= 7; ++k) {
				i++;
				j = 180;
				source = pool.getSection("KAJ0" + k);
				row = matrix.get(source.getName());

				// Robot section
				fillResultSingle( pool.getSection("Robot"));

				// KAJ section
				for (l = 7; l > k; --l) {
					destination = pool.getSection("KAJ0" + l);
					fillResultSingle( destination);
				}
			}
		}
	}

	public double[][] getResult() {
		return result;
	}

	/*
	 * we are working on a particular line, which is row, source remain the same we are iterating through destination section,
	 * this method is created for saving code, and its function is limited and can only be applied to continuous section
	 */
	private void fillRow(SectionSimple s, SectionSimple d, double b, int k, Map<String, Double> row) {
		if (k > 1) {
			// this means we are counting from the end
			for (j = 1; j <= d.getNumber(); ++j) {
				if (k <= 9)
					row.put(d.getName() + "0" + k, b + s.getHeight() + d.getWidth() * (j - 1) + d.getHeight());
				else
					row.put(d.getName() + "" + k, b + s.getHeight() + d.getWidth() * (j - 1) + d.getHeight());
				--k;
			}
		} else {
			// this means we are counting from the beginning
			for (j = 1; j <= d.getNumber(); ++j) {
				if (k <= 9)
					row.put(d.getName() + "0" + k, b + s.getHeight() + d.getWidth() * (j - 1) + d.getHeight());
				else
					row.put(d.getName() + "" + k, b + s.getHeight() + d.getWidth() * (j - 1) + d.getHeight());
				++k;
			}
		}
	}

	/*
	 * put all the values corresponding to F in this row, which is determined by the field: row, this method is created because of
	 * the limitation of fillRow, and the uniqueness of Section F
	 */
	private void fillRowF(SectionSimple source, SectionF destination, double a, double b, double c, Map<String, Double> row) {
		row.put(destination.getName() + "01", source.getHeight() + a + destination.getHeight_1());
		row.put(destination.getName() + "02", source.getHeight() + b + destination.getHeight_2());
		row.put(destination.getName() + "03", source.getHeight() + c + destination.getHeight_3());
	}

	/*
	 * put all the values corresponding to LU in this row, which is determined by the field: row, this method is created because
	 * of the limitation of fillRow, and the uniqueness of Section LU
	 */
	private void fillRowLU(Section source, Section destination, double valueLeft, double valueRight, Map<String, Double> row) {
		for (int i = 1; i <= 4; ++i)
			row.put(destination.getName() + "0" + i, valueLeft);
		for (int i = 6; i <= 9; ++i)
			row.put(destination.getName() + "0" + i, valueRight);
		row.put(destination.getName() + "10", valueRight);
	}

	/*
	 * swap the position of source and destination and put the value in the matrix, i is used to determine the real row in matrix
	 * this method is created because of the fact that some sections are likely to be the destination, but in order to make the
	 * matrix look consistent, we need to swap the postion
	 */
	private void regroup(Section source, Section destination, Map<String, Double> row) {
		for (int j = 1; j <= destination.getNumber(); ++j) {
			Map<String, Double> realRow;
			if (j < 10) {
				realRow = matrix.get(destination.getName() + "0" + j);
				if (i <= 9)
					realRow.put(source.getName() + "0" + i, row.get(destination.getName() + "0" + j));
				else
					realRow.put(source.getName() + "" + i, row.get(destination.getName() + "0" + j));
			} else {
				realRow = matrix.get(destination.getName() + "" + j);
				if (i <= 9)
					realRow.put(source.getName() + "0" + i, row.get(destination.getName() + "" + j));
				else
					realRow.put(source.getName() + "" + i, row.get(destination.getName() + "" + j));
			}
		}
	}

	private void regroupRobot(Section source, Section destination, Map<String, Double> row) {
		for (int j = 1; j <= destination.getNumber(); ++j) {
			Map<String, Double> realRow;
			if (j < 10) {
				realRow = matrix.get(destination.getName() + "0" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
			} else {
				realRow = matrix.get(destination.getName() + "" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "" + j));
			}
		}
	}

	/*
	 * distribute values targeting all other cells from KAJ to their own row this method is not really necessary, since the
	 * function could be equally achieved by using regroup
	 */
	private void regroupKAJ(Section source, Map<String, Double> row) {
		Map<String, Double> realRow;
		Section destination;
		// Conveyor section
		destination = pool.getSection("ConveyorLeft");
		matrix.get(destination.getName()).put(source.getName(), row.get(destination.getName()));
		destination = pool.getSection("ConveyorRight");
		matrix.get(destination.getName()).put(source.getName(), row.get(destination.getName()));
		// A section
		destination = pool.getSection("A");
		for (int j = 1; j <= destination.getNumber(); ++j) {
			if (j <= 9) {
				realRow = matrix.get(destination.getName() + "0" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
			} else {
				realRow = matrix.get(destination.getName() + "" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "" + j));
			}
		}
		// B section
		destination = pool.getSection("B");
		for (int j = 1; j <= destination.getNumber(); ++j) {
			if (j <= 9) {
				realRow = matrix.get(destination.getName() + "0" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
			} else {
				realRow = matrix.get(destination.getName() + "" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "" + j));
			}
		}
		// E section
		destination = pool.getSection("E");
		for (int j = 1; j <= destination.getNumber(); ++j) {
			if (j <= 9) {
				realRow = matrix.get(destination.getName() + "0" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
			} else {
				realRow = matrix.get(destination.getName() + "" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "" + j));
			}
		}
		// F section
		destination = pool.getSection("F");
		for (int j = 1; j <= destination.getNumber(); ++j) {
			realRow = matrix.get(destination.getName() + "0" + j);
			realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
		}
		// G section
		destination = pool.getSection("G");
		for (int j = 1; j <= destination.getNumber(); ++j) {
			realRow = matrix.get(destination.getName() + "0" + j);
			realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
		}
		// H section
		destination = pool.getSection("H");
		for (int j = 0; j < destination.getNumber(); ++j) {
			realRow = matrix.get(destination.getName() + "0" + j);
			realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
		}
		// K section
		destination = pool.getSection("K");
		for (int j = 1; j <= destination.getNumber(); ++j) {
			if (j <= 9) {
				realRow = matrix.get(destination.getName() + "0" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
			} else {
				realRow = matrix.get(destination.getName() + "" + j);
				realRow.put(source.getName(), row.get(destination.getName() + "" + j));
			}
		}
		// M section
		destination = pool.getSection("M");
		for (int j = 1; j <= destination.getNumber(); ++j) {
			realRow = matrix.get(destination.getName() + "0" + j);
			realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
		}
		// LU section
		destination = pool.getSection("LU");
		for (int j = 1; j <= 4; ++j) {
			realRow = matrix.get(destination.getName() + "0" + j);
			realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
		}
		for (int j = 6; j <= 9; ++j) {
			realRow = matrix.get(destination.getName() + "0" + j);
			realRow.put(source.getName(), row.get(destination.getName() + "0" + j));
		}
		realRow = matrix.get(destination.getName() + "" + 10);
		realRow.put(source.getName(), row.get(destination.getName() + "" + 10));
	}

	public String toString() {
		// the type of source and destination could be abstracted to Section, for the exact type is not necessary
		Section source;
		Section destination;
		StringBuilder sb = new StringBuilder();
		// Source ConveyorLeft
		source = pool.getSection("ConveyorLeft");
		{
			row = matrix.get(source.getName());
			// comment the subsequent line when we want to write the output to Distance Matrix.txt
			sb.append(source.getName() + " size :" + row.size() + " ");

			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));

			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}

			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillString(sb, destination, 9);

			// G section
			destination = pool.getSection("G");
			fillString(sb, destination, 8);

			// F section
			destination = pool.getSection("F");
			fillString(sb, destination, 3);

			// E section
			destination = pool.getSection("E");
			fillString(sb, destination, 32);

			// B section
			destination = pool.getSection("B");
			fillString(sb, destination, 42);

			// A section
			destination = pool.getSection("A");
			fillString(sb, destination, 31);

			sb.append("\n");
		}

		// Source ConveyorRight
		source =  pool.getSection("ConveyorRight");
		{
			row = matrix.get(source.getName());

			// comment the subsequent line when we want to write the output to Distance Matrix.txt
			sb.append(source.getName() + " size :" + row.size() + " ");

			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));

			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}

			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillString(sb, destination, 9);

			// G section
			destination = pool.getSection("G");
			fillString(sb, destination, 8);

			// F section
			destination = pool.getSection("F");
			fillString(sb, destination, 3);

			// E section
			destination = pool.getSection("E");
			fillString(sb, destination, 32);

			// B section
			destination = pool.getSection("B");
			fillString(sb, destination, 42);

			// A section
			destination = pool.getSection("A");
			fillString(sb, destination, 31);

			sb.append("\n");
		}
		// Source A section
		source =  pool.getSection("A");
		for (i = 1; i <= source.getNumber(); ++i) {
			if (i <= 9)
				row = matrix.get(source.getName() + "0" + i);
			else
				row = matrix.get(source.getName() + "" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");

			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));

			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}

			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillString(sb, destination, 9);

			// G section
			destination = pool.getSection("G");
			fillString(sb, destination, 8);

			// F section
			destination = pool.getSection("F");
			fillString(sb, destination, 3);

			// E section
			destination = pool.getSection("E");
			fillString(sb, destination, 32);

			// B section
			destination = pool.getSection("B");
			fillString(sb, destination, 42);

			// A section
			destination = pool.getSection("A");
			fillStringSelf(sb, destination, 31);

			sb.append("\n");
		}
		// Source B section
		source = pool.getSection("B");
		for (i = 1; i <= source.getNumber(); ++i) {
			if (i <= 9)
				row = matrix.get(source.getName() + "0" + i);
			else
				row = matrix.get(source.getName() + "" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");

			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillString(sb, destination, 9);

			// G section
			destination = pool.getSection("G");
			fillString(sb, destination, 8);

			// E section
			destination = pool.getSection("E");
			fillString(sb, destination, 32);

			// F section
			destination = pool.getSection("F");
			fillString(sb, destination, 3);

			// B section
			destination = pool.getSection("B");
			fillStringSelf(sb, destination, 42);

			sb.append("\n");
		}
		// Source E section
		source =  pool.getSection("E");
		for (i = 1; i <= source.getNumber(); ++i) {
			if (i <= 9)
				row = matrix.get(source.getName() + "0" + i);
			else
				row = matrix.get(source.getName() + "" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");
			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillString(sb, destination, 9);

			// G section
			destination = pool.getSection("G");
			fillString(sb, destination, 8);

			// F section
			destination = pool.getSection("F");
			fillString(sb, destination, 3);
			// E section
			destination = pool.getSection("E");
			fillStringSelf(sb, destination, 32);

			sb.append("\n");
		}
		// Source F section
		source =  pool.getSection("F");
		for (i = 1; i <= source.getNumber(); ++i) {
			row = matrix.get(source.getName() + "0" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");
			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillString(sb, destination, 9);

			// G section
			destination = pool.getSection("G");
			fillString(sb, destination, 8);

			// F section
			destination = pool.getSection("F");
			fillStringSelf(sb, destination, 3);

			sb.append("\n");
		}

		// Source G section
		source =  pool.getSection("G");
		for (i = 1; i <= source.getNumber(); ++i) {
			if (i <= 9)
				row = matrix.get(source.getName() + "0" + i);
			else
				row = matrix.get(source.getName() + "" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");
			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillString(sb, destination, 9);

			// G section
			destination = pool.getSection("G");
			fillStringSelf(sb, destination, 8);

			sb.append("\n");
		}
		// Source H section
		source =  pool.getSection("H");
		for (i = 0; i < source.getNumber(); ++i) {
			if (i <= 9)
				row = matrix.get(source.getName() + "0" + i);
			else
				row = matrix.get(source.getName() + "" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");
			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillString(sb, destination, 28);

			// H section
			destination = pool.getSection("H");
			fillStringSelf(sb, destination, 9);

			sb.append("\n");
		}

		// Source K Section
		source =  pool.getSection("K");
		for (i = 1; i <= source.getNumber(); ++i) {
			if (i <= 9)
				row = matrix.get(source.getName() + "0" + i);
			else
				row = matrix.get(source.getName() + "" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");
			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);

			// M section
			destination = pool.getSection("M");
			fillString(sb, destination, 8);

			// K section
			destination = pool.getSection("K");
			fillStringSelf(sb, destination, 28);

			sb.append("\n");
		}
		// Source M section
		source =  pool.getSection("M");
		for (i = 1; i <= source.getNumber(); ++i) {
			row = matrix.get(source.getName() + "0" + i);
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + "" + i + " size :" + row.size() + " ");
			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (int j = 7; j >= 1; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			// LU section
			destination = pool.getSection("LU");
			fillStringLU(sb, destination);
			// M section
			destination = pool.getSection("M");
			fillStringSelf(sb, destination, 8);

			sb.append("\n");
		}
		// Source LU section
		source = pool.getSection("LU");
		for (i = 1; i <= source.getNumber(); ++i) {
			if (i != 5) {
				if (i <= 9)
					row = matrix.get(source.getName() + "0" + i);
				else
					row = matrix.get(source.getName() + "" + i);
				// comment the subsequent line when we want to write the output to Distance Matrix.txt
				sb.append(source.getName() + "" + i + " size :" + row.size() + " ");
				// Robot section
				fillStringRobot(sb, pool.getSection("Robot"));
				// KAJ section
				for (int j = 7; j >= 1; --j) {
					destination = pool.getSection("KAJ0" + j);
					fillStringKAJ(sb, destination);
				}
				// LU section
				destination = pool.getSection("LU");
				for (j = 10; j > i; --j) {
					if (j != 5) {
						if (j <= 9)
							sb.append(destination.getName() + "0" + j + ":" + row.get(destination.getName() + "0" + j));
						// sb.append(row.get(destination.getName() + "0" + j));
						else
							sb.append(destination.getName() + "" + j + ":" + row.get(destination.getName() + "" + j));
						// sb.append(row.get(destination.getName() + "" + j));
					}
				}

				sb.append("\n");
			}
		}
		// Source KAJ section
		for (i = 1; i <= 7; ++i) {
			source = pool.getSection("KAJ0" + i);
			row = matrix.get(source.getName());
			// comment the subsequent line when we want to write the output to
			// Distance Matrix.txt
			sb.append(source.getName() + " size :" + row.size() + " ");
			// Robot section
			fillStringRobot(sb, pool.getSection("Robot"));
			// KAJ section
			for (j = 7; j > i; --j) {
				destination = pool.getSection("KAJ0" + j);
				fillStringKAJ(sb, destination);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	private void fillString(StringBuilder sb, Section d, int initial) {
		for (j = initial, k = 1; k <= d.getNumber(); --j, ++k)
			if (j <= 9) {
				sb.append(d.getName() + "0" + j + ":" + row.get(d.getName() + "0" + j) + ", ");
				// sb.append(row.get(d.getName() + "0" + j) + ", ");
			} else {
				sb.append(d.getName() + "" + j + ":" + row.get(d.getName() + "" + j) + ", ");
				// sb.append(+ row.get(d.getName() + "" + j)+ ", ");
			}
	}

	private void fillResult(Section d, int initial) {
		int k;
		for (k = 1; k <= d.getNumber(); --initial, ++k)
			if (initial <= 9) {
				result[i][j--] = row.get(d.getName() + "0" + initial);
			} else {
				result[i][j--] = row.get(d.getName() + "" + initial);
			}
	}

	private void fillStringRobot(StringBuilder sb, Section d) {
		sb.append(d.getName() + ":" + row.get(d.getName()) + ",");
		// sb.append(row.get(d.getName()) + ",");
	}

	private void fillResultSingle(Section d) {
		result[i][j--] = row.get(d.getName());
	}

	private void fillStringKAJ(StringBuilder sb, Section d) {
		sb.append(d.getName() + ":" + row.get(d.getName()) + ",");
		// sb.append(row.get(d.getName()) + ",");
	}

	private void fillStringLU(StringBuilder sb, Section d) {
		sb.append(d.getName() + "" + 10 + ":" + row.get(d.getName() + "" + 10) + ", ");
		for (j = 9; j >= 6; --j) {
			sb.append(d.getName() + "0" + j + ":" + row.get(d.getName() + "0" + j) + ", ");
		}
		for (j = 4; j >= 1; --j) {
			sb.append(d.getName() + "0" + j + ":" + row.get(d.getName() + "0" + j) + ", ");
		}
	}

	private void fillResultLU(Section d) {
		int k;
		result[i][j--] = row.get(d.getName() + "" + 10);
		for (k = 9; k >= 6; --k) {
			result[i][j--] = row.get(d.getName() + "0" + k);
		}
		for (k = 4; k >= 1; --k) {
			result[i][j--] = row.get(d.getName() + "0" + k);
		}
	}

	private void fillStringSelf(StringBuilder sb, Section d, int initial) {
		for (j = initial, k = 1; k <= d.getNumber(); --j, ++k) {
			if (j == i)
				break;
			if (j > 9)
				sb.append(d.getName() + "" + j + ":" + row.get(d.getName() + "" + j) + ", ");
			// sb.append(row.get(destination.getName() + "" + j) + ", ");
			else
				sb.append(d.getName() + "0" + j + ":" + row.get(d.getName() + "0" + j) + ", ");
			// sb.append(row.get(destination.getName() + "0" + j) + ", ");
		}

	}

	private void fillResultSelf(Section d, int initial) {
		int k;
		for (k = 1; k <= d.getNumber(); --initial, ++k) {
			if (i == j)
				break;
			if (initial > 9){
				/*
				if(row.get(d.getName() + "" + initial) == null){
					System.out.println(i);
					System.out.println(j);
					System.out.println(initial);
					System.exit(1);
				}
				*/
				result[i][j--] = row.get(d.getName() + "" + initial);
			}
			else{
				result[i][j--] = row.get(d.getName() + "0" + initial);
			}
		}

	}

	public static void main(String[] args) {
		DistanceMatrix matrix = new DistanceMatrix();
		double[][] result = matrix.getResult();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("Matrix.txt"));
			for (int i = 0; i < result.length; ++i) {
				for ( int j = 0; j< result[i].length; ++j) {
					out.write(result[i][j]+",");
				}
				out.write("\n");
			}
			// out.write(matrix.toString());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(matrix.matrix.size());
	}
}
