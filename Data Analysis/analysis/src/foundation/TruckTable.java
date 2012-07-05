package foundation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TruckTable {
	private Map<String, Truck> table;

	public Truck getTruck(String key) {
		return table.get(key);
	}

	public Set<String> getKeySet() {
		return table.keySet();
	}

	public TruckTable(UnitTable env) {
		table = new HashMap<String, Truck>();
		Truck truck;
		truck = new Truck("VirtualTruck");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK01");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK02");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK03");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK04");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK05");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK06");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK07");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK08");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK09");
		table.put(truck.getName(), truck);
		truck = new Truck("HYLTRUCK10");
		table.put(truck.getName(), truck);
		TruckTableElaborate.elaborator(this, env);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(table.get("VirtualTruck").toString() + "\n");
		for (int i = 1; i <= 9; ++i) {
			sb.append(table.get("HYLTRUCK0" + i).toString() + "\n");
		}
		sb.append(table.get("HYLTRUCK10").toString() + "\n");
		return sb.toString();
	}
}

class TruckTableElaborate {
	public static void elaborator(TruckTable truckTable, UnitTable env) {
		Iterator<Unit> unitIterator = env.getIterator();
		Unit currentUnit;
		Iterator<Timespace> timespaceIterator;
		Timespace currentTimespace;
		// to iterator through all units
		while (unitIterator.hasNext()) {
			currentUnit = unitIterator.next();
			truckTable.getTruck("VirtualTruck").addNode(currentUnit.getOrigin());
			timespaceIterator = currentUnit.getList().iterator();
			// to iterator through all timespace of this unit
			while (timespaceIterator.hasNext()) {
				currentTimespace = timespaceIterator.next();
				truckTable.getTruck(currentTimespace.getUby()).addNode(currentTimespace);
			}
		}
		// now TruckTable is finished
		// System.out.println(truckTable.getTruck("VirtualTruck").minimal());
		// truckTable.getTruck("VirtualTruck").insertDelivery();
		Iterator<String> truckIterator = truckTable.getKeySet().iterator();
		String truckName;
		while (truckIterator.hasNext()) {
			truckName = truckIterator.next();
			truckTable.getTruck(truckName).pathParsing();
		}
	}
}
