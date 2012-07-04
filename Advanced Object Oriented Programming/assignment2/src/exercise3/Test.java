package exercise3;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static String[] filter(String[] a, Filter f) {
		List<String> list = new ArrayList<String>();
		for (String element : a) {
			if (f.accept(element)) {
				list.add(element);
			}
		}
		String[] result = new String[list.size()];
		return list.toArray(result);
	}

	public static void main(String[] args) {
		String[] input1 = { "hello", "fruit", "apples" };
		String[] input2 = { "hello", "abccba" };
		String[] result = Test.filter(input1, new EndingsFilter());
		for (String e : result) {
			System.out.println(e);
		}
		result = Test.filter(input2, new PalindromsFilter());
		for (String e : result) {
			System.out.println(e);
		}
	}
}
