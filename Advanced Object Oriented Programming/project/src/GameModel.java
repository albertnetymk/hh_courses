import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Common superclass for all game model classes. A particular game model must
 * implement executeCommand. Constructors of subclasses should initiate matrix
 * elements and additional, game-dependent fields.
 */
public class GameModel extends java.util.Observable {
	private boolean finished;
	protected GameObject[][] state;
	protected final Dimension dim;

	public GameModel(int width, int height) {
		dim = new Dimension(width, height);
		state = new GameObject[dim.width][dim.height];
		finished = false;
	}

	public GameModel() {
		this(20, 20);
	}

	public void setFinished(boolean f) {
		finished = f;
	}

	public boolean getFinished() {
		return finished;
	}

	/**
	 * Returns the GameObject in logical position (x,y). Called by views for
	 * painting.
	 */
	public GameObject getState(int x, int y) {
		return state[x][y];
	}

	/**
	 * Update of the game state in response to a user keystroke.
	 * 
	 * @param key
	 *            the user keystroke.
	 */
	public void doCommand(int key) throws GameOverException {
		executeCommand(key);
		setChanged();
		notifyObservers();
	}

	/**
	 * This event will change the state of the game.
	 * 
	 * @param key
	 *            key from the keyboard
	 */
	protected void keyPressed(int key) {
	}

	/**
	 * This event will change the state of the game.
	 * 
	 * @param key
	 *            key from the keyboard
	 */
	protected void keyReleased(int Key) {
	}

	/**
	 * This event will change the state of the game.
	 * 
	 * @param key
	 *            key from the keyboard
	 */
	protected void keyTyped(int key) {
	}

	/**
	 * This method will be called constantly by the thread. The logic of game
	 * should be in here.
	 * 
	 * @param key
	 * @throws GameOverException
	 */
	protected void executeCommand(int key) throws GameOverException {
	}

	/**
	 * This method will be called when the method is over.
	 */
	protected void cleanUp() {
	}

	/**
	 * This method will be called when the method is over.
	 * 
	 * @param g
	 * @param width
	 * @param height
	 */
	protected void shutdownView(Graphics g, int width, int height) {
	}
}
