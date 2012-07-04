import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Default JFrame, which has one menubar, which contains all the games.
 */
@SuppressWarnings("serial")
public class SwingMain extends JFrame {
	private GameFactory factory;
	private SwingView view;

	public SwingMain() {
		setLayout(new FlowLayout());

		// menu bar
		JMenuBar mb = new JMenuBar();
		JMenu m = new JMenu("Game");
		JMenuItem item1 = new JMenuItem("Gold Game");
		JMenuItem item2 = new JMenuItem("Shooter");
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory = new GameFactory() {
					private final Dimension d = new Dimension(20, 20);

					public GameModel newGameModel() {
						return new GoldModel(d.width, d.height);
					}

					public Dimension getDimension() {
						return d;
					}
				};
				init();
			}
		});
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory = new GameFactory() {
					private final Dimension d = new Dimension(30, 20);

					public GameModel newGameModel() {
						return new ShooterModel(d.width, d.height);
					}

					public Dimension getDimension() {
						return d;
					}
				};
				init();
			}
		});
		JMenuItem[] mis = { item1, item2 };

		for (JMenuItem mi : mis) {
			m.add(mi);
		}
		mb.add(m);
		setJMenuBar(mb);
		setVisible(true);
	}

	private void init() {
		if (view != null) {
			remove(view);
		}
		view = new SwingView(factory.getDimension());
		view.setBackground(Color.white);
		setTitle("Games");
		view.addKeyListener(new GameController(factory, view));
		add(view);
		setVisible(true);
		view.requestFocusInWindow();
		repaint();
	}

	public static void main(String[] args) {
		SwingConsole.run(SwingMain.class, 900, 600);
	}
}
