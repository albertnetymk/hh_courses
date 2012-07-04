import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * A circular game object with constant color.
 */
public class RoundObject extends GameObject {

	private Color color;

	/**
	 * Creates a circular game object.
	 * 
	 * @param color
	 *            the color of the object.
	 */
	public RoundObject(Color color) {
		this.color = color;
	}

	/**
	 * Draws itself in a given graphics context, position and size.
	 * 
	 * @param x
	 *            game matrix column index.
	 * @param y
	 *            game matrix row index.
	 * @param d
	 *            size of this object in pixels.
	 */
	@Override
	public void draw(Graphics g, int x, int y, Dimension d) {
		g.setColor(color);
		g.fillOval(x, y, d.width, d.height);
	}
}
