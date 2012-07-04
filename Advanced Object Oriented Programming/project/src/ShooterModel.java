import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

public class ShooterModel extends GameModel {
	private static enum DIRECTION {
		STILL, UP, DOWN
	}

	private static GameObject block = new RectangleObject(Color.blue);
	private static GameObject health = new RectangleObject(Color.red);
	private static GameObject blank = new GameObject();
	private static int HEIGHT = 5;
	// assume that width is larger than 10
	private static int HEALTH = 5;

	private List<Bullet> bullets = new ArrayList<Bullet>();
	private Bullet b;
	private int y0, y1;
	private int width, height;
	private DIRECTION dir0, dir1;
	private int winner;
	private int health0, health1;

	public ShooterModel(int width, int height) {
		super(width, height);
		this.width = width;
		this.height = height;
		dir0 = dir1 = DIRECTION.STILL;
		winner = -1;
		health0 = health1 = HEALTH;

		// initialize the playground
		for (int i = 0; i < dim.width; i++) {
			for (int j = 0; j < dim.height; j++) {
				state[i][j] = blank;
			}
		}

		// initialize the health
		for (int i = 0; i < HEALTH; i++) {
			state[i][0] = health;
			state[dim.width - i - 1][0] = health;
		}

		// initialize the players
		y0 = y1 = (height - HEIGHT) / 2;
		for (int i = 0; i < HEIGHT; i++) {
			state[0][y0 + i] = block;
			state[width - 1][y1 + i] = block;
		}
	}

	private void updatePlayerPosition() {
		if (dir0 == DIRECTION.UP) {
			if (y0 > 0) {
				state[0][y0 - 1] = block;
				state[0][y0 - 1 + HEIGHT] = blank;
				y0--;
			}
		} else if (dir0 == DIRECTION.DOWN) {
			if (y0 < height - HEIGHT) {
				state[0][y0 + HEIGHT] = block;
				state[0][y0] = blank;
				y0++;
			}
		}
		if (dir1 == DIRECTION.UP) {
			if (y1 > 0) {
				state[width - 1][y1 - 1] = block;
				state[width - 1][y1 - 1 + HEIGHT] = blank;
				y1--;
			}
		} else if (dir1 == DIRECTION.DOWN) {
			if (y1 < height - HEIGHT) {
				state[width - 1][y1 + HEIGHT] = block;
				state[width - 1][y1] = blank;
				y1++;
			}
		}
	}

	private void updateBulletPosition() throws GameOverException {
		for (Iterator<Bullet> i = bullets.iterator(); i.hasNext();) {
			b = i.next();
			// position
			state[b.getX()][b.getY()] = blank;
			if (b.getPlayer() == 1) {
				b.setX(b.getX() + 1);
			} else {
				b.setX(b.getX() - 1);
			}
			if (b.getX() >= width || b.getX() < 0) {
				i.remove();
				continue;
			}
			// check overlap
			if (state[b.getX()][b.getY()] == block) {
				if (b.getPlayer() == 1) {
					// player1 is hit
					i.remove();
					state[dim.width - health1][0] = blank;
					health1--;
					if (health1 <= 0) {
						winner = 0;
						throw new GameOverException();
					}
				}
				if (b.getPlayer() == 0) {
					// player0 is hit
					i.remove();
					state[health0 - 1][0] = blank;
					health0--;
					if (health0 <= 0) {
						winner = 1;
						throw new GameOverException();
					}
				}
			} else {
				state[b.getX()][b.getY()] = b;
			}
		}
	}

	@Override
	protected void keyPressed(int key) {
		switch (key) {
		// player0
		case KeyEvent.VK_W:
			dir0 = DIRECTION.UP;
			break;
		case KeyEvent.VK_S:
			dir0 = DIRECTION.DOWN;
			break;
		case KeyEvent.VK_SPACE:
			b = new Bullet(1, Color.red, 1, y0 + HEIGHT / 2, new Dimension(10,
					5));
			state[b.getX()][b.getY()] = b;
			bullets.add(b);
			break;
		// player1
		case KeyEvent.VK_UP:
			dir1 = DIRECTION.UP;
			break;
		case KeyEvent.VK_DOWN:
			dir1 = DIRECTION.DOWN;
			break;
		case KeyEvent.VK_ENTER:
			b = new Bullet(0, Color.red, width - 2, y1 + HEIGHT / 2,
					new Dimension(10, 5));
			state[b.getX()][b.getY()] = b;
			bullets.add(b);
			break;
		default:
			;
		}
	}

	@Override
	protected void keyReleased(int key) {
		switch (key) {
		// player0
		case KeyEvent.VK_W:
			dir0 = DIRECTION.STILL;
			break;
		case KeyEvent.VK_S:
			dir0 = DIRECTION.STILL;
			break;
		// player1
		case KeyEvent.VK_UP:
			dir1 = DIRECTION.STILL;
			break;
		case KeyEvent.VK_DOWN:
			dir1 = DIRECTION.STILL;
			break;
		default:
			;
		}
	}

	@Override
	protected void keyTyped(int key) {
		// nothing in this model
	}

	@Override
	protected void executeCommand(int key) throws GameOverException {
		updatePlayerPosition();
		updateBulletPosition();
	}

	@Override
	public void cleanUp() {
		JOptionPane.showMessageDialog(null, "Player " + winner
				+ " is the winner.");
	}
}
