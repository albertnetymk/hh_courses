import java.util.Observer;
import java.util.Observable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class EnvironmentView extends JPanel implements Observer {
	private static double scale = 0.8;
	private Environment env;
	private Agent agent;
	private int height;
	private int width;

	private Block[][] blocks;

	public EnvironmentView(Agent agent, Dimension d) {
		setPreferredSize(d);
		setBackground(Color.white);
		height = Environment.HEIGHT;
		width = Environment.WIDTH;
		this.env = agent.env;
		this.env.addObserver(this);
		this.agent = agent;
		blocks = new Block[height][width];
		setLayout(new GridLayout(height, width, 2, 2));
		setBorder(new MatteBorder(2, 2, 2, 2, Color.white));

		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				blocks[i][j] = new Block(agent.row == i && agent.col == j, !env.getFloor(i, j));
				add(blocks[i][j]);
			}
		}

		setVisible(true);
	}

	public void initUsingEnvironment(Environment env) {
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				this.env.setFloor(i, j, env.getFloor(i, j));
				blocks[i][j].setDirty(!env.getFloor(i,j));
				blocks[i][j].removeAgent();
			}
		}
		blocks[agent.row][agent.col].putAgent();
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		int[] tmpArray = (int[])arg;
		blocks[tmpArray[0]][tmpArray[1]].setDirty(true);
	}

	public void update() {
		blocks[agent.oldRow][agent.oldCol].setDirty(!env.getFloor(agent.oldRow, agent.oldCol));
		blocks[agent.oldRow][agent.oldCol].removeAgent();
		blocks[agent.row][agent.col].putAgent();
	}

	private class Block extends JPanel {
		private boolean hasAgent;
		private boolean isDirty;

		public Block(boolean hasAgent, boolean isDirty) {
			this.hasAgent = hasAgent;
			this.isDirty = isDirty;
			// setBorder(new LineBorder(Color.blue));
			setVisible(true);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (isDirty) {
				g.setColor(Color.black);
				g.fillRect((int) (getSize().width * (1 - scale) / 2),
						(int) (getSize().height * (1 - scale) / 2),
						(int) (getSize().width * scale),
						(int) (getSize().height * scale));
			}
			if (hasAgent) {
				g.setColor(Color.red);
				g.fillOval((int) (getSize().width * (1 - scale) / 2),
						(int) (getSize().height * (1 - scale) / 2),
						(int) (getSize().width * scale),
						(int) (getSize().height * scale));
			}
		}

		public void putAgent() {
			hasAgent = true;
			repaint();
		}

		public void removeAgent() {
			hasAgent = false;
			repaint();
		}

		public void setDirty(boolean b) {
			isDirty = b;
			repaint();
		}
	}
}
