package foundation;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Internationalization {
	public static TimeZone timeZone = TimeZone.getTimeZone("Europe/Stockholm");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd' 'HH:mm:ss", Locale.ENGLISH);
	static {
		dateFormat.setTimeZone(timeZone);
	}
}
