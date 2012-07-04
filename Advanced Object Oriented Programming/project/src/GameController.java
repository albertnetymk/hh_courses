import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The controller class of the framework. Listens to user keystrokes and
 * <ul>
 * <li>quits the application if the keystroke is 'Q'.
 * <li>starts a new game if the keystroke is 'G'
 * <li>passes it on to the current game otherwise.
 * </ul>
 */
public class GameController extends KeyAdapter {
	private GameFactory factory;
	private GameView view;
	private GameThread currentGame;
	private int delay = 100;
	private int quitCode = KeyEvent.VK_Q;
	private int gameCode = KeyEvent.VK_G;

	public GameController(GameFactory factory, GameView view) {
		this.factory = factory;
		this.view = view;
	}

	/*
	 * Responsible for key pressed event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == quitCode) {
			System.exit(0);
		} else if (keyCode == gameCode) {
			GameModel model = factory.newGameModel();
			view.setModel(model);
			if (currentGame != null) {
				currentGame.setFinished(true);
			}
			currentGame = new GameThread(model, delay);
		} else if (currentGame != null) {
			currentGame.keyPressed(keyCode);
		}
	}

	/*
	 * Responsible for key released event.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (!(keyCode == quitCode || keyCode == gameCode)
				&& currentGame != null) {
			currentGame.keyReleased(keyCode);
		}
	}

	/*
	 * Responsible for key typed event.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (!(keyCode == quitCode || keyCode == gameCode)
				&& currentGame != null) {
			currentGame.keyTyped(keyCode);
		}
	}

	/**
	 * A thread that controls the running of one game instance.
	 */
	private static class GameThread extends Thread {
		private GameModel model;
		private int delay;
		private int command;

		/**
		 * Creates and starts a new game.
		 * 
		 * @param model
		 *            the model class for the game.
		 * @param delay
		 *            sleep time between moves.
		 */
		public GameThread(GameModel model, int delay) {
			this.model = model;
			this.delay = delay;
			start();
		}

		/**
		 * This method exists only for compatible reason.
		 * 
		 * @deprecated
		 * @return
		 */
		private synchronized int getCommand() {
			return command;
		}

		public synchronized void keyPressed(int keyCode) {
			model.keyPressed(keyCode);
		}

		public synchronized void keyReleased(int keyCode) {
			model.keyReleased(keyCode);
		}

		public synchronized void keyTyped(int keyCode) {
			model.keyTyped(keyCode);
		}

		/**
		 * Sets a boolean flag forcing the thread to terminate at next move.
		 * Called from event-dispatching thread and hence synchronized (an
		 * overkill in this case).
		 */
		public synchronized void setFinished(boolean finished) {
			model.setFinished(finished);
		}

		public synchronized boolean getFinished() {
			return model.getFinished();
		}

		public void run() {
			while (!getFinished()) {
				try {
					model.doCommand(getCommand());
					sleep(delay);
				} catch (GameOverException e) {
					setFinished(true);
					model.cleanUp();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
