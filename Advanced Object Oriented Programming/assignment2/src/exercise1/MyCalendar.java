package exercise1;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Print out the calendar for 2011 march
 * 
 * @author minyan09
 * 
 */
public class MyCalendar extends GregorianCalendar {
	/**
	 * 
	 * @param year
	 * @param month
	 *            0 is January
	 */
	public MyCalendar(int year, int month) {
		super(year, month, 1);
	}

	public static void main(String[] args) {
		Calendar calendar = new MyCalendar(2011, Calendar.MARCH);
		StringBuilder sb = new StringBuilder();
		if (calendar.getFirstDayOfWeek() == Calendar.MONDAY) {
			sb.append("\tM");
			sb.append("\tT");
			sb.append("\tW");
			sb.append("\tT");
			sb.append("\tF");
			sb.append("\tS");
			sb.append("\tS");
			System.out.println(sb.toString());
			sb.setLength(0);
			int first = 0;
			switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				first = 1;
				break;
			case Calendar.TUESDAY:
				first = 2;
				break;
			case Calendar.WEDNESDAY:
				first = 3;
				break;
			case Calendar.THURSDAY:
				first = 4;
				break;

			case Calendar.FRIDAY:
				first = 5;
				break;
			case Calendar.SATURDAY:
				first = 6;
				break;
			case Calendar.SUNDAY:
				first = 7;
				break;

			}
			// the space before everything
			for (int j = 1; j < first; ++j) {
				sb.append("\t ");
			}
			int lineChange = 0;
			for (int i = 1; i <= calendar
					.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
				if (sb.length() == 14) {
					lineChange = i;
					sb.append("\n");
				} else if ((i - lineChange) % 7 == 0) {
					sb.append("\n");
				}
				sb.append("\t" + i);
			}
			System.out.println(sb.toString());
		} else {
			sb.append("\tS");
			sb.append("\tM");
			sb.append("\tT");
			sb.append("\tW");
			sb.append("\tT");
			sb.append("\tF");
			sb.append("\tS");

			System.out.println(sb.toString());
			sb.setLength(0);
			int first = 0;
			switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				first = 2;
				break;
			case Calendar.TUESDAY:
				first = 3;
				break;
			case Calendar.WEDNESDAY:
				first = 4;
				break;
			case Calendar.THURSDAY:
				first = 5;
				break;

			case Calendar.FRIDAY:
				first = 6;
				break;
			case Calendar.SATURDAY:
				first = 7;
				break;
			case Calendar.SUNDAY:
				first = 1;
				break;

			}
			// the space before everything
			for (int j = 1; j < first; ++j) {
				sb.append("\t ");
			}
			int lineChange = 0;
			for (int i = 1; i <= calendar
					.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
				if (sb.length() == 14) {
					lineChange = i;
					sb.append("\n");
				} else if ((i - lineChange) % 7 == 0) {
					sb.append("\n");
				}
				sb.append("\t" + i);
			}
			System.out.println(sb.toString());
		}
	}
}
