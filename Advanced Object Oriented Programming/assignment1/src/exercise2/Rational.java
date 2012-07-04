package exercise2;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable class representing rational number.
 * 
 * Because there is not public or protected constructors, this class can not be
 * extended.
 * 
 * @author minyan09
 * 
 */
public class Rational implements Comparable<Rational> {
	private static Map<String, Rational> values = new HashMap<String, Rational>();

	private final int num;
	private final int den;

	public static Rational Plus(Rational a, Rational b) {
		return getInstance(a.num * b.den + b.num * a.den, a.den * b.den);
	}

	public static Rational Times(Rational a, Rational b) {
		return getInstance(a.num * b.num, a.den * b.den);
	}

	/**
	 * factory method
	 * 
	 * @param n
	 *            numerator
	 * @param d
	 *            denominator
	 * @return the rational number
	 */
	public static Rational getInstance(int n, int d) {
		Rational r = new Rational(n, d);
		if (values.containsKey(r.toString())) {
			return values.get(r.toString());
		} else {
			values.put(r.toString(), r);
			return r;
		}
	}

	/**
	 * Constructor for rational number. Called only by the factory method.
	 * 
	 * @param n
	 *            numerator
	 * @param d
	 *            denominator
	 * @throws ArithmeticException
	 */
	private Rational(int n, int d) throws ArithmeticException {
		if (d == 0) {
			throw new ArithmeticException("Denomitor can not be 0");
		}
		n = (d < 0) ? -n : n;
		d = Math.abs(d);
		int divisor = gcd(n, d);
		if (divisor != 1) {
			n /= divisor;
			d /= divisor;
		}
		num = n;
		den = d;
	}

	/**
	 * Greatest common divisor of two integers.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private int gcd(int a, int b) {
		if (b == 0) {
			return a;
		} else {
			return gcd(b, a % b);
		}
	}

	public boolean equals(Object r) {
		if (r == this) {
			return true;
		}
		if (r instanceof Rational) {
			if (getClass().getName().equals(r.getClass().getName())) {
				return ((Rational) r).num == num && ((Rational) r).den == den;
			}
		}

		return false;
	}

	public int hashCode() {
		return ((Integer) num).hashCode() + ((Integer) den).hashCode();
	}

	public String toString() {
		return getClass().getName() + ": " + num + "/" + den;
	}

	@Override
	public int compareTo(Rational r) {
		if (num * r.den > r.num * den) {
			return 1;
		} else if (num * r.den < r.num * den) {
			return -1;
		} else {
			return 0;
		}
	}

	private static class Test {
		public static void main(String[] args) {
			Rational r = Rational.getInstance(4, 2);
			System.out.println(r);
			Rational result = Rational.Plus(r, Rational.getInstance(3, 4));

			System.out.println(result);
			System.out.println(r.compareTo(result));
		}
	}

}
