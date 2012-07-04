package exercise3;

public class PalindromsFilter implements Filter {
	@Override
	public boolean accept(String x) {
		int left = 0;
		int right = x.length() - 1;

		while (left < right) {
			if (x.charAt(left) != x.charAt(right)) {
				return false;
			}
			left++;
			right--;
		}

		return true;
	}
}
