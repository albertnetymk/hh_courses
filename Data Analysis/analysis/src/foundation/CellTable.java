package foundation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CellTable {
	private Map<String, Cell> table;

	public Cell getCell(String key) {
		return table.get(key);
	}

	public Set<String> getKeySet() {
		return table.keySet();
	}

	public CellTable(UnitTable env) {
		table = new HashMap<String, Cell>();
		Cell cell;
		cell = new Cell("A01");
		table.put(cell.getName(), cell);
		cell = new Cell("A02");
		table.put(cell.getName(), cell);
		cell = new Cell("A03");
		table.put(cell.getName(), cell);
		cell = new Cell("A04");
		table.put(cell.getName(), cell);
		cell = new Cell("A05");
		table.put(cell.getName(), cell);
		cell = new Cell("A06");
		table.put(cell.getName(), cell);
		cell = new Cell("A07");
		table.put(cell.getName(), cell);
		cell = new Cell("A08");
		table.put(cell.getName(), cell);
		cell = new Cell("A09");
		table.put(cell.getName(), cell);
		cell = new Cell("A10");
		table.put(cell.getName(), cell);
		cell = new Cell("A11");
		table.put(cell.getName(), cell);
		cell = new Cell("A12");
		table.put(cell.getName(), cell);
		cell = new Cell("A13");
		table.put(cell.getName(), cell);
		cell = new Cell("A14");
		table.put(cell.getName(), cell);
		cell = new Cell("A15");
		table.put(cell.getName(), cell);
		cell = new Cell("A16");
		table.put(cell.getName(), cell);
		cell = new Cell("A17");
		table.put(cell.getName(), cell);
		cell = new Cell("A18");
		table.put(cell.getName(), cell);
		cell = new Cell("A19");
		table.put(cell.getName(), cell);
		cell = new Cell("A20");
		table.put(cell.getName(), cell);
		cell = new Cell("A21");
		table.put(cell.getName(), cell);
		cell = new Cell("A22");
		table.put(cell.getName(), cell);
		cell = new Cell("A23");
		table.put(cell.getName(), cell);
		cell = new Cell("A24");
		table.put(cell.getName(), cell);
		cell = new Cell("A25");
		table.put(cell.getName(), cell);
		cell = new Cell("A26");
		table.put(cell.getName(), cell);
		cell = new Cell("A27");
		table.put(cell.getName(), cell);
		cell = new Cell("A28");
		table.put(cell.getName(), cell);
		cell = new Cell("A29");
		table.put(cell.getName(), cell);
		cell = new Cell("A30");
		table.put(cell.getName(), cell);
		cell = new Cell("A31");
		table.put(cell.getName(), cell);

		cell = new Cell("B01");
		table.put(cell.getName(), cell);
		cell = new Cell("B02");
		table.put(cell.getName(), cell);
		cell = new Cell("B03");
		table.put(cell.getName(), cell);
		cell = new Cell("B04");
		table.put(cell.getName(), cell);
		cell = new Cell("B05");
		table.put(cell.getName(), cell);
		cell = new Cell("B06");
		table.put(cell.getName(), cell);
		cell = new Cell("B07");
		table.put(cell.getName(), cell);
		cell = new Cell("B08");
		table.put(cell.getName(), cell);
		cell = new Cell("B09");
		table.put(cell.getName(), cell);
		cell = new Cell("B10");
		table.put(cell.getName(), cell);
		cell = new Cell("B11");
		table.put(cell.getName(), cell);
		cell = new Cell("B12");
		table.put(cell.getName(), cell);
		cell = new Cell("B13");
		table.put(cell.getName(), cell);
		cell = new Cell("B14");
		table.put(cell.getName(), cell);
		cell = new Cell("B15");
		table.put(cell.getName(), cell);
		cell = new Cell("B16");
		table.put(cell.getName(), cell);
		cell = new Cell("B17");
		table.put(cell.getName(), cell);
		cell = new Cell("B18");
		table.put(cell.getName(), cell);
		cell = new Cell("B19");
		table.put(cell.getName(), cell);
		cell = new Cell("B20");
		table.put(cell.getName(), cell);
		cell = new Cell("B21");
		table.put(cell.getName(), cell);
		cell = new Cell("B22");
		table.put(cell.getName(), cell);
		cell = new Cell("B23");
		table.put(cell.getName(), cell);
		cell = new Cell("B24");
		table.put(cell.getName(), cell);
		cell = new Cell("B25");
		table.put(cell.getName(), cell);
		cell = new Cell("B26");
		table.put(cell.getName(), cell);
		cell = new Cell("B27");
		table.put(cell.getName(), cell);
		cell = new Cell("B28");
		table.put(cell.getName(), cell);
		cell = new Cell("B29");
		table.put(cell.getName(), cell);
		cell = new Cell("B30");
		table.put(cell.getName(), cell);
		cell = new Cell("B31");
		table.put(cell.getName(), cell);
		cell = new Cell("B32");
		table.put(cell.getName(), cell);
		cell = new Cell("B33");
		table.put(cell.getName(), cell);
		cell = new Cell("B34");
		table.put(cell.getName(), cell);
		cell = new Cell("B35");
		table.put(cell.getName(), cell);
		cell = new Cell("B36");
		table.put(cell.getName(), cell);
		cell = new Cell("B37");
		table.put(cell.getName(), cell);
		cell = new Cell("B38");
		table.put(cell.getName(), cell);
		cell = new Cell("B39");
		table.put(cell.getName(), cell);
		cell = new Cell("B40");
		table.put(cell.getName(), cell);
		cell = new Cell("B41");
		table.put(cell.getName(), cell);
		cell = new Cell("B42");
		table.put(cell.getName(), cell);

		cell = new Cell("E01");
		table.put(cell.getName(), cell);
		cell = new Cell("E02");
		table.put(cell.getName(), cell);
		cell = new Cell("E03");
		table.put(cell.getName(), cell);
		cell = new Cell("E04");
		table.put(cell.getName(), cell);
		cell = new Cell("E05");
		table.put(cell.getName(), cell);
		cell = new Cell("E06");
		table.put(cell.getName(), cell);
		cell = new Cell("E07");
		table.put(cell.getName(), cell);
		cell = new Cell("E08");
		table.put(cell.getName(), cell);
		cell = new Cell("E09");
		table.put(cell.getName(), cell);
		cell = new Cell("E10");
		table.put(cell.getName(), cell);
		cell = new Cell("E11");
		table.put(cell.getName(), cell);
		cell = new Cell("E12");
		table.put(cell.getName(), cell);
		cell = new Cell("E13");
		table.put(cell.getName(), cell);
		cell = new Cell("E14");
		table.put(cell.getName(), cell);
		cell = new Cell("E15");
		table.put(cell.getName(), cell);
		cell = new Cell("E16");
		table.put(cell.getName(), cell);
		cell = new Cell("E17");
		table.put(cell.getName(), cell);
		cell = new Cell("E18");
		table.put(cell.getName(), cell);
		cell = new Cell("E19");
		table.put(cell.getName(), cell);
		cell = new Cell("E20");
		table.put(cell.getName(), cell);
		cell = new Cell("E21");
		table.put(cell.getName(), cell);
		cell = new Cell("E22");
		table.put(cell.getName(), cell);
		cell = new Cell("E23");
		table.put(cell.getName(), cell);
		cell = new Cell("E24");
		table.put(cell.getName(), cell);
		cell = new Cell("E25");
		table.put(cell.getName(), cell);
		cell = new Cell("E26");
		table.put(cell.getName(), cell);
		cell = new Cell("E27");
		table.put(cell.getName(), cell);
		cell = new Cell("E28");
		table.put(cell.getName(), cell);
		cell = new Cell("E29");
		table.put(cell.getName(), cell);
		cell = new Cell("E30");
		table.put(cell.getName(), cell);
		cell = new Cell("E31");
		table.put(cell.getName(), cell);
		cell = new Cell("E32");
		table.put(cell.getName(), cell);

		cell = new Cell("F01");
		table.put(cell.getName(), cell);
		cell = new Cell("F02");
		table.put(cell.getName(), cell);
		cell = new Cell("F03");
		table.put(cell.getName(), cell);

		cell = new Cell("G01");
		table.put(cell.getName(), cell);
		cell = new Cell("G02");
		table.put(cell.getName(), cell);
		cell = new Cell("G03");
		table.put(cell.getName(), cell);
		cell = new Cell("G04");
		table.put(cell.getName(), cell);
		cell = new Cell("G05");
		table.put(cell.getName(), cell);
		cell = new Cell("G06");
		table.put(cell.getName(), cell);
		cell = new Cell("G07");
		table.put(cell.getName(), cell);
		cell = new Cell("G08");
		table.put(cell.getName(), cell);

		cell = new Cell("H00");
		table.put(cell.getName(), cell);
		cell = new Cell("H01");
		table.put(cell.getName(), cell);
		cell = new Cell("H02");
		table.put(cell.getName(), cell);
		cell = new Cell("H03");
		table.put(cell.getName(), cell);
		cell = new Cell("H04");
		table.put(cell.getName(), cell);
		cell = new Cell("H05");
		table.put(cell.getName(), cell);
		cell = new Cell("H06");
		table.put(cell.getName(), cell);
		cell = new Cell("H07");
		table.put(cell.getName(), cell);
		cell = new Cell("H08");
		table.put(cell.getName(), cell);
		cell = new Cell("H09");
		table.put(cell.getName(), cell);

		cell = new Cell("K01");
		table.put(cell.getName(), cell);
		cell = new Cell("K02");
		table.put(cell.getName(), cell);
		cell = new Cell("K03");
		table.put(cell.getName(), cell);
		cell = new Cell("K04");
		table.put(cell.getName(), cell);
		cell = new Cell("K05");
		table.put(cell.getName(), cell);
		cell = new Cell("K06");
		table.put(cell.getName(), cell);
		cell = new Cell("K07");
		table.put(cell.getName(), cell);
		cell = new Cell("K08");
		table.put(cell.getName(), cell);
		cell = new Cell("K09");
		table.put(cell.getName(), cell);
		cell = new Cell("K10");
		table.put(cell.getName(), cell);
		cell = new Cell("K11");
		table.put(cell.getName(), cell);
		cell = new Cell("K12");
		table.put(cell.getName(), cell);
		cell = new Cell("K13");
		table.put(cell.getName(), cell);
		cell = new Cell("K14");
		table.put(cell.getName(), cell);
		cell = new Cell("K15");
		table.put(cell.getName(), cell);
		cell = new Cell("K16");
		table.put(cell.getName(), cell);
		cell = new Cell("K17");
		table.put(cell.getName(), cell);
		cell = new Cell("K18");
		table.put(cell.getName(), cell);
		cell = new Cell("K19");
		table.put(cell.getName(), cell);
		cell = new Cell("K20");
		table.put(cell.getName(), cell);
		cell = new Cell("K21");
		table.put(cell.getName(), cell);
		cell = new Cell("K22");
		table.put(cell.getName(), cell);
		cell = new Cell("K23");
		table.put(cell.getName(), cell);
		cell = new Cell("K24");
		table.put(cell.getName(), cell);
		cell = new Cell("K25");
		table.put(cell.getName(), cell);
		cell = new Cell("K26");
		table.put(cell.getName(), cell);
		cell = new Cell("K27");
		table.put(cell.getName(), cell);
		cell = new Cell("K28");

		cell = new Cell("M01");
		table.put(cell.getName(), cell);
		cell = new Cell("M02");
		table.put(cell.getName(), cell);
		cell = new Cell("M03");
		table.put(cell.getName(), cell);
		cell = new Cell("M04");
		table.put(cell.getName(), cell);
		cell = new Cell("M05");
		table.put(cell.getName(), cell);
		cell = new Cell("M06");
		table.put(cell.getName(), cell);
		cell = new Cell("M07");
		table.put(cell.getName(), cell);
		cell = new Cell("M08");
		table.put(cell.getName(), cell);
		CellTableElaborator.elaborator(this, env);
	}

	public void addCell(Cell cell) {
		table.put(cell.getName(), cell);
	}

	public Iterator<Cell> getIterator() {
		return table.values().iterator();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<String> set = table.keySet();
		for (String key : set) {
			sb.append(table.get(key).toString() + "\n");
		}
		return sb.toString();
	}
}

class CellTableElaborator {
	public static void elaborator(CellTable table, UnitTable env) {
		Iterator<Unit> iterator = env.getIterator();

		Iterator<Timespace> itr;
		Timespace previous, current;
		// the outer loop is for different unit
		while (iterator.hasNext()) {
			itr = iterator.next().getList().iterator();
			// the inner loop is for different timespace of the current unit
			// for the first timespace, it is the arrival of the destination
			if (itr.hasNext()) {
				current = itr.next();
				if (current.getPosition().matches("[A-Z]\\d\\d")) {
					// System.out.println(current.getPosition());
					table.getCell(current.getPosition()).addDump(
							current.getDate(), 1);
					previous = current;

					while (itr.hasNext()) {
						current = itr.next();
						table.getCell(previous.getPosition()).addDump(
								current.getDate(), -1);

						if (current.getPosition().matches("[A-Z]\\d\\d")) {
							table.getCell(current.getPosition()).addDump(
									current.getDate(), 1);
							previous = current;
						} else {
							break;
						}
					}
				}
			}
		}
		Iterator<Cell> cellIterator = table.getIterator();
		while (cellIterator.hasNext()) {
			cellIterator.next().timeSeriesElaborator();
		}
	}
}
