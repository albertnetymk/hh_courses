import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * A view Component suitable for inclusion in an Swing JFrame. Paints itself by
 * consulting its model.
 */
@SuppressWarnings("serial")
public class SwingView extends JPanel implements GameView {
	private Dimension sqDim, modelDim;
	private int objectSize;
	private GameModel model;

	/**
	 * Creates a view where each GameObject has side length20 pixels..
	 */
	public SwingView(Dimension dim) {
		this(dim, 25);
	}

	/**
	 * Creates a view where each GameObject has a given size.
	 * 
	 * @param size
	 *            side length in pixels of each GameObject.
	 */
	public SwingView(Dimension dim, int size) {
		objectSize = size;
		modelDim = dim;
		sqDim = new Dimension(size, size);
		// in the unit of GameObject
		// there are 20 rows GameObjects and each row
		// contains 20 GameObjects
		setPreferredSize(new Dimension(dim.width * objectSize + objectSize / 2,
				dim.height * objectSize + objectSize / 2));
		setOpaque(true);
		setBorder(new TitledBorder("Playground"));
	}

	/**
	 * Implements the update method of Observer by just calling repaint
	 */
	public void update(java.util.Observable o, Object arg) {
		repaint();
	}

	@Override
	public void setModel(GameModel model) {
		this.model = model;
		this.model.addObserver(this);
		repaint();
	}

	/**
	 * Consults the model to paint the game matrix. If model is null, draws a
	 * default text.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (model != null) {
			if (model.getFinished()) {
				model.shutdownView(g, getWidth(), getHeight());
			} else {
				for (int i = 0; i < modelDim.width; i++) {
					for (int j = 0; j < modelDim.height; j++) {
						model.getState(i, j).draw(g,
								i * objectSize + objectSize / 4,
								j * objectSize + objectSize / 4, sqDim);
					}
				}
			}
		} else {
			Font font = new Font("SanSerif", Font.PLAIN, 20);
			FontMetrics fontMetrics = g.getFontMetrics(font);
			g.setFont(font);
			String tip = "Press G to (re)start game, Q to quit";
			g.drawString(tip, (getWidth() - fontMetrics.stringWidth(tip)) / 2,
					getHeight() / 2);
		}
	}
}
