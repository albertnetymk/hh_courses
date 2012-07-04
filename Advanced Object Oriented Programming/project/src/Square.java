/**
 * Utility class of immutable points in two dimensions. Useful in model classes.
 */
public class Square {

	public final int x, y;

	/**
	 * Creates an point in the plane.
	 */
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Equality means equal coordinates, as natural for immutable classes.
	 */
	public boolean equals(Square other) {
		return x == other.x && y == other.y;
	}

	/**
	 * Equality means equal coordinates, as natural for immutable classes.
	 */

	public boolean equals(Object other) {
		if (!(other instanceof Square))
			return false;
		else
			return equals((Square) other);
	}
}
