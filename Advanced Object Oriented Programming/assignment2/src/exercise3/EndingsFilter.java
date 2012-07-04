package exercise3;

public class EndingsFilter implements Filter {

	@Override
	public boolean accept(String x) {
		if (x.endsWith("s")) {
			return true;
		} else {
			return false;
		}
	}

}
