package foundation;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Cell {

	private String name;
	private TreeMap<Date, Integer> list;
	private PriorityQueue<Dump> queue;

	public Cell(String name) {
		this.name = name;
		list = new TreeMap<Date, Integer>();
		queue = new PriorityQueue<Dump>(10, new Compare());
	}

	public void timeSeriesElaborator() {

		Dump current;
		if (!queue.isEmpty()) {
			current = queue.poll();
			list.put(current.getTime(), current.getChange());

			while (!queue.isEmpty()) {
				current = queue.poll();
				list.put(current.getTime(), current.getChange() + list.get(list.lastKey()));
			}
		}
	}

	public TreeMap<Date, Integer> getList() {
		return list;
	}

	public void addDump(Date date, int change) {
		Dump dump = new Dump(date, change);
		queue.add(dump);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#"+ name + "  :\n");
		Iterator<Date> iterator = list.keySet().iterator();
		while (iterator.hasNext()) {
			Date time = iterator.next();
			sb.append(Internationalization.dateFormat.format(time) + " " + list.get(time) + "\n"  );
		}
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PriorityQueue<Dump> getQueue() {
		return queue;
	}
}

class Dump {
	private Date time;
	private int change;

	public Dump(Date time, int change) {
		this.time = time;
		this.change = change;
	}

	public String toString() {
		return time.toString() + " " + change;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getChange() {
		return change;
	}

	public void setChange(int change) {
		this.change = change;
	}
}

class Compare implements Comparator<Dump> {
	public int compare(Dump o1, Dump o2) {
		return o1.getTime().compareTo(o2.getTime());
	}
}
