package foundation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UnitTable {
	// to store all the units
	private Map<String, Unit> table;

	private Map<String, Order> orderTable;

	class Order {
		private String name;
		private String position;

		public Order(String name, String position) {
			this.name = name;
			this.position = position;
		}

		public String getPosition() {
			return position;
		}
	}

	// To store the unit code of the current unit
	private String currentUnit;

	public UnitTable() {
		currentUnit = "";
		table = new HashMap<String, Unit>();
		orderTable = new HashMap<String, Order>();
		UnitTableElaborator.elaborator(this, null, null);
	}

	public UnitTable(String beginDate, String endDate) {
		currentUnit = "";
		table = new HashMap<String, Unit>();
		orderTable = new HashMap<String, Order>();
		UnitTableElaborator.elaborator(this, beginDate, endDate);
	}

	public List<Integer> relocationStatistics() {
		int initial = 6;
		List<Integer> list = new ArrayList<Integer>(initial);
		for (int i = 0; i < initial; ++i) {
			list.add(0);
		}
		Iterator<String> iterator = table.keySet().iterator();
		// the current unit
		Unit unit;
		while (iterator.hasNext()) {
			unit = table.get(iterator.next());
			switch (unit.getList().size()) {
			case 0:
				list.set(0, list.get(0) + 1);
				break;
			case 1:
				list.set(1, list.get(1) + 1);
				break;
			case 2:
				list.set(2, list.get(2) + 1);
				break;
			case 3:
				list.set(3, list.get(3) + 1);
				break;
			case 4:
				list.set(4, list.get(4) + 1);
				break;
			case 5:
				list.set(5, list.get(5) + 1);
				break;
			default:
				System.out.println(unit);
				System.exit(1);
				// list.set(6, list.get(6) + 1);
				// break;
			}
		}
		return list;
	}

	public Unit getUnit(String key) {
		return table.get(key);
	}

	public Map<String, Unit> getUnitTable() {
		return table;
	}

	public void setUnitTable(Map<String, Unit> table) {
		this.table = table;
	}

	public Map<String, Order> getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(Map<String, Order> orderTable) {
		this.orderTable = orderTable;
	}

	public void addOrder(String orderCode, String position) {
		if(orderTable.isEmpty()) {
			orderTable.put(orderCode, new Order(orderCode, position));
		} else if (orderTable.containsKey(orderCode)) {
			if (!orderTable.get(orderCode).getPosition().equals(position)) {
				System.out.println("Order code is wrong");
				System.out.println(orderCode);
				System.out.println(orderTable.get(orderCode).getPosition() + " VS " + position);
				System.exit(1);
			}
		} else
			orderTable.put(orderCode, new Order(orderCode, position));
	}


	public void addUnit(Unit unit) {
		table.put(unit.getUnitcode(), unit);
		currentUnit = unit.getUnitcode();
	}

	public void removeLastUnit() {
		table.remove(currentUnit);
		currentUnit = "";
	}

	public boolean isNewUnit(String unit) {
		if (currentUnit.equals(unit)) {
			return false;
		} else {
			return true;
		}
	}

	public String getCurrentUnit() {
		return currentUnit;
	}

	public int size() {
		return table.size();
	}

	public Iterator<Unit> getIterator() {
		return table.values().iterator();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String key : table.keySet()) {
			sb.append(table.get(key).toString() + "\n");
		}
		return sb.toString();
	}

	// initialize all the information
	public static void init() {
		// to construct all the information without the limitation of time range
		UnitTable unitTable = new UnitTable();
		// unitTable.relocationStatistics();
		TruckTable truckTable = new TruckTable(unitTable);
		CellTable cellTable = new CellTable(unitTable);
		try {
			String unitDirectory = "unit";
			String truckDirectory = "truck";
			String cellDirectory = "cell";

			BufferedWriter out;

			out = new BufferedWriter(new FileWriter("UnitTable.txt"));
			out.write(unitTable.toString());
			out.close();

			/*
			// to make one directory called truck, and put text files named after each truck.
			(new File(truckDirectory)).mkdir();
			Iterator<String> truckIterator = truckTable.getKeySet().iterator();
			while( truckIterator.hasNext() ) {
				Truck truck = truckTable.getTruck(truckIterator.next());
				out = new BufferedWriter(new FileWriter(truckDirectory+"/"+truck.getName()+".txt"));
				out.write(truck.toString());
				out.close();
			}

			// to make one directory called cell, and put text files named after each cell.
			(new File(cellDirectory)).mkdir();
			Iterator<String> cellIterator = cellTable.getKeySet().iterator();
			while(cellIterator.hasNext()) {
				Cell cell = cellTable.getCell(cellIterator.next());
				out = new BufferedWriter(new FileWriter(cellDirectory+"/"+cell.getName()+".txt"));
				out.write(cell.toString());
				out.close();
			}
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// initialize all the information, this is the base we will be working on
		// therefore, this method should be called only once, unless you want to override the generated foundational information
		// comment it out, when you have constructed the foundation
		init();
	}
}

class UnitTableElaborator {

	public static void elaborator(UnitTable unitTable, String beginDate, String endDate) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/Thesis";
			Connection con = DriverManager.getConnection(url, "thesis",
					"thesis");

			Statement statement = con.createStatement();
			ResultSet result;
			if ( beginDate == null) 
				result = statement
					.executeQuery("select UNITCODE, UBY, UDATE, OLDWHLOCCODE, NEWWHLOCCODE, ACTCODE, OLDQUALCAT, NEWQUALCAT, OLDORDECODE, NEWORDECODE"
							+ " from hylte "
							// + " where UNITCODE = \"101005820806\";");
							// + " order by UNITCODE, UDATE " + " limit 4000;");
					+ " order by UNITCODE, UDATE;");
			else 
				result = statement
					.executeQuery("select UNITCODE, UBY, UDATE, OLDWHLOCCODE, NEWWHLOCCODE, ACTCODE, OLDQUALCAT, NEWQUALCAT, OLDORDECODE, NEWORDECODE"
							+ " from hylte " 
							+ " where UDATE > \"" + beginDate +  "\" and UDATE < \"" + endDate + "\""
							+ " order by UNITCODE, UDATE;");

			String unitcode;
			String uby;
			java.util.Date udate;
			String oldwhloccode;
			String newwhloccode;
			String actcode;
			String oldqualcat;
			String newqualcat;
			String oldordecode;
			String newordecode;
			/*
			   while (result.next()) {
			   unitcode = result.getString("UNITCODE");
			   uby = result.getString("UBY");
			   newwhloccode = result.getString("NEWWHLOCCODE");
			   actcode = result.getString("ACTCODE");
			   newordecode = result.getString("NEWORDECODE");
			// check if it has a new unitcode
			if (unitTable.isNewUnit(unitcode)) {
			// check if this reel is produced during this period we
			// choose if not, we will just ignore this one
			if (actcode.equals("PROD")) {
			Unit unit = new Unit(unitcode, null, newwhloccode,
			newordecode);
			unitTable.addUnit(unit);
			unitTable.addOrder(newordecode, newwhloccode);
			}
			} else {
			// this time we only need to pick the information of order code, so we ignore all others
			continue;
			}

			}
			unitTable.getUnitTable().clear();
			 */
			while (result.next()) {
				unitcode = result.getString("UNITCODE");
				uby = result.getString("UBY");
				udate = result.getTimestamp("UDATE");
				oldwhloccode = result.getString("OLDWHLOCCODE");
				newwhloccode = result.getString("NEWWHLOCCODE");
				actcode = result.getString("ACTCODE");
				oldqualcat = result.getString("OLDQUALCAT");
				newqualcat = result.getString("NEWQUALCAT");
				oldordecode = result.getString("OLDORDECODE");
				newordecode = result.getString("NEWORDECODE");
				// check if it has a new unitcode
				if (unitTable.isNewUnit(unitcode)) {
					// check if this reel is produced during this period we
					// choose if not, we will just ignore this one
					if (actcode.equals("PROD")) {
						// it is possible that newhlccode is "OPL", but we do
						// not have enough information to determine what the
						// destination of this unit really is, so we just put it
						// "OPL", and correct it later
						Unit unit = new Unit(unitcode, udate, newwhloccode,
								newordecode);
						unitTable.addUnit(unit);
					}
				} else {
					if (actcode.equals("CANCEL")) {
						unitTable.removeLastUnit();
					} else {
						if (uby.matches("HYLTRUCK\\d\\d")) {
							// ignore the duplicate data
							if (!newwhloccode.equals(oldwhloccode)) {
								// only deal with the movement within level one
								// uncomment the following line if we need to deal with A, E, etc.
								// if (newwhloccode.matches("[A-Z](\\d\\d)?")
								if (newwhloccode.matches("[A-Z]\\d\\d")) {
									unitTable.getUnit(unitTable.getCurrentUnit())
										.addTimespace(udate, newwhloccode,
												uby);
								}
								// only deal with the movement from level one to
								// level two
								else if (newwhloccode.matches("(LU\\d\\d)") || newwhloccode.matches("KAJ\\d\\d")) 
									unitTable.getUnit(unitTable.getCurrentUnit()) .addTimespace(udate, newwhloccode, uby);
								else if (newwhloccode.matches("^[0-9]{10}$")) 
									unitTable.getUnit(unitTable.getCurrentUnit()) .addTimespace(udate, "Robot", uby);


							}
						}
						// we only deal with the situation that UBY is HYLTRUCK
						// Therefore, there is no else
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
