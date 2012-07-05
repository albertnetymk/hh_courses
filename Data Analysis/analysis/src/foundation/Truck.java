package foundation;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;


public class Truck {

	private String name;
	// to store the unparsed data
	private TreeMap<Date, String> prePath;
	// to store parsed data
	private TreeMap<Date, String> path;
	private double distance;

	public Truck(String name) {
		this.name = name;
		prePath = new TreeMap<Date, String>();
		path = new TreeMap<Date, String>();
		distance = 0;
	}

	// this method is mainly for Virtual Truck, since we have to decide
	// when the deliver was made
	public long minimal() {
		Iterator<Date> iterator = prePath.keySet().iterator();
		long minimal = 60*1000;
		Date current, next;
		if (iterator.hasNext()){	
			current = iterator.next();
			while(iterator.hasNext()) {
				next = iterator.next();
				minimal = (minimal > (next.getTime() - current.getTime()))? next.getTime()-current.getTime(): minimal;
				current = next;
			}
		}
		return minimal;
	}

	/*
	public void insertDelivery() {
		Iterator<Date> iterator = prePath.keySet().iterator();
		Date current, next, insert;
		if (iterator.hasNext()){	
			current = iterator.next();
			while(iterator.hasNext()) {
				next = iterator.next();
				if ((next.getTime() - current.getTime()) > 15*60*1000)
					insert = current.getTime() + 15*60*1000;
				else 
					insert = (current.getTime() + next.getTime())/2;
				prePath.put(new Date(insert),); 
				current = next;
			}
		}
	}
	*/



	public void addNode(Timespace current) {
		Date date = current.getDate();
		String node = current.getPosition();
		if (prePath.containsKey(date)) {
			if (!prePath.get(date).equals(node)) {
				System.out.println(current);
				System.out.print(prePath.get(date) + " VS " + node + "\n");
				System.exit(1);
			}
		}
		else
			prePath.put(date, node);
	}

	public void pathParsing() {
		if (!name.equals("VirtualTruck")) {
			Iterator<Date> iterator = prePath.keySet().iterator();
			Date current;
			String position;
			// the first time
			if(iterator.hasNext()) {
				current = iterator.next();
				position = prePath.get(current);
				path.put(current, prePath.get(current));

				while( iterator.hasNext() ) {
					current = iterator.next();
					position = prePath.get(current);
					// if the position is the same, we do nothing
					if (!path.get(path.lastKey()).equals(position))
						path.put(current, prePath.get(current));
				}
			}
		}
	}

	public void calulateDistance(Matrix map) {
		Iterator<Date> iterator = path.keySet().iterator();
		String current = null, next = null;
		if (iterator.hasNext())
			current = path.get(iterator.next());
		while (iterator.hasNext()) {
			next = path.get(iterator.next());
			distance = distance + map.getDistance(current, next);
			current = next;
		}
	}

	public List<String> getPathList() {
		List<String> list = new ArrayList<String>(path.size());
		Iterator<Date> iterator = path.keySet().iterator();
		while(iterator.hasNext()) {
			list.add(path.get(iterator.next()));
		}
		return list; 
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#"+ name+" :\n");
		Iterator<Date> iterator = path.keySet().iterator();
		Date tmp;
		while (iterator.hasNext()) {
			tmp = iterator.next();
			sb.append(Internationalization.dateFormat.format(tmp) + " : " + path.get(tmp) + " " +  nameMaping.getName().get(path.get(tmp)) + "\n");
		}
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public Map<Date, String> getPathMap() {
		return path;
	}

	public void setName(String name) {
		this.name = name;
	}
}
