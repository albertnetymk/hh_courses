import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class RectangleObject extends GameObject {
	private Color color;

	public RectangleObject(Color color) {
		this.color = color;
	}

	@Override
	public void draw(Graphics g, int x, int y, Dimension d) {
		g.setColor(color);
		g.fillRect(x, y, d.width, d.height);
	}
}
