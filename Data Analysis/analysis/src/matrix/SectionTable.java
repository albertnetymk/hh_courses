package matrix;

import java.util.HashMap;
import java.util.Map;

public class SectionTable {
	private Map<String, Section> table;

	public Section getSection(String name) {
		return table.get(name);
	}

	public SectionTable() {
		table = new HashMap<String, Section>();
		Section section;
		section = new SectionSimple("ConveyorLeft", 1, 0, 0);
		table.put(section.getName(), section);
		section = new SectionSimple("ConveyorRight", 1, 0, 0);
		table.put(section.getName(), section);
		section = new SectionSimple("A", 31, 10.59, 3.12);
		table.put("A", section);
		section = new SectionB("B");
		table.put("B", section);
		section = new SectionSimple("E", 32, 14.37, 2.70);
		table.put("E", section);
		section = new SectionF("F");
		table.put("F", section);
		section = new SectionSimple("G", 8, 14.99, 2.88);
		table.put("G", section);
		section = new SectionSimple("H", 10, 14.70, 2.98);
		table.put("H", section);
		section = new SectionSimple("K", 28, 6.86, 3.19);
		table.put("K", section);
		section = new SectionSimple("M", 8, 9.98, 2.38);
		table.put("M", section);
		section = new SectionSimple("LU", 10, 0, 0);
		table.put("LU", section);
		for (int i = 1; i <= 7; ++i) {
			section = new SectionSimple("KAJ0" + i, 1, 0, 0);
			table.put(section.getName(), section);
		}
		section = new SectionSimple("Robot", 1, 0, 0);
		table.put(section.getName(), section);
	}
}
