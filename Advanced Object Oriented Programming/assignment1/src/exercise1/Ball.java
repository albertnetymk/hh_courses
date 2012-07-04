package exercise1;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A ball with center position, radius and color.
 * 
 * @author minyan09
 * 
 */
public class Ball implements Cloneable {

	private int centerX, centerY, radius;
	private Color color;

	/**
	 * To construct one ball.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param r
	 *            radius
	 * @param c
	 *            color
	 */
	public Ball(int x, int y, int r, Color c) {
		centerX = x;
		centerY = y;
		radius = r;
		color = c;
	}

	/**
	 * increase x, y by 10 points
	 */
	public void move() {
		centerX = centerX + 10;
		centerY = centerY + 10;
	}

	/**
	 * Increase x coordinate by x, and y coordinate by y.
	 * 
	 * @param x
	 *            x increase
	 * @param y
	 *            y increase
	 */
	public void move(int x, int y) {
		centerX = centerX + x;
		centerY = centerY + y;
	}

	/**
	 * Report the current state of this object.
	 */
	public void report() {
		System.out.println("Ball with radius " + radius + " and coordinates ("
				+ centerX + "," + centerY + ")" + " and color " + color);
	}

	/**
	 * Graphic report
	 * 
	 * @param pen
	 */
	public void report(Graphics pen) {
		pen.setColor(color);
		pen
				.fillOval(centerX - radius, centerY - radius, 2 * radius,
						2 * radius);
	}

	/**
	 * Get the radius of this ball.
	 * 
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Get the color of this ball.
	 * 
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Check if the two object have the same radius and color. The object must
	 * be the exact type of Ball.
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Ball) {
			if (getClass().getName().equals(obj.getClass().getName())) {
				return ((Ball) obj).getRadius() == radius
						&& ((Ball) obj).getColor().equals(color);
			}
		}
		return false;
	}

	/**
	 * Hashcode depends on radius and color, as equals implies.
	 */
	public int hashCode() {
		return ((Integer) radius).hashCode() + color.hashCode();
	}

	public String toString() {
		return getClass().getName() + ": " + "Ball with radius " + radius
				+ " and coordinates (" + centerX + "," + centerY + ")"
				+ " and color " + color;
	}

	private static class Test {
		public static void main(String[] args)
				throws CloneNotSupportedException {
			Ball b = new Ball(100, 100, 50, Color.red);
			Ball b_cloned = (Ball) b.clone();
			System.out.println(b.equals(b_cloned));
			Ball cb = new ChildBall(100, 100, 50, Color.red, 1);
			System.out.println(b.equals(cb));
		}
	}
}
