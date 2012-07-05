package foundation;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

class Unit {
	private String unitcode;
	// to store the instant when and where this unit is produced
	private Timespace origin;
	private String destination;
	private String ordercode;

	// to store the location and the corresponding instant
	private LinkedList<Timespace> list;

	public Unit(String unitcode, Date date, String destination, String ordercode) {
		this.unitcode = unitcode;

		this.destination = destination;
		this.ordercode = ordercode;
		list = new LinkedList<Timespace>();
		// ordercode is used to determine the origin
		if (ordercode.matches("HYSE") || ordercode.matches("HYNO"))
			origin = new Timespace(date, "ConveyorLeft"); // 25%
		else
			origin = new Timespace(date, "ConveyorRight"); // 75%

		// less than or equal 15 minutes later, this unit is transferred to its
		// destination
		// by one virtual truck
		// the exact time is unknown, and we need to appoint one
		// this time should not contradict with other operation of this virtual
		// truck
		// so we can not decide it yet, for we do not have enough information
		// now
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(unitcode + " ");
		sb.append(origin + " " + destination + "\n");
		sb.append("**********************************************\n");
		ListIterator<Timespace> iterator = list.listIterator();
		while (iterator.hasNext()) {
			sb.append(iterator.next() + "\n");
		}
		return sb.toString();
	}

	public void addTimespace(Date date, String position, String uby) {
		if (list.isEmpty()) {
			// if the destination is "OPL"
			if (destination.equals("OPL") || destination.matches("^[A-Z]$")) {
				destination = position;
				// we do not add new Timespace here
			} else if (!(destination.equals(position))) {
				list.add(new Timespace(date, position, uby));
			}
		} else {
			Timespace tmp = list.getLast();
			// ignore the case that truck drivers just scan the unit
			// to get some information
			if (!(tmp.getPosition().equals(destination))
					&& !(tmp.getPosition().equals(position))) {
				Timespace ts = new Timespace(date, position, uby);
				list.add(ts);
			}
		}
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public Timespace getOrigin() {
		return origin;
	}

	public void setOrigin(Timespace origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public LinkedList<Timespace> getList() {
		return list;
	}
}
