import java.awt.Dimension;
import java.awt.Graphics;

/**
 * An abstract game object with no visual appearance.
 */
public class GameObject {
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
	public void draw(Graphics g, int x, int y, Dimension d) {
	}
}
