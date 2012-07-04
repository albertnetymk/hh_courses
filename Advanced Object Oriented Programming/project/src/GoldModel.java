import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * Sample game for illustration. Intentionally stupid; more interesting games to
 * be provided by students.
 * <p>
 * Initially 20 gold coins are randomly placed in the matrix. The red gold
 * collector aims to collect these coins which disappear after collection. Each
 * coin is randomly moved to a new position every n moves, where n is the number
 * of remaining coins. Game is won when all coins collected and lost when
 * collector leaves game board.
 */
public class GoldModel extends GameModel {
	protected static Map<String, Integer> scores = new HashMap<String, Integer>();

	private static List<String> sortByValue(final Map<String, Integer> m) {
		List<String> keys = new ArrayList<String>();
		keys.addAll(m.keySet());
		Collections.sort(keys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				Integer v1 = m.get(o1);
				Integer v2 = m.get(o2);
				if (v1 == null) {
					return (v2 == null) ? 0 : 1;
				} else {
					return v2.compareTo(v1);
				}
			}
		});
		return keys;
	}

	public static List<String> getScores() {
		return sortByValue(scores);
	}

	private static final int EAST = 0;
	private static final int WEST = 1;
	private static final int NORTH = 2;
	private static final int SOUTH = 3;

	// note that only one object of each type is needed.
	private GameObject coin = new RoundObject(new Color(255, 215, 0));
	private GameObject collector = new RoundObject(Color.red);
	private GameObject blank = new GameObject();

	private Vector coins = new Vector(); // current coin positions
	// the current collector position
	private Square coll = new Square(dim.width / 2, dim.height / 2);

	private int dir; // current direction of move
	private int score; // nr of coins found

	public GoldModel() {
		this(20, 20);
	}

	public GoldModel(int width, int height) {
		super(width, height);
		for (int i = 0; i < dim.width; i++) {
			for (int j = 0; j < dim.height; j++) {
				state[i][j] = blank;
			}
		}
		for (int i = 1; i < 20; i++) {
			setGold();
		}
	}

	public void setGold() {
		Square tmp;
		do {
			tmp = new Square((int) (Math.random() * dim.width), (int) (Math
					.random() * dim.height));
		} while (inCoins(tmp));
		state[tmp.x][tmp.y] = coin;
		coins.addElement(tmp);
	}

	private boolean inCoins(Square sq) {
		// relies on the fact that matrix elements share objects.
		return state[sq.x][sq.y] == coin;
	}

	private void updateDir(int key) {
		switch (key) {
		case KeyEvent.VK_LEFT:
			dir = WEST;
			break;
		case KeyEvent.VK_UP:
			dir = NORTH;
			break;
		case KeyEvent.VK_RIGHT:
			dir = EAST;
			break;
		case KeyEvent.VK_DOWN:
			dir = SOUTH;
			break;
		default:
			;
		}
	}

	private Square newPosition() {
		Square res = null;
		switch (dir) {
		case EAST:
			res = new Square(coll.x + 1, coll.y);
			break;
		case NORTH:
			res = new Square(coll.x, coll.y - 1);
			break;
		case WEST:
			res = new Square(coll.x - 1, coll.y);
			break;
		case SOUTH:
			res = new Square(coll.x, coll.y + 1);
			break;
		}
		return res;
	}

	public void keyPressed(int key) {
		updateDir(key);
	}

	protected void executeCommand(int key) throws GameOverException {
		try {
			state[coll.x][coll.y] = blank;
			coll = newPosition();
			state[coll.x][coll.y] = collector;
			boolean found = coins.removeElement(coll);
			if (found)
				score++;
			// move one coin using only Java 1.1 methods.
			Square old = (Square) coins.elementAt(0);
			coins.removeElementAt(0);
			state[old.x][old.y] = blank;
			setGold();
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new GameOverException();
		}
	}

	/**
	 * This method will be called when the game is finished, so that
	 * developers can collect any information.
	 */
	public void cleanUp() {
		StringBuilder name = new StringBuilder();
		while (name.length() == 0) {
			name.append(
					JOptionPane.showInputDialog(null, "Your name?")
					);
		}
		scores.put(name.toString(), score);
		setChanged();
		notifyObservers();
	}

	/**
	 * This method will be called when the game is finished so that
	 * developers can present some information.
	 */
	public void shutdownView(Graphics g, int width, int height) {
		Font font = new Font("SanSerif", Font.PLAIN, 20);
		FontMetrics fontMetrics = g.getFontMetrics(font);
		g.setFont(font);
		List<String> list = getScores();
		if (!list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			int i = 1;
			for (String player : list) {
				if(i>=4) {
					// only top 3 players
					break;
				}
				sb.append(player + " " + scores.get(player));
				g.drawString(sb.toString(), (width - fontMetrics
							.stringWidth(sb.toString())) / 2, height / 2
						+ fontMetrics.getHeight() * i);
				i++;
				sb.setLength(0);
			}
		}
	}
}
