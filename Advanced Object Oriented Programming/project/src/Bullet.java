import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * This Class is used in Shooter game.
 * 
 * @author minyan09
 * 
 */
public class Bullet extends GameObject {
	private Color color;
	// coordinate in model
	private int x, y;
	private final Dimension d;
	private int player;

	public Bullet(int p, Color color, int x, int y, Dimension d) {
		player = p;
		this.color = color;
		this.x = x;
		this.y = y;
		this.d = d;
	}

	@Override
	public void draw(Graphics g, int x, int y, Dimension d) {
		g.setColor(color);
		g.fillRect(x, y, this.d.width, this.d.height);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	/**
	 * The target of this bullet.
	 * 
	 * @return player number
	 */
	public int getPlayer() {
		return player;
	}
}
