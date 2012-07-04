package exercise1;

import java.util.Comparator;

/**
 * To Compare two balls according to their radius
 * 
 * @author minyan09
 * 
 */
public class Compare implements Comparator<Ball> {

	@Override
	public int compare(Ball b1, Ball b2) {
		if (b1.getRadius() > b2.getRadius()) {
			return 1;
		} else if (b1.getRadius() < b2.getRadius()) {
			return -1;
		} else {
			return 0;
		}
	}
}
