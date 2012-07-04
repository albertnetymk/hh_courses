package exercise2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.Icon;

/**
 * An icon that has the shape of the planet Mars.
 */
public class MarsIcon implements Icon {
	/**
	 * Constructs a Mars icon of a given size.
	 * 
	 * @param aSize
	 *            the size of the icon
	 */
	public MarsIcon(int aSize) {
		size = aSize;
	}

	public int getIconWidth() {
		return size;
	}

	public int getIconHeight() {
		return size;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		Ellipse2D.Double planet = new Ellipse2D.Double(x, y, size, size);
		g2.setColor(Color.RED);
		g2.fill(planet);
	}

	private int size;
}
