package exercise1;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * One class to calculate how many days have elapsed since one fixed date.
 * 
 * @author minyan09
 * 
 */
public class Day extends GregorianCalendar {
	public Day(int year, int month, int day) {
		super(year, month, day);
	}

	/**
	 * Return how many days have passed since that day.
	 * 
	 * @param other
	 *            The particular day
	 * @return the number of days have passed
	 */
	public int daysFrom(Calendar other) {
		int n = 0;

		while (this.after(other)) {
			n++;
			other.add(Calendar.DATE, 1);
		}
		while (this.before(other)) {
			n--;
			other.add(Calendar.DATE, -1);
		}
		return n;
	}

	public static void main(String[] args) {
		Day calendar = new Day(2011, 3, 14);
		calendar.setTimeZone(TimeZone.getTimeZone("HongKong"));

		System.out.println(calendar
				.daysFrom(new GregorianCalendar(1990, 5, 29)));
	}
}
